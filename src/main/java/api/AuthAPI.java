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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
                Map<String , Object> response = new HashMap<>();
                String jwtToken = JwtUtils.generateToken(user.getEmail(),user.getRoles().getUserTypes());
                response.put("jwt_token", jwtToken);
                response.put("user", loginUsers);
                return Response.ok(response).build();
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

}
