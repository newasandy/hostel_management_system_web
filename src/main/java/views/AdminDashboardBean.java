package views;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class AdminDashboardBean {

    private List<Card> cards;

    private int totalStudent = 20;
    private int totalRooms = 10;
    private int allocatedRooms = 5;
    private int totalVisitors = 50;

    public AdminDashboardBean(){
        cards = new ArrayList<>();
        cards.add(new Card("Users", totalStudent));
        cards.add(new Card("Rooms", totalRooms));
        cards.add(new Card("Allocated", allocatedRooms));
        cards.add(new Card("Visitor", totalVisitors));
    }

    public List<Card> getCards() {
        return cards;
    }
}
