package views.stateModel;

import daoInterface.BaseDAO;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GenericLazyDataModel<T> extends LazyDataModel<T> {

    private BaseDAO<T> dao;

    private Map<String, Object> exactMatchFilters;

    private boolean unAllocatedUser;

    public GenericLazyDataModel(BaseDAO<T> dao,Map<String, Object> matchFilters, boolean unAllocatedUser) {
        this.dao=dao;
        this.exactMatchFilters = (matchFilters != null) ? matchFilters : Collections.emptyMap();
        this.unAllocatedUser = unAllocatedUser;
    }


    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filters) {

        if (dao == null) {
            throw new RuntimeException("DAO is not injected properly!");
        }

        List<T> results = dao.findPaginatedEntities(filters, exactMatchFilters, first, pageSize, sortField, sortOrder,unAllocatedUser);
        int count = dao.getTotalEntityCount(filters, exactMatchFilters);
        this.setRowCount(count);
        return results;
    }

    @Override
    public String getRowKey(T entity) {
        try {
            return String.valueOf(entity.getClass().getMethod("getId").invoke(entity));
        } catch (Exception e) {
            throw new RuntimeException("Entity does not have getId() method", e);
        }
    }

    @Override
    public T getRowData(String rowKey) {
        try {
            Long id = Long.valueOf(rowKey);
            return dao.getById(id);
        } catch (Exception e) {
            throw new RuntimeException("Unable to convert rowKey to ID or find entity", e);
        }
    }
}