package views;

import java.util.Collections;
import java.util.Comparator;

import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.StatusMessageModel;
import model.Users;
import model.Visitors;
import service.VisitorService;
import views.stateModel.VisitorState;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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
    private HttpServletRequest request;

    @PostConstruct
    public void init(){
        try{
            statusMessageModel = new StatusMessageModel();
            visitorState = new VisitorState();
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            refreshVisitorList();
        } catch (Exception e) {
            throw new RuntimeException("Failed ti init",e);
        }

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
        visitorState.setLoginUser(usersDAO.getByEmail(JwtUtils.getUserEmail(SessionUtils.getToken(request))));
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
            showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
        }else {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
        }
    }

    public void exitVisitor(Visitors exitVisitor){
        if (visitorService.exitVisitor(exitVisitor)){
            showMessage(FacesMessage.SEVERITY_INFO, "Success", "Visitor Exit.");
        }else {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Exit Visitor.");
        }
    }

    private void showMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void viewStudentVisitor(Users student){
        visitorState.setSelectStudent(student);
        visitorState.setViewVisitorByEachStudent(visitorsDAO.getUserVisitedBy(student.getId()));
    }

    public VisitorState getVisitorState() {
        return visitorState;
    }
}
