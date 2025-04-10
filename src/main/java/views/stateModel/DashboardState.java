package views.stateModel;

import model.LeaveRequest;
import model.RoomAllocation;
import model.Users;

public class DashboardState {

    private Users loginUser ;

    private RoomAllocation recentRoom;
    private LeaveRequest recentLeaveRequest ;
    private Long userVisitorCount;
    private Long countOnlyAllocated;
    private Long countOnlyStudent;

    public DashboardState() {
    }

    public Long getUserVisitorCount() {
        return userVisitorCount;
    }

    public void setUserVisitorCount(Long userVisitorCount) {
        this.userVisitorCount = userVisitorCount;
    }

    public Users getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Users loginUser) {
        this.loginUser = loginUser;
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
