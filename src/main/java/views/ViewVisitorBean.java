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

@Named
@ViewScoped
public class ViewVisitorBean implements Serializable {
    private VisitorsDAO visitorsDAO = new VisitorsDAOImp();
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    private List<Visitors> orginalVisitorList;
    private List<Visitors> visitorList;

    @PostConstruct
    public void init(){
        orginalVisitorList = visitorsDAO.getAll();
        visitorList = new ArrayList<>(orginalVisitorList);
    }

    public List<Visitors> getVisitorList() {
        return visitorList;
    }
}
