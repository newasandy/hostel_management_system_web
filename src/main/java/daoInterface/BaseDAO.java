package daoInterface;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortOrder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

public interface BaseDAO<T> {
    boolean add(T entity);
    boolean update(T entity);
    boolean delete(Long id);
    T getById(Long id);
    List<T> getAll();
    Long getCount();

    List<T> findPaginatedEntities(
            Map<String, FilterMeta> filters,
            Map<String, Object> exactMatchFilters,
            int first,
            int pageSize,
            String sortField,
            SortOrder sortOrder,
            boolean unallocatedStudent
    );
    int getTotalEntityCount(Map<String, FilterMeta> filters, Map<String, Object> exactMatchFilters);
    void applySorting(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, String sortField, SortOrder sortOrder);
    List<Predicate> buildFilters(CriteriaBuilder cb, Root<T> root, Map<String, FilterMeta> filters);
}
