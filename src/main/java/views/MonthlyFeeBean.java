package views;

import daoImp.MonthlyFeeDAOImpl;
import daoImp.TransactionStatementDAOImp;
import daoImp.UserDAOImpl;
import daoInterface.MonthlyFeeDAO;
import daoInterface.UsersDAO;
import model.MonthlyFee;
import model.StatusMessageModel;
import model.TransactionStatement;
import model.Users;
import service.MonthlyFeeService;
import utils.GetCookiesValues;
import utils.PasswordUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named("monthlyFeeBean")
@ViewScoped
public class MonthlyFeeBean implements Serializable {

    private MonthlyFeeDAO monthlyFeeDAO = new MonthlyFeeDAOImpl();
    private TransactionStatementDAOImp transactionStatementDAOImp = new TransactionStatementDAOImp();
    private UsersDAO usersDAO = new UserDAOImpl();
    private MonthlyFeeService monthlyFeeService = new MonthlyFeeService(monthlyFeeDAO, transactionStatementDAOImp);

    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    private boolean feeContext = false;
    private Users selectStudent;
    private double assignFeeAmount;
    private double paidAmount;
    private MonthlyFee selectForPayFee;
    private String verifyPassword;
    private List<MonthlyFee> monthlyFeeList;
    private List<MonthlyFee> monthlyFeeListEachUser;
    private List<TransactionStatement> statementListEachStudent;
    private String userRole = GetCookiesValues.getUserRoleFromCookie();
    private double selectStudentDueAmount;
    private Users loginUser;

    @PostConstruct
    public void init(){
        refreshMonthlyFeeList();
    }

    public void refreshMonthlyFeeList(){
        monthlyFeeList = monthlyFeeDAO.getAll();
        Collections.sort(monthlyFeeList, new Comparator<MonthlyFee>() {
            @Override
            public int compare(MonthlyFee v1, MonthlyFee v2) {
                return v2.getIssueDate().compareTo(v1.getIssueDate());
            }
        });
        if ("USER".equals(userRole)){
            loginUser = usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie());
            monthlyFeeListEachUser = monthlyFeeDAO.getUserFeeDetails(loginUser.getId());
            selectStudentDueAmount = monthlyFeeDAO.getTotalDueAmount(loginUser.getId());
            statementListEachStudent = transactionStatementDAOImp.getStatementByEachUser(loginUser.getId());
        }
        if (selectStudent != null){
            statementListEachStudent = transactionStatementDAOImp.getStatementByEachUser(selectStudent.getId());
        }
    }

    public List<TransactionStatement> getStatementListEachStudent() {
        return statementListEachStudent;
    }

    public double getSelectStudentDueAmount() {
        return selectStudentDueAmount;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public Users getLoginUser() {
        return loginUser;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public List<MonthlyFee> getMonthlyFeeListEachUser() {
        return monthlyFeeListEachUser;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public List<MonthlyFee> getMonthlyFeeList() {
        return monthlyFeeList;
    }

    public MonthlyFee getSelectForPayFee() {
        return selectForPayFee;
    }

    public void setSelectForPayFee(MonthlyFee selectForPayFee) {
        this.selectForPayFee = selectForPayFee;
    }

    public boolean isFeeContext() {
        return feeContext;
    }

    public void setFeeContext(boolean feeContext) {
        this.feeContext = feeContext;
    }

    public double getAssignFeeAmount() {
        return assignFeeAmount;
    }

    public void setAssignFeeAmount(double assignFeeAmount) {
        this.assignFeeAmount = assignFeeAmount;
    }

    public Users getSelectStudent() {
        return selectStudent;
    }

    public void setSelectStudent(Users selectStudent) {
        this.selectStudent = selectStudent;
    }

    public void assignMonthlyFee(){

        try{
            statusMessageModel = monthlyFeeService.assignStudentMonthlyFee(selectStudent,assignFeeAmount);
            if (statusMessageModel.isStatus()){
                resetField();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
            }
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to Assign Monthly Fee."));
        }
    }

    public void monthlyFeePay(){
        Users loginAdmin = usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie());
        if (PasswordUtils.verifyPassword(verifyPassword , loginAdmin.getPasswords())){
            if (paidAmount<= selectForPayFee.getDue() && paidAmount > 0){
                statusMessageModel = monthlyFeeService.payFee(selectForPayFee,paidAmount);
                if (statusMessageModel.isStatus()){
                    resetField();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
                }else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
                }
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Paid Amount is Invalid"));
            }
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid Password."));
        }
    }

    public void viewStatementForStudent(Users student){
        selectStudent = student;
        selectStudentDueAmount = monthlyFeeDAO.getTotalDueAmount(student.getId());
        statementListEachStudent = transactionStatementDAOImp.getStatementByEachUser(student.getId());
    }

    public void resetField(){
        this.assignFeeAmount = 1;
        this.paidAmount = 0;
        this.selectStudent = null;
    }
}
