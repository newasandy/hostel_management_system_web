package daoInterface;

import model.MonthlyFee;

import java.util.List;

public interface MonthlyFeeDAO extends BaseDAO<MonthlyFee> {
    List<MonthlyFee> getUserFeeDetails(Long userId);
    MonthlyFee checkAssignFee(Long studentId, String month, int years);
    double getTotalDueAmount(Long userId);
}