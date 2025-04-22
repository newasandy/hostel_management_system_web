package api;

import daoInterface.UsersDAO;
import dto.UsersDTO;
import dto.dtoMapper.DTOMapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersAPI {
    @Inject
    private UsersDAO usersDAO;

    @GET
    @Path("/table")
    public Response getUserDetails(){
        List<UsersDTO> usersList = DTOMapper.userTableList(usersDAO.getOnlyStudent());
        return Response.ok(usersList).build();
    }
}
