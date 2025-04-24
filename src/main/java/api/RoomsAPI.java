package api;

import daoInterface.RoomDAO;
import dto.RoomsDTO;
import dto.dtoMapper.DTOMapper;
import model.Rooms;
import sun.java2d.pipe.RegionSpanIterator;
import utils.JWTTokenNeeded;
import utils.Role;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomsAPI {

    @Inject
    private RoomDAO roomDAO;

    @GET
    @Path("/availableRoom")
    @JWTTokenNeeded(allowed = Role.ADMIN)
    public Response getAvailableRoom(){
        List<Rooms> availableRoom = roomDAO.getAvailableRoom();
        List<RoomsDTO> roomsDTOList = DTOMapper.roomTableList(availableRoom);

        return Response.ok(roomsDTOList).build();
    }

}
