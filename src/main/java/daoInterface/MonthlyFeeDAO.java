package daoInterface;

import model.MonthlyFee;

import java.util.List;

public interface MonthlyFeeDAO extends BaseDAO<MonthlyFee> {
    List<MonthlyFee> getUserFeeDetails(Long userId);
    List<MonthlyFee> getUserUnPaidFee(Long userId);
    List<MonthlyFee> getAllUserUnPaidFee();
}