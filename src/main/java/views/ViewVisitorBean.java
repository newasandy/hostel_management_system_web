package views;

import daoImp.VisitorsDAOImp;
import daoInterface.VisitorsDAO;
import model.StatusMessageModel;
import model.Visitors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("viewVisitorBean")
@ViewScoped
public class ViewVisitorBean implements Serializable {
    private VisitorsDAO visitorsDAO = new VisitorsDAOImp();
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    private List<Visitors> orginalVisitorList;
    private List<Visitors> visitorList;

    private String fullName;
    private String reason;


    @PostConstruct
    public void init(){
        orginalVisitorList = visitorsDAO.getAll();
        visitorList = new ArrayList<>(orginalVisitorList);
    }

    public List<Visitors> getVisitorList() {
        return visitorList;
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

    public void addVisitor(){

    }

    public void resetFields(){
        this.fullName = "";
        this.reason = "";
    }
}
