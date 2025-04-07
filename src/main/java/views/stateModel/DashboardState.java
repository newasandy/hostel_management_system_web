package views.stateModel;

import model.LeaveRequest;
import model.RoomAllocation;
import model.Users;
import model.Visitors;

public class DashboardState {

    private Users loginUser ;

    private Visitors recentUserVisitor;
    private RoomAllocation recentRoom;
    private LeaveRequest recentLeaveRequest ;
    private Long countOnlyAllocated;
    private Long countOnlyStudent;

    public DashboardState() {
    }

    public Users getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Users loginUser) {
        this.loginUser = loginUser;
    }

    public Visitors getRecentUserVisitor() {
        return recentUserVisitor;
    }

    public void setRecentUserVisitor(Visitors recentUserVisitor) {
        this.recentUserVisitor = recentUserVisitor;
    }

    public RoomAllocation getRecentRoom() {
        return recentRoom;
    }

    public void setRecentRoom(RoomAllocation recentRoom) {
        this.recentRoom = recentRoom;
    }

    public LeaveRequest getRecentLeaveRequest() {
        return recentLeaveRequest;
    }

    public void setRecentLeaveRequest(LeaveRequest recentLeaveRequest) {
        this.recentLeaveRequest = recentLeaveRequest;
    }

    public Long getCountOnlyAllocated() {
        return countOnlyAllocated;
    }

    public void setCountOnlyAllocated(Long countOnlyAllocated) {
        this.countOnlyAllocated = countOnlyAllocated;
    }

    public Long getCountOnlyStudent() {
        return countOnlyStudent;
    }

    public void setCountOnlyStudent(Long countOnlyStudent) {
        this.countOnlyStudent = countOnlyStudent;
    }
}
