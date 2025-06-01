package views;

import java.util.*;

import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.GenericLazyDataModel;
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
    private Map<String, Object> matchFilter;
    private Map<String, Object> matchFilterEachStudent;


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
            System.out.println("hero");
        }
    }

    public void refreshVisitorList(){
        visitorState.setLoginUser(usersDAO.getByEmail(JwtUtils.getUserEmail(SessionUtils.getToken(request))));
        if ("ADMIN".equals(visitorState.getLoginUser().getRoles().getUserTypes())){
            matchFilter = new HashMap<>();
            visitorState.setVisitorList(new GenericLazyDataModel<>(visitorsDAO,matchFilter,false));
        }
        if ("USER".equals(visitorState.getLoginUser().getRoles().getUserTypes())){
            matchFilterEachStudent = new HashMap<>();
            matchFilterEachStudent.put("studentId",visitorState.getLoginUser());
            visitorState.setViewVisitorByEachStudent(new GenericLazyDataModel<>(visitorsDAO,matchFilterEachStudent,false));
        }
        if (visitorState.getSelectStudent() != null){
            matchFilterEachStudent = new HashMap<>();
            matchFilterEachStudent.put("studentId",visitorState.getSelectStudent());
            visitorState.setViewVisitorByEachStudent(new GenericLazyDataModel<>(visitorsDAO,matchFilterEachStudent,false));
        }
    }

    public void addVisitor(){
        statusMessageModel = visitorService.addVisitor(visitorState.getFullName(),visitorState.getReason(), visitorState.getSelectStudent(), visitorState.getRelation());
        if (!statusMessageModel.isStatus()){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
            return;
        }
        refreshVisitorList();
        visitorState.resetFields();
        showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
    }

    public void exitVisitor(Visitors exitVisitor){
        if (!visitorService.exitVisitor(exitVisitor)){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Exit Visitor.");
            return;
        }
        showMessage(FacesMessage.SEVERITY_INFO, "Success", "Visitor Exit.");
    }

    private void showMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void viewStudentVisitor(Users student){
        matchFilterEachStudent = new HashMap<>();
        matchFilterEachStudent.put("studentId",student);
        visitorState.setViewVisitorByEachStudent(new GenericLazyDataModel<>(visitorsDAO,matchFilterEachStudent,false));
        visitorState.setSelectStudent(student);
    }

    public VisitorState getVisitorState() {
        return visitorState;
    }
}
