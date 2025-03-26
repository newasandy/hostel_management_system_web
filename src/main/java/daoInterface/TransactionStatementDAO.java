package daoInterface;

import model.TransactionStatement;

import java.util.List;

public interface TransactionStatementDAO extends BaseDAO<model.TransactionStatement> {

    List<TransactionStatement> getStatementByEachUser(Long userId);
}
