package views;

import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.Cards;
import org.primefaces.model.chart.DonutChartModel;
import views.stateModel.DashboardState;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class AdminDashboardBean implements Serializable {
    @Inject
    private UsersDAO usersDAO;

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private VisitorsDAO visitorsDAO;

    @Inject
    private RoomAllocationDAO roomAllocationDAO;

    private DashboardState dashboardState;

    private List<Cards> cards;
    private DonutChartModel roomOccupancyChart;
    private DonutChartModel userStatusChart;

    @PostConstruct
    public void init(){
        try{
            dashboardState = new DashboardState();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String token = SessionUtils.getToken(request);
            if (token == null){
                return;
            }
            dashboardState.setLoginUser(usersDAO.getByEmail(JwtUtils.getUserEmail(token)));
            if ("ADMIN".equals(dashboardState.getLoginUser().getRoles().getUserTypes())){
                dashboardState.setCountOnlyAllocated(roomAllocationDAO.getCountOnlyAllocated());
                dashboardState.setCountOnlyStudent(usersDAO.getCountOnlyStudent());
                cardModel();
                roomOccupancyChartModel();
                userStatusChartModel();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed ti init",e);
        }
    }

    public void cardModel(){
        cards = new ArrayList<>();
        cards.add(new Cards("Users", dashboardState.getCountOnlyStudent()));
        cards.add(new Cards("Rooms", roomDAO.getCount()));
        cards.add(new Cards("Allocated", dashboardState.getCountOnlyAllocated()));
        cards.add(new Cards("Visitor", visitorsDAO.getCount()));
    }

    public void roomOccupancyChartModel() {
        roomOccupancyChart = new DonutChartModel();
        Map<String, Number> circle1 = new LinkedHashMap<String, Number>();

        circle1.put("Occupied", dashboardState.getCountOnlyAllocated());
        circle1.put("Vacant", roomDAO.getTotalCapacity()- dashboardState.getCountOnlyAllocated());
        roomOccupancyChart.addCircle(circle1);

        roomOccupancyChart.setTitle("Room Occupancy Status");
        roomOccupancyChart.setLegendPosition("w");
    }
    public void userStatusChartModel() {
        userStatusChart = new DonutChartModel();
        Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
        Long countActiveStudent =usersDAO.getCountActiveStudent();
        circle1.put("Active", countActiveStudent);
        circle1.put("Inactive", dashboardState.getCountOnlyStudent() - countActiveStudent);
        userStatusChart.addCircle(circle1);
        userStatusChart.setTitle("Student Status");
        userStatusChart.setLegendPosition("w");
    }

    public DonutChartModel getRoomOccupancyChart() {
        return roomOccupancyChart;
    }

    public DonutChartModel getUserStatusChart() {
        return userStatusChart;
    }

    public List<Cards> getCards() {
        return cards;
    }

}
