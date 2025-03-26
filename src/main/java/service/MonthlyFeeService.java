package service;

import daoImp.TransactionStatementDAOImp;
import daoInterface.MonthlyFeeDAO;
import model.MonthlyFee;
import model.StatusMessageModel;
import model.TransactionStatement;
import model.Users;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

public class MonthlyFeeService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private MonthlyFeeDAO monthlyFeeDAO;
    private TransactionStatementDAOImp transactionStatementDAOImp;

    public MonthlyFeeService(MonthlyFeeDAO monthlyFeeDAO, TransactionStatementDAOImp transactionStatementDAOImp) {
        this.monthlyFeeDAO = monthlyFeeDAO;
        this.transactionStatementDAOImp = transactionStatementDAOImp;
    }

    public StatusMessageModel assignStudentMonthlyFee(Users student, double assignFeeAmount){
        String currentMonthUpper = LocalDate.now()
                .getMonth()
                .toString();
        int year = Year.now().getValue();
        Date date = new Date();
        Timestamp issueDate = new Timestamp(date.getTime());

        MonthlyFee assignFee = new MonthlyFee();
        assignFee.setFeeAmount(assignFeeAmount);
        assignFee.setStudentId(student);
        assignFee.setDue(assignFeeAmount);
        assignFee.setPaid(0);
        assignFee.setMonth(currentMonthUpper);
        assignFee.setYear(year);
        assignFee.setIssueDate(issueDate);
        MonthlyFee checkAssignFee = monthlyFeeDAO.checkAssignFee(student.getId(), currentMonthUpper , year);
        if (checkAssignFee == null){
            if (monthlyFeeDAO.add(assignFee)){
                statusMessageModel.setStatus(true);
                statusMessageModel.setMessage("Assign Fee Successfully");
            }else {
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("Unsuccessful Fee Assign");
            }
        }else {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("This Month Fee Already Assign");
        }
        return statusMessageModel;
    }

    public StatusMessageModel payFee(MonthlyFee selectFee, double payAmount){
        double totalPayAmount = selectFee.getPaid() + payAmount;
        double totalDueAmount = selectFee.getDue() - payAmount;
        selectFee.setPaid(totalPayAmount);
        selectFee.setDue(totalDueAmount);
        if (monthlyFeeDAO.update(selectFee)){
            if (addPayTransactionStatement(selectFee.getStudentId(),selectFee,payAmount)){
                statusMessageModel.setStatus(true);
                statusMessageModel.setMessage("Fee Payment Success");
            }else {
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("Fee Payment Success but not Add Transaction History");
            }
        }else {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("Failed to Pay Fee");
        }
        return statusMessageModel;
    }

    public boolean addPayTransactionStatement(Users selectUser, MonthlyFee selectAssignFee, double payAmount){
        Date date = new Date();
        Timestamp payDate = new Timestamp(date.getTime());
        TransactionStatement newPayTransaction = new TransactionStatement();
        newPayTransaction.setStudentId(selectUser);
        newPayTransaction.setFeeId(selectAssignFee);
        newPayTransaction.setPayAmount(payAmount);
        newPayTransaction.setPaymentDate(payDate);
        if (transactionStatementDAOImp.add(newPayTransaction)){
            return true;
        }else {
            return false;
        }
    }
}
