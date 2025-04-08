package views;

import daoInterface.LeaveRequestDAO;
import daoInterface.RoomAllocationDAO;
import daoInterface.UsersDAO;
import daoInterface.VisitorsDAO;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.DashboardState;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (SessionUtils.isSessionValid(request) && JwtUtils.isTokenValid(SessionUtils.getToken(request))){
            dashboardState.setLoginUser(usersDAO.getByEmail(JwtUtils.getUserEmail(SessionUtils.getToken(request))));
            dashboardState.setRecentUserVisitor(visitorsDAO.getRecentUserVisitor(dashboardState.getLoginUser().getId()));
            dashboardState.setRecentRoom(roomAllocationDAO.getRecentUserRoomAllocation(dashboardState.getLoginUser().getId()));
            dashboardState.setRecentLeaveRequest(leaveRequestDAO.getRecentLeaveRequest(dashboardState.getLoginUser().getId()));
        }else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext != null) {
                try {
                    facesContext.getExternalContext().redirect("index.xhtml?expired=true");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public DashboardState getDashboardState() {
        return dashboardState;
    }
}
