package views;

import daoInterface.LeaveRequestDAO;
import daoInterface.RoomAllocationDAO;
import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import model.LeaveRequest;
import model.RoomAllocation;
import model.Users;
import model.Visitors;
import utils.GetCookiesValues;
import views.stateModel.DashboardState;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserDashboardBean {

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private LeaveRequestDAO leaveRequestDAO;

    @Inject
    private VisitorsDAO visitorsDAO;

    @Inject
    private RoomAllocationDAO roomAllocationDAO;

    private DashboardState dashboardState;


    @PostConstruct
    public void init(){
        dashboardState = new DashboardState();
        dashboardState.setLoginUser(usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie()));
        if (!GetCookiesValues.getEmailFromCookie().isEmpty()){
            dashboardState.setRecentUserVisitor(visitorsDAO.getRecentUserVisitor(dashboardState.getLoginUser().getId()));
            dashboardState.setRecentRoom(roomAllocationDAO.getRecentUserRoomAllocation(dashboardState.getLoginUser().getId()));
            dashboardState.setRecentLeaveRequest(leaveRequestDAO.getRecentLeaveRequest(dashboardState.getLoginUser().getId()));
        }
    }

    public DashboardState getDashboardState() {
        return dashboardState;
    }
}
