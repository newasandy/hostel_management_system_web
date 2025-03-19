package views;

import daoImp.RoomAllocationDAOImp;
import daoImp.RoomDAOImp;
import daoImp.UserDAOImpl;
import daoImp.VisitorsDAOImp;
import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class AdminDashboardBean implements Serializable {
    private UsersDAO usersDAO = new UserDAOImpl();
    private RoomDAO roomDAO = new RoomDAOImp();
    private VisitorsDAO visitorsDAO = new VisitorsDAOImp();
    private RoomAllocationDAO roomAllocationDAO = new RoomAllocationDAOImp();

    private List<Card> cards;


    @PostConstruct
    public void init(){
        cards = new ArrayList<>();
        cards.add(new Card("Users", usersDAO.getCount()));
        cards.add(new Card("Rooms", roomDAO.getCount()));
        cards.add(new Card("Allocated", roomAllocationDAO.getCountOnlyAllocated()));
        cards.add(new Card("Visitor", visitorsDAO.getCount()));
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
