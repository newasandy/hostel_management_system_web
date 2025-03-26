package views;

import daoImp.LeaveRequestDAOImp;
import daoImp.UserDAOImpl;
import daoInterface.LeaveRequestDAO;
import daoInterface.UsersDAO;
import model.LeaveRequest;
import model.StatusMessageModel;
import model.Users;
import service.LeaveRequestService;
import utils.GetCookiesValues;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named("leaveRequestBean")
@ViewScoped
public class LeaveRequestBean implements Serializable {
    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAOImp();
    private UsersDAO usersDAO = new UserDAOImpl();
    private LeaveRequestService leaveRequestService = new LeaveRequestService(leaveRequestDAO);
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    private List<LeaveRequest> leaveRequestList;
    private List<LeaveRequest> userLeaveRequestList;
    private LeaveRequest selectLeaveRequest;

    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private Users loginUser = usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie());
    @PostConstruct
    public void init(){
        refreshLeaveRequestList();
    }

    public void refreshLeaveRequestList(){
        leaveRequestList = leaveRequestDAO.getAll();
        Collections.sort(leaveRequestList, new Comparator<LeaveRequest>() {
            @Override
            public int compare(LeaveRequest v1, LeaveRequest v2) {
                return v2.getApplyDate().compareTo(v1.getApplyDate());
            }
        });
        if ("USER".equals(GetCookiesValues.getUserRoleFromCookie())){
            userLeaveRequestList = leaveRequestDAO.getUserLeaveRequestByUserId(loginUser.getId());
        }
    }

    public List<LeaveRequest> getLeaveRequestList() {
        return leaveRequestList;
    }

    public List<LeaveRequest> getUserLeaveRequestList() {
        return userLeaveRequestList;
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
        return LocalDate.now();  // Disables past dates
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

    public void resetFields(){
        this.reason ="";
        this.startDate = null;
        this.endDate = null;
    }
}
