package views;

import daoImp.RoomAllocationDAOImp;
import daoImp.RoomDAOImp;
import daoImp.UserDAOImpl;
import daoImp.VisitorsDAOImp;
import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import org.primefaces.model.chart.DonutChartModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@RequestScoped
public class AdminDashboardBean implements Serializable {
    private UsersDAO usersDAO = new UserDAOImpl();
    private RoomDAO roomDAO = new RoomDAOImp();
    private VisitorsDAO visitorsDAO = new VisitorsDAOImp();
    private RoomAllocationDAO roomAllocationDAO = new RoomAllocationDAOImp();

    private List<Card> cards;
    private Long countOnlyAllocated;
    private Long countOnlyStudent;

    private DonutChartModel roomOccupancyChart;
    private DonutChartModel userStatusChart;



    @PostConstruct
    public void init(){
        countOnlyAllocated = roomAllocationDAO.getCountOnlyAllocated();
        countOnlyStudent = usersDAO.getCountOnlyStudent();
        cardModel();
        roomOccupancyChartModel();
        userStatusChartModel();
    }

    public void cardModel(){
        cards = new ArrayList<>();
        cards.add(new Card("Users", countOnlyStudent));
        cards.add(new Card("Rooms", roomDAO.getCount()));
        cards.add(new Card("Allocated", countOnlyAllocated));
        cards.add(new Card("Visitor", visitorsDAO.getCount()));
    }

    public void roomOccupancyChartModel() {
        roomOccupancyChart = new DonutChartModel();
        Map<String, Number> circle1 = new LinkedHashMap<String, Number>();

        circle1.put("Occupied", countOnlyAllocated);
        circle1.put("Vacant", roomDAO.getTotalCapacity()- countOnlyAllocated);
        roomOccupancyChart.addCircle(circle1);

        roomOccupancyChart.setTitle("Room Occupancy Status");
        roomOccupancyChart.setLegendPosition("w");
    }
    public void userStatusChartModel() {
        userStatusChart = new DonutChartModel();
        Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
        Long countActiveStudent =usersDAO.getCountActiveStudent();
        circle1.put("Active", countActiveStudent);
        circle1.put("Inactive", usersDAO.getCountOnlyStudent() - countActiveStudent);
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

    public List<Card> getCards() {
        return cards;
    }

    public static class Card {
        private String title;
        private Long value;

        public Card(String title, Long value) {
            this.title = title;
            this.value = value;
        }

        public String getTitle() { return title; }
        public Long getValue() { return value; }
    }
}
