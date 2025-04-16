package views;

import daoInterface.MonthlyFeeDAO;
import daoInterface.TransactionStatementDAO;
import daoInterface.UsersDAO;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.*;
import model.Users;
import service.MonthlyFeeService;
import utils.PasswordUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

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
    private Map<String, Object> matchFilter;
    private Map<String, Object> matchFilterAll;

    private WalletUserState walletUserState;
    private WalletUserState loginWalletUser;
    private WalletPaymentState walletPaymentState;
    private static String jsessionId;

    @PostConstruct
    public void init(){
        try{
            statusMessageModel = new StatusMessageModel();
            monthlyFeeState = new MonthlyFeeState();
            walletUserState = new WalletUserState();
            walletPaymentState = new WalletPaymentState();

            refreshMonthlyFeeList();
        } catch (Exception e) {
            throw new RuntimeException("Failed ti init",e);
        }
    }

    public void refreshMonthlyFeeList(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        monthlyFeeState.setLoginUser(usersDAO.getByEmail(JwtUtils.getUserEmail(SessionUtils.getToken(request))));
        if ("USER".equals(monthlyFeeState.getLoginUser().getRoles().getUserTypes())){
            matchFilter = new HashMap<>();
            matchFilter.put("studentId",monthlyFeeState.getLoginUser());
            monthlyFeeState.setMonthlyFeeList(new GenericLazyDataModel<>(monthlyFeeDAO,matchFilter, false));
            monthlyFeeState.setSelectStudentDueAmount(monthlyFeeDAO.getTotalDueAmount(monthlyFeeState.getLoginUser().getId()));
            monthlyFeeState.setStatementListEachStudent(new GenericLazyDataModel<>(transactionStatementDAOImp,matchFilter, false));
        }
        if ("ADMIN".equals(monthlyFeeState.getLoginUser().getRoles().getUserTypes())){
            matchFilterAll = new HashMap<>();
            monthlyFeeState.setMonthlyFeeList(new GenericLazyDataModel<>(monthlyFeeDAO,matchFilter, false));
            matchFilter = new HashMap<>();
            matchFilter.put("status","PENDING");
            monthlyFeeState.setPendingPaymentRequest(new GenericLazyDataModel<>(transactionStatementDAOImp,matchFilter, false));
        }
        if (monthlyFeeState.getSelectStudent() != null){
            matchFilter = new HashMap<>();
            matchFilter.put("studentId", monthlyFeeState.getLoginUser());
            monthlyFeeState.setStatementListEachStudent(new GenericLazyDataModel<>(transactionStatementDAOImp,matchFilter, false));
        }
    }

    public void assignMonthlyFee(){
        try{
            statusMessageModel = monthlyFeeService.assignStudentMonthlyFee(monthlyFeeState.getSelectStudent(), monthlyFeeState.getAssignFeeAmount());
            if (statusMessageModel.isStatus()){
                monthlyFeeState.resetField();
                showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
            }
        }catch (Exception e){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to Assign Monthly Fee.");
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
                    showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
                }else {
                    showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
                }
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Paid Amount is Invalid");
            }
        }else {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid Password.");
        }
    }

    public void responsePayRequest(){
        if (PasswordUtils.verifyPassword(monthlyFeeState.getVerifyPassword(), monthlyFeeState.getLoginUser().getPasswords())){
            statusMessageModel = monthlyFeeService.responsePaymentRequest(monthlyFeeState.getSelectTransaction());
            if (statusMessageModel.isStatus()){
                monthlyFeeState.resetField();
                showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
            }
        }
        else {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid Password.");
        }
    }

    public void viewStatementForStudent(Users student){
        this.monthlyFeeState.setSelectStudent(student);
        this.monthlyFeeState.setSelectStudentDueAmount(monthlyFeeDAO.getTotalDueAmount(student.getId()));
        matchFilter = new HashMap<>();
        matchFilter.put("studentId",student);
        this.monthlyFeeState.setStatementListEachStudent(new GenericLazyDataModel<>(transactionStatementDAOImp,matchFilter, false));
    }

    public void walletUserConfirm(){
        try{
            Client client = ClientBuilder.newClient();

            Response authResponse = client
                    .target("http://172.7.0.15:8080/digital_wallet_web_V2/api/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(walletUserState, MediaType.APPLICATION_JSON));
            if (authResponse.getStatus() == 200){
                Map<String, NewCookie> cookies = authResponse.getCookies();
                NewCookie sessionCookie = cookies.get("JSESSIONID");
                if (sessionCookie != null) {
                    jsessionId = sessionCookie.getValue();
                    System.out.println("JSESSIONID: " + jsessionId);
                }
                loginWalletUser = authResponse.readEntity(WalletUserState.class);
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR,"Error","Invalid User");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void payWithWallet(){
        System.out.println(monthlyFeeState.getPaidAmount());
        try{
            if (monthlyFeeState.getPaidAmount()<= monthlyFeeState.getSelectForPayFee().getDue() && monthlyFeeState.getPaidAmount() > 0){
                walletPaymentState.setUsername(loginWalletUser.getUsername());
                walletPaymentState.setBalance(monthlyFeeState.getPaidAmount());
                walletPaymentState.setUsernameOrContact("Sandeep");

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target("http://172.7.0.15:8080/digital_wallet_web_V2/api/transaction/payment");
                Invocation.Builder builder = target
                        .request(MediaType.APPLICATION_JSON)
                        .cookie("JSESSIONID", jsessionId);

                Response paymentResponse = builder
                        .post(Entity.entity(walletPaymentState, MediaType.APPLICATION_JSON));
                if (paymentResponse.getStatus() == 200){
                    String payStatus = "COMPLETED";
                    statusMessageModel = monthlyFeeService.payFee(monthlyFeeState.getSelectForPayFee(), walletPaymentState.getBalance(), payStatus);
                    if (statusMessageModel.isStatus()){
                        monthlyFeeState.resetField();
                        showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
                    }else {
                        showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
                    }
                }else {
                    showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Payment Error");
                }
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Paid Amount is Invalid");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public WalletUserState getLoginWalletUser() {
        return loginWalletUser;
    }

    public WalletUserState getWalletUserState() {
        return walletUserState;
    }

    public MonthlyFeeState getMonthlyFeeState() {
        return monthlyFeeState;
    }
}