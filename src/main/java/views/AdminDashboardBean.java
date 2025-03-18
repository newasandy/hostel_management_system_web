package views;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AdminDashboardBean {
    private int totalStudent = 20;
    private int totalRooms = 10;
    private int allocatedRooms = 5;
    private int totalVisitors = 50;

    public int getTotalStudent() {
        return totalStudent;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public int getAllocatedRooms() {
        return allocatedRooms;
    }

    public int getTotalVisitors() {
        return totalVisitors;
    }
}
