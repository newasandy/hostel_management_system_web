package api;

import daoInterface.UsersDAO;
import dto.UsersDTO;
import dto.dtoMapper.DTOMapper;
import model.Users;
import utils.JwtUtils;
import utils.PasswordUtils;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthAPI {
    @Inject
    private UsersDAO usersDAO;

    @POST
    @Path("/login")
    public Response userLogin(UsersDTO loginUser){
        Users user = usersDAO.getByEmail(loginUser.getEmail());
        if (user != null){
            if (PasswordUtils.verifyPassword(loginUser.getPasswords(),user.getPasswords())){
                UsersDTO loginUsers = DTOMapper.toUserDTO(user);
                loginUsers.setPasswords(null);
                String accessToken = JwtUtils.generateToken(user.getEmail(),user.getRoles().getUserTypes());
                String refreshToken = JwtUtils.generateRefreshToken(user.getEmail());
                NewCookie refreshCookie = new NewCookie("refresh_token",refreshToken,"/",null,null,60*10,false,true);
                NewCookie accessCookie = new NewCookie("access_token",accessToken,"/",null,null,60*60*24*7,false,true);
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
                null, null, 60*60*24*7, false, true);

        return Response.ok().cookie(accessCookie).build();
    }

}
