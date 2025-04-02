package daoInterface;

import model.MonthlyFee;
import model.TransactionStatement;

import java.util.List;

public interface TransactionStatementDAO extends BaseDAO<model.TransactionStatement> {

    List<TransactionStatement> getStatementByEachUser(Long userId);
    List<TransactionStatement> getPendingPaymentRequest();
    boolean paymentCompletedByAdmin(MonthlyFee selectedFee);
}
