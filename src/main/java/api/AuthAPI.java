package api;

import daoImp.UserTypeDAOImp;
import daoInterface.RoomDAO;
import daoInterface.UsersDAO;
import dto.UsersDTO;
import dto.dtoMapper.DTOMapper;
import model.Rooms;
import model.UserType;
import model.Users;
import service.UserService;
import utils.JWTTokenNeeded;
import utils.JwtUtils;
import utils.PasswordUtils;
import utils.Role;
import views.stateModel.StatusMessageModel;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthAPI {
    @Inject
    private UsersDAO usersDAO;

    @Inject
    private UserTypeDAOImp userTypeDAOImp;

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    public Response userLogin(UsersDTO loginUser){
        Users user = usersDAO.getByEmail(loginUser.getEmail());
        if (user != null){
            if (PasswordUtils.verifyPassword(loginUser.getPasswords(),user.getPasswords())){
                UsersDTO loginUsers = DTOMapper.toUserDTO(user);
                loginUsers.setPasswords(null);
                String accessToken = JwtUtils.generateToken(user.getEmail(),user.getRoles().getUserTypes());
                String refreshToken = JwtUtils.generateRefreshToken(user.getEmail(), user.getRoles().getUserTypes());
                NewCookie refreshCookie = new NewCookie("refresh_token",
                        refreshToken,
                        "/",
                        null,
                        null,
                        60*60*24*7,
                        false,
                        true);
                NewCookie accessCookie = new NewCookie("access_token",
                        accessToken,
                        "/",
                        null,
                        null,
                        60*10,
                        false,
                        true);
                Map<String, Object> resp = new HashMap<>();
                resp.put("user",loginUsers);
                return Response.ok(resp).cookie(refreshCookie,accessCookie).build();
            }else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Invalid Password\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        }else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid Email\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @POST
    @Path("/logout")
    @JWTTokenNeeded
    public Response logout() {
        NewCookie expiredRefreshToken = new NewCookie("refresh_token", "", "/", null, null, 0, false, true);
        NewCookie expiredAccessToken = new NewCookie("access_token", "", "/", null, null, 0, false, true);

        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Successfully logged out");

        return Response.ok(resp)
                .cookie(expiredAccessToken, expiredRefreshToken)
                .build();
    }

    @POST
    @Path("/me")
    @JWTTokenNeeded
    public Response isMe(@CookieParam("access_token") String accessToken){
        if (accessToken != null){
            String email = JwtUtils.getUserEmail(accessToken);
            Users user = usersDAO.getByEmail(email);
            UsersDTO loginUsers = DTOMapper.toUserDTO(user);
            loginUsers.setPasswords(null);
            Map<String, Object> yesYou = new HashMap<>();
            yesYou.put("user", loginUsers);
            return Response.ok(yesYou).build();
        }else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Not You hehe\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @POST
    @Path("/register")
    @JWTTokenNeeded(allowed = Role.ADMIN)
    public Response registerNewUser(UsersDTO usersDTO){
        StatusMessageModel statusMessageModel = new StatusMessageModel();
        statusMessageModel = userService.registerNewStudent(DTOMapper.toRegisterUserEntity(usersDTO),
                DTOMapper.toRoomEntity(usersDTO.getRoom()));

        if (statusMessageModel.isStatus()){
            Map<String, Object> registerRes = new HashMap<>();
            registerRes.put("message",statusMessageModel.getMessage());
            return Response.ok(registerRes).build();
        }else {
            Map<String, Object> registerRes = new HashMap<>();
            registerRes.put("message",statusMessageModel.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(registerRes).build();
        }
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(@Context HttpHeaders headers) {
        Cookie refreshCookie = headers.getCookies().get("refresh_token");
        if (refreshCookie == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Refresh token missing\"}").build();
        }

        String refreshToken = refreshCookie.getValue();
        if (!JwtUtils.isTokenValid(refreshToken)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid refresh token\"}").build();
        }

        String email = JwtUtils.getUserEmail(refreshToken);
        Users user = usersDAO.getByEmail(email);

        String newAccessToken = JwtUtils.generateToken(user.getEmail(), user.getRoles().getUserTypes());
        NewCookie accessCookie = new NewCookie("access_token", newAccessToken, "/",
                null, null, 60*10, false, true);

        return Response.ok().cookie(accessCookie).build();
    }

}
