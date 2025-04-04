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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    @Inject
    private VisitorService visitorService;

    private List<Visitors> orginalVisitorList;
    private List<Visitors> visitorList;
    private String searchItem;
    private List<Visitors> viewVisitorByEachStudent;

    private String fullName;
    private String reason;
    private Users selectStudent;
    private String relation;


    @PostConstruct
    public void init(){
        refreshVisitorList();
    }

    public void searchList(){
        if (searchItem == null || searchItem.isEmpty()){
            refreshVisitorList();
        }else {
            String lowerSearch = searchItem.toLowerCase();
            visitorList = orginalVisitorList.stream().filter(visitors -> visitors.getFullName().toLowerCase().contains(lowerSearch)).collect(Collectors.toList());
        }
    }

    public void refreshVisitorList(){
        orginalVisitorList = visitorsDAO.getAll();
        Collections.sort(orginalVisitorList, new Comparator<Visitors>() {
            @Override
            public int compare(Visitors v1, Visitors v2) {
                return v2.getEntryDatetime().compareTo(v1.getEntryDatetime());
            }
        });
        visitorList = new ArrayList<>(orginalVisitorList);
        if ("USER".equals(GetCookiesValues.getUserRoleFromCookie())){
            Users loginUser = usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie());
            viewVisitorByEachStudent = visitorsDAO.getUserVisitedBy(loginUser.getId());
        }
        if (selectStudent != null){
            viewVisitorByEachStudent = visitorsDAO.getUserVisitedBy(selectStudent.getId());
        }
    }

    public void addVisitor(){
        statusMessageModel = visitorService.addVisitor(fullName,reason,selectStudent,relation);
        if (statusMessageModel.isStatus()){
            refreshVisitorList();
            resetFields();
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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Users getSelectStudent() {
        return selectStudent;
    }

    public void setSelectStudent(Users selectStudent) {
        this.selectStudent = selectStudent;
    }

    public List<Visitors> getViewVisitorByEachStudent() {
        return viewVisitorByEachStudent;
    }

    public void setViewVisitorByEachStudent(List<Visitors> viewVisitorByEachStudent) {
        this.viewVisitorByEachStudent = viewVisitorByEachStudent;
    }

    public List<Visitors> getVisitorList() {
        return visitorList;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public void viewStudentVisitor(Users student){
        selectStudent = student;
        viewVisitorByEachStudent = visitorsDAO.getUserVisitedBy(student.getId());
    }

    public void refreshVisitorByEachStudent(){
        viewVisitorByEachStudent = visitorsDAO.getUserVisitedBy(selectStudent.getId());
    }

    public void resetFields(){
        this.fullName = "";
        this.reason = "";
        this.selectStudent = null;
        this.relation = "";
    }
}
