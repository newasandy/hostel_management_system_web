package daoInterface;

import model.Visitors;

import java.util.List;

public interface VisitorsDAO extends BaseDAO<Visitors> {
    List<Visitors> getUserVisitedBy(Long userId);
    Visitors getRecentUserVisitor(Long userId);
}