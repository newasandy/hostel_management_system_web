package views;

import daoInterface.LeaveRequestDAO;
import daoInterface.UsersDAO;
import model.LeaveRequest;
import views.stateModel.StatusMessageModel;
import model.Users;
import service.LeaveRequestService;
import utils.GetCookiesValues;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named("leaveRequestBean")
@ViewScoped
public class LeaveRequestBean implements Serializable {

    @Inject
    private LeaveRequestDAO leaveRequestDAO;

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private LeaveRequestService leaveRequestService;

    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    private List<LeaveRequest> leaveRequestList;
    private LeaveRequest selectLeaveRequest;

    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private Users loginUser;


    @PostConstruct
    public void init(){
        String email = GetCookiesValues.getEmailFromCookie();
        if (email != null) {
            loginUser = usersDAO.getByEmail(email);
        } else {
            System.out.println("Error: No email found in cookie.");
        }
        refreshLeaveRequestList();
    }

    public void refreshLeaveRequestList(){
        if ("USER".equals(GetCookiesValues.getUserRoleFromCookie())){
            leaveRequestList = leaveRequestDAO.getUserLeaveRequestByUserId(loginUser.getId());
        }
        if ("ADMIN".equals(GetCookiesValues.getUserRoleFromCookie())){
            leaveRequestList = leaveRequestDAO.getAll();
            Collections.sort(leaveRequestList, new Comparator<LeaveRequest>() {
                @Override
                public int compare(LeaveRequest v1, LeaveRequest v2) {
                    return v2.getApplyDate().compareTo(v1.getApplyDate());
                }
            });
        }
    }

    public void applyLeaveRequest(){
        LeaveRequest checkLeaveRequest = leaveRequestDAO.checkLeaveRequest(loginUser.getId());
        try{
            if (checkLeaveRequest == null){
                statusMessageModel = leaveRequestService.applyLeaveRequest(loginUser,reason,startDate,endDate);
                if (statusMessageModel.isStatus()){
                    refreshLeaveRequestList();
                    resetFields();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
                }else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
                }
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Leave Request already Applied"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Applied Leave Request"));
        }
    }

    @Transactional
    public void updateByAdmin(LeaveRequest.Status status){
        selectLeaveRequest.setStatus(status);
        if (leaveRequestDAO.update(selectLeaveRequest)){
            refreshLeaveRequestList();
            resetFields();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", status+" Leave Request"));
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Leave Request"));
        }
    }

    @Transactional
    public void updatePendingLeaveRequestByUser(){
        if (leaveRequestDAO.update(selectLeaveRequest)){
            refreshLeaveRequestList();
            resetFields();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Leave Request"));
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Leave Request"));
        }
    }

    public List<LeaveRequest> getLeaveRequestList() {
        return leaveRequestList;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LeaveRequest getSelectLeaveRequest() {
        return selectLeaveRequest;
    }

    public void setSelectLeaveRequest(LeaveRequest selectLeaveRequest) {
        this.selectLeaveRequest = selectLeaveRequest;
    }

    public LocalDate getMinDate() {
        return LocalDate.now();
    }

    public String getRowStyleClass(LeaveRequest leave) {
        if (leave == null || leave.getStatus() == null) {
            return "";
        }

        switch (leave.getStatus().toString()) {
            case "PENDING":
                return "pending-row";
            case "ACCEPTED":
                return "accepted-row";
            case "REJECTED":
                return "rejected-row";
            default:
                return "";
        }
    }

    public void resetFields(){
        this.reason ="";
        this.startDate = null;
        this.endDate = null;
    }
}
