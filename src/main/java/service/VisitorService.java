package service;

import daoInterface.VisitorsDAO;
import views.stateModel.StatusMessageModel;
import model.Users;
import model.Visitors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
        Visitors newVisitor = new Visitors();
        newVisitor.setFullName(fullName);
        newVisitor.setReason(reason);
        newVisitor.setStudentId(student);
        newVisitor.setRelation(relation);

        Date date = new Date();
        Timestamp entryTime = new Timestamp(date.getTime());

        newVisitor.setEntryDatetime(entryTime);
        if (visitorsDAO.add(newVisitor)){
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("New Visitor Added Successfully");
        }else {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("Visitor Added Failed");
        }
        return statusMessageModel;
    }
}
