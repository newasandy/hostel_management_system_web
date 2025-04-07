package views.stateModel;

import model.MonthlyFee;
import model.TransactionStatement;
import model.Users;

import java.util.List;

public class MonthlyFeeState {

    private boolean feeContext = false;
    private Users selectStudent;
    private double assignFeeAmount;
    private double paidAmount;
    private MonthlyFee selectForPayFee;
    private TransactionStatement selectTransaction;
    private String verifyPassword;
    private List<MonthlyFee> monthlyFeeList;
    private List<TransactionStatement> statementListEachStudent;
    private List<TransactionStatement> pendingPaymentRequest;
    private double selectStudentDueAmount;
    private Users loginUser;

    public MonthlyFeeState() {
    }

    public boolean isFeeContext() {
        return feeContext;
    }

    public void setFeeContext(boolean feeContext) {
        this.feeContext = feeContext;
    }

    public Users getSelectStudent() {
        return selectStudent;
    }

    public void setSelectStudent(Users selectStudent) {
        this.selectStudent = selectStudent;
    }

    public double getAssignFeeAmount() {
        return assignFeeAmount;
    }

    public void setAssignFeeAmount(double assignFeeAmount) {
        this.assignFeeAmount = assignFeeAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public MonthlyFee getSelectForPayFee() {
        return selectForPayFee;
    }

    public void setSelectForPayFee(MonthlyFee selectForPayFee) {
        this.selectForPayFee = selectForPayFee;
    }

    public TransactionStatement getSelectTransaction() {
        return selectTransaction;
    }

    public void setSelectTransaction(TransactionStatement selectTransaction) {
        this.selectTransaction = selectTransaction;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public List<MonthlyFee> getMonthlyFeeList() {
        return monthlyFeeList;
    }

    public void setMonthlyFeeList(List<MonthlyFee> monthlyFeeList) {
        this.monthlyFeeList = monthlyFeeList;
    }

    public List<TransactionStatement> getStatementListEachStudent() {
        return statementListEachStudent;
    }

    public void setStatementListEachStudent(List<TransactionStatement> statementListEachStudent) {
        this.statementListEachStudent = statementListEachStudent;
    }

    public List<TransactionStatement> getPendingPaymentRequest() {
        return pendingPaymentRequest;
    }

    public void setPendingPaymentRequest(List<TransactionStatement> pendingPaymentRequest) {
        this.pendingPaymentRequest = pendingPaymentRequest;
    }


    public double getSelectStudentDueAmount() {
        return selectStudentDueAmount;
    }

    public void setSelectStudentDueAmount(double selectStudentDueAmount) {
        this.selectStudentDueAmount = selectStudentDueAmount;
    }

    public Users getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Users loginUser) {
        this.loginUser = loginUser;
    }

    public void resetField(){
        this.assignFeeAmount = 1;
        this.paidAmount = 0;
        this.selectStudent = null;
        this.selectTransaction = null;
        this.verifyPassword = "";
    }
}
