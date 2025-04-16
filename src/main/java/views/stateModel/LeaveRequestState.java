package views.stateModel;

import model.LeaveRequest;
import model.Users;

import java.time.LocalDate;

public class LeaveRequestState {

    private GenericLazyDataModel<LeaveRequest> leaveRequestsList;
    private LeaveRequest selectLeaveRequest;

    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private Users loginUser;

    public LeaveRequestState() {
    }


    public GenericLazyDataModel<LeaveRequest> getLeaveRequestsList() {
        return leaveRequestsList;
    }

    public void setLeaveRequestsList(GenericLazyDataModel<LeaveRequest> leaveRequestsList) {
        this.leaveRequestsList = leaveRequestsList;
    }

    public LeaveRequest getSelectLeaveRequest() {
        return selectLeaveRequest;
    }

    public void setSelectLeaveRequest(LeaveRequest selectLeaveRequest) {
        this.selectLeaveRequest = selectLeaveRequest;
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

    public Users getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Users loginUser) {
        this.loginUser = loginUser;
    }

    public void resetFields(){
        this.reason ="";
        this.startDate = null;
        this.endDate = null;
    }
}
