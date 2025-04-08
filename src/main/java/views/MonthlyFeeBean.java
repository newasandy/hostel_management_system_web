package views;

import daoInterface.MonthlyFeeDAO;
import daoInterface.TransactionStatementDAO;
import daoInterface.UsersDAO;
import model.MonthlyFee;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.MonthlyFeeState;
import views.stateModel.StatusMessageModel;
import model.Users;
import service.MonthlyFeeService;
import utils.PasswordUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named
@ViewScoped
public class MonthlyFeeBean implements Serializable {

    @Inject
    private MonthlyFeeDAO monthlyFeeDAO;

    @Inject
    private TransactionStatementDAO transactionStatementDAOImp;

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private MonthlyFeeService monthlyFeeService;

    private StatusMessageModel statusMessageModel;
    private MonthlyFeeState monthlyFeeState;

    @PostConstruct
    public void init(){
        statusMessageModel = new StatusMessageModel();
        monthlyFeeState = new MonthlyFeeState();
        refreshMonthlyFeeList();
    }

    public void refreshMonthlyFeeList(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (SessionUtils.isSessionValid(request) && JwtUtils.isTokenValid(SessionUtils.getToken(request))){
            monthlyFeeState.setLoginUser(usersDAO.getByEmail(JwtUtils.getUserEmail(SessionUtils.getToken(request))));
            if ("USER".equals(monthlyFeeState.getLoginUser().getRoles().getUserTypes())){
                monthlyFeeState.setMonthlyFeeList(monthlyFeeDAO.getUserFeeDetails(monthlyFeeState.getLoginUser().getId()));
                monthlyFeeState.setSelectStudentDueAmount(monthlyFeeDAO.getTotalDueAmount(monthlyFeeState.getLoginUser().getId()));
                monthlyFeeState.setStatementListEachStudent(transactionStatementDAOImp.getStatementByEachUser(monthlyFeeState.getLoginUser().getId()));
            }
            if ("ADMIN".equals(monthlyFeeState.getLoginUser().getRoles().getUserTypes())){
                List<MonthlyFee> allMonthlyFee = monthlyFeeDAO.getAll();
                Collections.sort(allMonthlyFee, new Comparator<MonthlyFee>() {
                    @Override
                    public int compare(MonthlyFee v1, MonthlyFee v2) {
                        return v2.getIssueDate().compareTo(v1.getIssueDate());
                    }
                });
                monthlyFeeState.setMonthlyFeeList(allMonthlyFee);
                monthlyFeeState.setPendingPaymentRequest(transactionStatementDAOImp.getPendingPaymentRequest());
            }
            if (monthlyFeeState.getSelectStudent() != null){
                monthlyFeeState.setStatementListEachStudent(transactionStatementDAOImp.getStatementByEachUser(monthlyFeeState.getSelectStudent().getId()));
            }
        }else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?expired=true");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void assignMonthlyFee(){
        try{
            statusMessageModel = monthlyFeeService.assignStudentMonthlyFee(monthlyFeeState.getSelectStudent(), monthlyFeeState.getAssignFeeAmount());
            if (statusMessageModel.isStatus()){
                monthlyFeeState.resetField();
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
        if (PasswordUtils.verifyPassword(monthlyFeeState.getVerifyPassword(), monthlyFeeState.getLoginUser().getPasswords())){
            if (monthlyFeeState.getPaidAmount()<= monthlyFeeState.getSelectForPayFee().getDue() && monthlyFeeState.getPaidAmount() > 0){
                if ("ADMIN".equals(monthlyFeeState.getLoginUser().getRoles().getUserTypes())){
                    String payStatus = "COMPLETED";
                    statusMessageModel = monthlyFeeService.payFee(monthlyFeeState.getSelectForPayFee(), monthlyFeeState.getPaidAmount(), payStatus);
                } else if ("USER".equals(monthlyFeeState.getLoginUser().getRoles().getUserTypes())) {
                    String payStatus = "PENDING";
                    statusMessageModel = monthlyFeeService.payFee(monthlyFeeState.getSelectForPayFee(), monthlyFeeState.getPaidAmount(), payStatus);
                }
                if (statusMessageModel.isStatus()){
                    monthlyFeeState.resetField();
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

    public void responsePayRequest(){
        if (PasswordUtils.verifyPassword(monthlyFeeState.getVerifyPassword(), monthlyFeeState.getLoginUser().getPasswords())){
            statusMessageModel = monthlyFeeService.responsePaymentRequest(monthlyFeeState.getSelectTransaction());
            if (statusMessageModel.isStatus()){
                monthlyFeeState.resetField();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
            }
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid Password."));
        }
    }

    public void viewStatementForStudent(Users student){
        this.monthlyFeeState.setSelectStudent(student);
        this.monthlyFeeState.setSelectStudentDueAmount(monthlyFeeDAO.getTotalDueAmount(student.getId()));
        this.monthlyFeeState.setStatementListEachStudent(transactionStatementDAOImp.getStatementByEachUser(student.getId()));
    }

    public MonthlyFeeState getMonthlyFeeState() {
        return monthlyFeeState;
    }
}