package views.stateModel;

import model.Users;
import model.Visitors;

import java.util.List;

public class VisitorState {

    private GenericLazyDataModel<Visitors> visitorList;
    private String searchItem;
    private GenericLazyDataModel<Visitors> viewVisitorByEachStudent;

    private String fullName;
    private String reason;
    private Users selectStudent;
    private String relation;

    private Users loginUser;

    public VisitorState() {
    }

    public GenericLazyDataModel<Visitors> getVisitorList() {
        return visitorList;
    }

    public void setVisitorList(GenericLazyDataModel<Visitors> visitorList) {
        this.visitorList = visitorList;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public GenericLazyDataModel<Visitors> getViewVisitorByEachStudent() {
        return viewVisitorByEachStudent;
    }

    public void setViewVisitorByEachStudent(GenericLazyDataModel<Visitors> viewVisitorByEachStudent) {
        this.viewVisitorByEachStudent = viewVisitorByEachStudent;
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

    public Users getSelectStudent() {
        return selectStudent;
    }

    public void setSelectStudent(Users selectStudent) {
        this.selectStudent = selectStudent;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Users getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Users loginUser) {
        this.loginUser = loginUser;
    }

    public void resetFields(){
        this.fullName = "";
        this.reason = "";
        this.selectStudent = null;
        this.relation = "";
    }
}
