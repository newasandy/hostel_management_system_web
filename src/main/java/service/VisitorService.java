package service;

import daoInterface.VisitorsDAO;
import model.StatusMessageModel;
import model.Users;
import model.Visitors;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Timestamp;
import java.util.Date;

@ApplicationScoped
public class VisitorService {
    private VisitorsDAO visitorsDAO;

    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    public VisitorService(VisitorsDAO visitorsDAO) {
        this.visitorsDAO = visitorsDAO;
    }

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
