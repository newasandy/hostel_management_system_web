package service;

import daoInterface.LeaveRequestDAO;
import model.LeaveRequest;
import views.stateModel.StatusMessageModel;
import model.Users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@RequestScoped
@Transactional
public class LeaveRequestService {

    @Inject
    private LeaveRequestDAO leaveRequestDAO;

    public StatusMessageModel applyLeaveRequest(Users student , String reason, LocalDate startFrom, LocalDate endOn){
        StatusMessageModel statusMessageModel = new StatusMessageModel();
        try{
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
        }catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while apply leave request.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public boolean updateLeaveRequest(LeaveRequest selectedLeaveRequest){
        try{
            return leaveRequestDAO.update(selectedLeaveRequest);
        }catch (PersistenceException e){
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
