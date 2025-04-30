package dto;

import java.util.Map;

public class LazyLoadRequest {

    private int first;
    private int pageSize;
    private String sortField;
    private String sortOrder;          // "ASC" or "DESC"
    private Map<String, String> filters;       // key → input‐value (contains/LIKE)
    private Map<String, Object> exactFilters;  // key → exact match value
    private boolean unAllocatedUser;

    public LazyLoadRequest() {
    }

    public boolean isUnAllocatedUser() {
        return unAllocatedUser;
    }

    public void setUnAllocatedUser(boolean unAllocatedUser) {
        this.unAllocatedUser = unAllocatedUser;
    }

    public Map<String, Object> getExactFilters() {
        return exactFilters;
    }

    public void setExactFilters(Map<String, Object> exactFilters) {
        this.exactFilters = exactFilters;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }
}
