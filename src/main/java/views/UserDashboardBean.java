package views;

import daoImp.LeaveRequestDAOImp;
import daoImp.RoomAllocationDAOImp;
import daoImp.UserDAOImpl;
import daoImp.VisitorsDAOImp;
import daoInterface.LeaveRequestDAO;
import daoInterface.RoomAllocationDAO;
import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import model.LeaveRequest;
import model.RoomAllocation;
import model.Users;
import model.Visitors;
import utils.GetCookiesValues;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class UserDashboardBean {

    private UsersDAO usersDAO = new UserDAOImpl();
    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAOImp();
    private VisitorsDAO visitorsDAO = new VisitorsDAOImp();
    private RoomAllocationDAO roomAllocationDAO = new RoomAllocationDAOImp();

    private Users loginUser =usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie());

    private LeaveRequest recentLeaveRequest = leaveRequestDAO.getRecentLeaveRequest(loginUser.getId());
    private Visitors recentUserVisitor = visitorsDAO.getRecentUserVisitor(loginUser.getId());
    private RoomAllocation recentRoom = roomAllocationDAO.getRecentUserRoomAllocation(loginUser.getId());


    public Users getLoginUser() {
        return loginUser;
    }
    public LeaveRequest getRecentLeaveRequest() {
        return recentLeaveRequest;
    }
    public Visitors getRecentUserVisitor() {
        return recentUserVisitor;
    }

    public RoomAllocation getRecentRoom() {
        return recentRoom;
    }
}
