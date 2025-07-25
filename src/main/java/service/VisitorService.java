package service;

import daoInterface.VisitorsDAO;
import views.stateModel.StatusMessageModel;
import model.Users;
import model.Visitors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;

@RequestScoped
@Transactional
public class VisitorService {

    @Inject
    private VisitorsDAO visitorsDAO;

    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    public StatusMessageModel addVisitor(String fullName, String reason, Users student, String relation){
        try{
            Visitors newVisitor = new Visitors();
            newVisitor.setFullName(fullName);
            newVisitor.setReason(reason);
            newVisitor.setStudentId(student);
            newVisitor.setRelation(relation);

            Date date = new Date();
            Timestamp entryTime = new Timestamp(date.getTime());

            newVisitor.setEntryDatetime(entryTime);
            if (!visitorsDAO.add(newVisitor)){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("Visitor Added Failed");
                return statusMessageModel;
            }
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("New Visitor Added Successfully");
        } catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while add new visitor.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public boolean exitVisitor(Visitors exitVisitor){
        try{
            Date date = new Date();
            Timestamp exitDate = new Timestamp(date.getTime());
            exitVisitor.setExitDatetime(exitDate);
            return visitorsDAO.update(exitVisitor);
        }catch (PersistenceException e){
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
