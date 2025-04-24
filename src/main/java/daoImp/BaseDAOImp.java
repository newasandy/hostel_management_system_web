package daoImp;

import daoInterface.BaseDAO;
import model.RoomAllocation;
import model.Rooms;
import model.Users;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortOrder;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public abstract class BaseDAOImp <T> implements BaseDAO<T>, Serializable {
    private final Class<T> entityClass;

    public BaseDAOImp(Class<T> entity){
        this.entityClass=entity;
    }

    @PersistenceContext(unitName = "hostelmanagement")
    protected EntityManager entityManager;


    @Override
    public boolean add(T entity){
        boolean status = false;
        if (entityManager == null) {
            return status;
        }
        try{
            entityManager.persist(entity);
            status = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public boolean update(T entity){
        boolean status = false;
        if (entityManager == null) {
            return status;
        }

        try{
            entityManager.merge(entity);
            status = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public boolean delete(Long id){
        boolean status = false;
        if (entityManager == null) {
            return status;
        }

        try{
            T entity = entityManager.find(entityClass , id);
            if (entity != null){
                entityManager.remove(entity);
            }
            status = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public T getById(Long id){
        if (entityManager == null) {
            return null;
        }

        try{
            return entityManager.find(entityClass ,id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve entity by ID: " + id, e);
        }
    }

    @Override
    public List<T> getAll() {
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try {
            return entityManager.createQuery("SELECT e FROM " + entityClass.getName() + " e", entityClass)
                    .getResultList();
        }catch (PersistenceException e){
            return Collections.emptyList();
        }
    }

    @Override
    public Long getCount(){
        if (entityManager == null) {
            return 0L;
        }
        try{
            Long result = entityManager.createQuery("SELECT COUNT(*) FROM "+ entityClass.getName() +" e",Long.class)
                    .getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {

            return 0L;
        }
    }


    @Override
    public List<T> findPaginatedEntities(
            Map<String, FilterMeta> filters,
            Map<String, Object> exactMatchFilters,
            int first,
            int pageSize,
            String sortField,
            SortOrder sortOrder,
            boolean unallocatedStudent
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        // Combine all filters (partial and exact)
        List<Predicate> allPredicates = new ArrayList<>();
        allPredicates.addAll(buildFilters(cb, root, filters));
        allPredicates.addAll(buildExactFilters(cb, root, exactMatchFilters));

        if (unallocatedStudent){
            Predicate customPredicate = buildCustomPredicate(cb, root);
            if (customPredicate != null) {
                allPredicates.add(customPredicate);
            }
        }


        // Apply the filters to the query
        if (!allPredicates.isEmpty()) {
            cq.where(cb.and(allPredicates.toArray(new Predicate[0])));
        }

        // Apply sorting
        applySorting(cb, cq, root, sortField, sortOrder);

        // Create and execute the query with pagination
        TypedQuery<T> query = entityManager.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public int getTotalEntityCount(Map<String, FilterMeta> filters, Map<String, Object> exactMatchFilters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> root = countQuery.from(entityClass);
        countQuery.select(cb.count(root));

        // Combine all filters (partial and exact)
        List<Predicate> allPredicates = new ArrayList<>();
        allPredicates.addAll(buildFilters(cb, root, filters));
        allPredicates.addAll(buildExactFilters(cb, root, exactMatchFilters));

        // Apply the filters to the count query
        if (!allPredicates.isEmpty()) {
            countQuery.where(cb.and(allPredicates.toArray(new Predicate[0])));
        }

        // Execute the count query
        TypedQuery<Long> query = entityManager.createQuery(countQuery);
        Long result = query.getSingleResult();
        return result != null ? result.intValue() : 0;
    }


    private Predicate buildCustomPredicate(CriteriaBuilder cb, Root<T> root) {
        if (entityClass.equals(Users.class)) {
            Subquery<Long> subquery = cb.createQuery().subquery(Long.class);
            Root<?> ra = subquery.from(RoomAllocation.class);
            subquery.select(ra.get("studentId").get("id"))
                    .where(cb.isNull(ra.get("unallocationDate")));

            Predicate rolePredicate = cb.equal(getPath(root, "roles.userTypes"), "USER");
            Predicate statusPredicate = cb.isTrue((Path<Boolean>)getPath(root, "status"));
            Predicate notInSubquery = cb.not(root.get("id").in(subquery));

            return cb.and(rolePredicate, statusPredicate, notInSubquery);
        }

        if (entityClass.equals(Rooms.class)) {
            Subquery<Long> countSubquery = cb.createQuery().subquery(Long.class);
            Root<RoomAllocation> ra = countSubquery.from(RoomAllocation.class);
            countSubquery.select(cb.count(ra));
            countSubquery.where(
                    cb.equal(ra.get("roomId").get("id"), root.get("id")),
                    cb.isNull(ra.get("unallocationDate"))
            );

            Predicate statusPredicate = cb.isTrue((Path<Boolean>) getPath(root, "status"));
            Predicate capacityCheck = cb.greaterThan(
                    (Path<Integer>) getPath(root, "capacity"),
                    cb.toInteger(countSubquery) // ✅ fix here
            );
            return cb.and(statusPredicate, capacityCheck);
        }
        return cb.conjunction();
    }



    public List<Predicate> buildFilters(CriteriaBuilder cb, Root<T> root, Map<String, FilterMeta> filters) {
        List<Predicate> predicates = new ArrayList<>();

        if (filters != null) {
            for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object filterValue = entry.getValue().getFilterValue();

                if (filterValue != null && !filterValue.toString().trim().isEmpty()) {
                    Path<?> path = getPath(root, field);

                    // Apply LIKE for string fields
                    predicates.add(cb.like(
                            cb.lower(path.as(String.class)),
                            "%" + filterValue.toString().toLowerCase() + "%"
                    ));
                }
            }
        }
        return predicates;
    }

    public List<Predicate> buildExactFilters(CriteriaBuilder cb, Root<T> root, Map<String, Object> filters) {
        List<Predicate> predicates = new ArrayList<>();

        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object filterValue = entry.getValue();

                if (filterValue != null) {
                    Path<?> path = getPath(root, field);
                    predicates.add(cb.equal(path, filterValue));
                }
            }
        }

        return predicates;
    }

    // ✅ 5. Sorting
    public void applySorting(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                             String sortField, SortOrder sortOrder) {
        if (sortField != null && !sortField.trim().isEmpty()) {
            Path<?> sortPath = getPath(root, sortField);
            cq.orderBy(sortOrder == SortOrder.DESCENDING
                    ? cb.desc(sortPath)
                    : cb.asc(sortPath));
        }
    }

    // ✅ 6. Helper to support nested paths (e.g. "wallet.id")
    private Path<?> getPath(Root<T> root, String fieldPath) {
        Path<?> path = root;
        for (String part : fieldPath.split("\\.")) {
            path = path.get(part);
        }
        return path;
    }
}
