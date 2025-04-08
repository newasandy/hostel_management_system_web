package views;

import daoInterface.LeaveRequestDAO;
import daoInterface.UsersDAO;
import model.LeaveRequest;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.LeaveRequestState;
import views.stateModel.StatusMessageModel;
import service.LeaveRequestService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named
@ViewScoped
public class LeaveRequestBean implements Serializable {

    @Inject
    private LeaveRequestDAO leaveRequestDAO;

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private LeaveRequestService leaveRequestService;

    private StatusMessageModel statusMessageModel;
    private LeaveRequestState leaveRequestState;

    private HttpServletRequest request;


    @PostConstruct
    public void init(){
        statusMessageModel = new StatusMessageModel();
        leaveRequestState = new LeaveRequestState();
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = JwtUtils.getUserEmail(SessionUtils.getToken(request));
        if (email != null) {
            leaveRequestState.setLoginUser(usersDAO.getByEmail(email));
        } else {
            System.out.println("Error: No email found in cookie.");
        }
        refreshLeaveRequestList();
    }

    public void refreshLeaveRequestList(){
        if ("USER".equals(JwtUtils.getUserRole(SessionUtils.getToken(request)))){
            leaveRequestState.setLeaveRequestList(leaveRequestDAO.getUserLeaveRequestByUserId(leaveRequestState.getLoginUser().getId()));
        }
        if ("ADMIN".equals(JwtUtils.getUserRole(SessionUtils.getToken(request)))){
            List<LeaveRequest> allLeaveRequest = leaveRequestDAO.getAll();
            Collections.sort(allLeaveRequest, new Comparator<LeaveRequest>() {
                @Override
                public int compare(LeaveRequest v1, LeaveRequest v2) {
                    return v2.getApplyDate().compareTo(v1.getApplyDate());
                }
            });
            leaveRequestState.setLeaveRequestList(allLeaveRequest);
        }
    }

    public void applyLeaveRequest(){
        LeaveRequest checkLeaveRequest = leaveRequestDAO.checkLeaveRequest(leaveRequestState.getLoginUser().getId());
        try{
            if (checkLeaveRequest == null){
                statusMessageModel = leaveRequestService.applyLeaveRequest(leaveRequestState.getLoginUser(),leaveRequestState.getReason(),leaveRequestState.getStartDate(),leaveRequestState.getEndDate());
                if (statusMessageModel.isStatus()){
                    refreshLeaveRequestList();
                    leaveRequestState.resetFields();
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
        leaveRequestState.getSelectLeaveRequest().setStatus(status);
        if (leaveRequestService.updateLeaveRequest(leaveRequestState.getSelectLeaveRequest())){
            refreshLeaveRequestList();
            leaveRequestState.resetFields();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", status+" Leave Request"));
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Leave Request"));
        }
    }

    public void updatePendingLeaveRequestByUser(){
        if (leaveRequestService.updateLeaveRequest(leaveRequestState.getSelectLeaveRequest())){
            refreshLeaveRequestList();
            leaveRequestState.resetFields();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Leave Request"));
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Leave Request"));
        }
    }

    public LeaveRequestState getLeaveRequestState() {
        return leaveRequestState;
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
}
