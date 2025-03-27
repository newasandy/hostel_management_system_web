package daoInterface;

import model.LeaveRequest;

import java.util.List;

public interface LeaveRequestDAO extends BaseDAO<LeaveRequest>{

    List<LeaveRequest> getUserLeaveRequestByUserId(Long userId);
    LeaveRequest checkLeaveRequest(Long userId);
    LeaveRequest getRecentLeaveRequest(Long userId);
}
