package views;

import daoInterface.LeaveRequestDAO;
import daoInterface.RoomAllocationDAO;
import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.DashboardState;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@ViewScoped
public class UserDashboardBean implements Serializable {

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
        try{
            dashboardState = new DashboardState();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String token = SessionUtils.getToken(request);
            if (token == null){
                return;
            }

            String email = JwtUtils.getUserEmail(token);
            if (email == null){
                return;
            }

            dashboardState.setLoginUser(usersDAO.getByEmail(email));
            dashboardState.setUserVisitorCount(visitorsDAO.userTotalVisitor(dashboardState.getLoginUser().getId()));
            dashboardState.setRecentRoom(roomAllocationDAO.getRecentUserRoomAllocation(dashboardState.getLoginUser().getId()));
            dashboardState.setRecentLeaveRequest(leaveRequestDAO.getRecentLeaveRequest(dashboardState.getLoginUser().getId()));
        } catch (Exception e) {
            throw new RuntimeException("Failed ti init",e);
        }
    }

    public DashboardState getDashboardState() {
        return dashboardState;
    }
}
