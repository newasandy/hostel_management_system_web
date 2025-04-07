package views.stateModel;

import model.LeaveRequest;
import model.Users;

import java.time.LocalDate;
import java.util.List;

public class LeaveRequestState {

    private List<LeaveRequest> leaveRequestList;
    private LeaveRequest selectLeaveRequest;

    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private Users loginUser;

    public LeaveRequestState() {
    }

    public LeaveRequestState(List<LeaveRequest> leaveRequestList, LeaveRequest selectLeaveRequest, String reason, LocalDate startDate, LocalDate endDate, Users loginUser) {
        this.leaveRequestList = leaveRequestList;
        this.selectLeaveRequest = selectLeaveRequest;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.loginUser = loginUser;
    }

    public List<LeaveRequest> getLeaveRequestList() {
        return leaveRequestList;
    }

    public void setLeaveRequestList(List<LeaveRequest> leaveRequestList) {
        this.leaveRequestList = leaveRequestList;
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
}
