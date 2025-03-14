package daoInterface;

import java.util.List;

public interface BaseDAO<T> {
    boolean add(T entity);
    boolean update(T entity);
    boolean delete(Long id);
    T getById(Long id);
    List<T> getAll();
}
