package service;

import daoInterface.MonthlyFeeDAO;
import daoInterface.TransactionStatementDAO;
import model.MonthlyFee;
import views.stateModel.StatusMessageModel;
import model.TransactionStatement;
import model.Users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;


@RequestScoped
@Transactional
public class MonthlyFeeService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    @Inject
    private MonthlyFeeDAO monthlyFeeDAO;

    @Inject
    private TransactionStatementDAO transactionStatementDAOImp;

    public StatusMessageModel assignStudentMonthlyFee(Users student, double assignFeeAmount){
        try {
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
            if (checkAssignFee != null){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("This Month Fee Already Assign");
                return statusMessageModel;
            }
            if (!monthlyFeeDAO.add(assignFee)){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("Unsuccessful Fee Assign");
                return statusMessageModel;
            }
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("Assign Fee Successfully");
        }catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while assign fee.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public StatusMessageModel payFee(MonthlyFee selectFee, double payAmount, String payStatus){
        try{
            double totalPayAmount = selectFee.getPaid() + payAmount;
            double totalDueAmount = selectFee.getDue() - payAmount;
            if (payStatus.equals("COMPLETED")){
                selectFee.setPaid(totalPayAmount);
                selectFee.setDue(totalDueAmount);
                if (totalDueAmount == 0){
                    transactionStatementDAOImp.paymentCompletedByAdmin(selectFee);
                }
                if (!monthlyFeeDAO.update(selectFee)){
                    statusMessageModel.setStatus(false);
                    statusMessageModel.setMessage("Failed to Pay Fee");
                    return statusMessageModel;
                }
                if (!addPayTransactionStatement(selectFee.getStudentId(),selectFee,payAmount, totalDueAmount, payStatus)){
                    statusMessageModel.setStatus(false);
                    statusMessageModel.setMessage("Fee Payment Success but not Add Transaction History");
                    return statusMessageModel;
                }
                statusMessageModel.setStatus(true);
                statusMessageModel.setMessage("Fee Payment Success");
                return statusMessageModel;
            }
            if (payStatus.equals("PENDING")) {
                if (!addPayTransactionStatement(selectFee.getStudentId(),selectFee,payAmount, totalDueAmount, payStatus)){
                    statusMessageModel.setStatus(false);
                    statusMessageModel.setMessage("Payment Failed");
                    return statusMessageModel;

                }
                statusMessageModel.setStatus(true);
                statusMessageModel.setMessage("Your Payment Request Successfully");
                return statusMessageModel;
            }
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("Payment Failed");

        }catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while pay fee.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public boolean addPayTransactionStatement(Users selectUser, MonthlyFee selectAssignFee, double payAmount, double newDue , String payStatus){
        try{
            Date date = new Date();
            Timestamp payDate = new Timestamp(date.getTime());
            TransactionStatement newPayTransaction = new TransactionStatement();
            newPayTransaction.setStudentId(selectUser);
            newPayTransaction.setFeeId(selectAssignFee);
            newPayTransaction.setPayAmount(payAmount);
            newPayTransaction.setPaymentDate(payDate);
            newPayTransaction.setStatementDue(newDue);
            newPayTransaction.setStatus(payStatus);
            return transactionStatementDAOImp.add(newPayTransaction);
        }catch (PersistenceException e){
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public StatusMessageModel responsePaymentRequest(TransactionStatement selectTransaction){
        try{
            selectTransaction.setStatus("COMPLETED");
            if (!transactionStatementDAOImp.update(selectTransaction)){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("Payment not Accepted");
                return statusMessageModel;
            }
            MonthlyFee updateFee = selectTransaction.getFeeId();
            double totalPay = updateFee.getPaid()+selectTransaction.getPayAmount();
            double totalDue = updateFee.getDue() - selectTransaction.getPayAmount();
            updateFee.setDue(totalDue);
            updateFee.setPaid(totalPay);
            if (!monthlyFeeDAO.update(updateFee)){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("Payment Accepted but not update monthly fee");
                return statusMessageModel;
            }
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("Payment Accepted Successfully");
        }catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while approve fee payment.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }
}
