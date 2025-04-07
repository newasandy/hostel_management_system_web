package views;

import java.util.Collections;
import java.util.Comparator;

import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import views.stateModel.StatusMessageModel;
import model.Users;
import model.Visitors;
import service.VisitorService;
import utils.GetCookiesValues;
import views.stateModel.VisitorState;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ViewVisitorBean implements Serializable {

    @Inject
    private VisitorsDAO visitorsDAO;

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private VisitorService visitorService;

    private StatusMessageModel statusMessageModel;
    private VisitorState visitorState;

    @PostConstruct
    public void init(){
        statusMessageModel = new StatusMessageModel();
        visitorState = new VisitorState();
        refreshVisitorList();
    }

    public void searchList(){
        if (visitorState.getSearchItem() == null || visitorState.getSearchItem().isEmpty()){
            refreshVisitorList();
        }else {
            List<Visitors> orginalVisitorList = visitorsDAO.getAll();
            String lowerSearch = visitorState.getSearchItem().toLowerCase();
            visitorState.setVisitorList(orginalVisitorList.stream().filter(visitors -> visitors.getFullName().toLowerCase().contains(lowerSearch)).collect(Collectors.toList()));
        }
    }

    public void refreshVisitorList(){
        visitorState.setLoginUser(usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie()));
        if ("ADMIN".equals(visitorState.getLoginUser().getRoles().getUserTypes())){
            List<Visitors> orginalVisitorList = visitorsDAO.getAll();
            Collections.sort(orginalVisitorList, new Comparator<Visitors>() {
                @Override
                public int compare(Visitors v1, Visitors v2) {
                    return v2.getEntryDatetime().compareTo(v1.getEntryDatetime());
                }
            });
            visitorState.setVisitorList(orginalVisitorList);
        }
        if ("USER".equals(visitorState.getLoginUser().getRoles().getUserTypes())){
            visitorState.setViewVisitorByEachStudent(visitorsDAO.getUserVisitedBy(visitorState.getLoginUser().getId()));
        }
        if (visitorState.getSelectStudent() != null){
            visitorState.setViewVisitorByEachStudent(visitorsDAO.getUserVisitedBy(visitorState.getSelectStudent().getId()));
        }
    }

    public void addVisitor(){
        statusMessageModel = visitorService.addVisitor(visitorState.getFullName(),visitorState.getReason(), visitorState.getSelectStudent(), visitorState.getRelation());
        if (statusMessageModel.isStatus()){
            refreshVisitorList();
            visitorState.resetFields();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
        }
    }

    @Transactional
    public void exitVisitor(Visitors exitVisitor){
        Date date = new Date();
        Timestamp exitDate = new Timestamp(date.getTime());
        exitVisitor.setExitDatetime(exitDate);
        if (visitorsDAO.update(exitVisitor)){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Visitor Exit."));
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Exit Visitor."));
        }
    }

    public void viewStudentVisitor(Users student){
        visitorState.setSelectStudent(student);
        visitorState.setViewVisitorByEachStudent(visitorsDAO.getUserVisitedBy(student.getId()));
    }

    public VisitorState getVisitorState() {
        return visitorState;
    }
}
