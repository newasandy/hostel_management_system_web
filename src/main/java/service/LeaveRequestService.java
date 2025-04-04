package service;

import daoInterface.LeaveRequestDAO;
import model.LeaveRequest;
import views.stateModel.StatusMessageModel;
import model.Users;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@ApplicationScoped
public class LeaveRequestService {

    @Inject
    private LeaveRequestDAO leaveRequestDAO;

    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    @Transactional
    public StatusMessageModel applyLeaveRequest(Users student , String reason, LocalDate startFrom, LocalDate endOn){
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStudentId(student);
        leaveRequest.setReason(reason);
        leaveRequest.setStartFrom(startFrom);
        leaveRequest.setEndOn(endOn);
        Date date = new Date();
        Timestamp applyDate = new Timestamp(date.getTime());
        leaveRequest.setApplyDate(applyDate);
        leaveRequest.setStatus(LeaveRequest.Status.PENDING);
        if (leaveRequestDAO.add(leaveRequest)){
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("Leave Application is Submit Successfully");
        }else {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("!! Leave Application is not Submit");
        }
        return statusMessageModel;
    }
}
