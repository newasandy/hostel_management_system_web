package api;

import daoInterface.RoomDAO;
import dto.LazyLoadRequest;
import dto.RoomsDTO;
import dto.dtoMapper.DTOMapper;
import model.Rooms;
import org.primefaces.model.SortOrder;
import utils.JWTTokenNeeded;
import utils.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dto.dtoMapper.FilterMateMapper.buildFilterMetaMap;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomsAPI {

    @Inject
    private RoomDAO roomDAO;

    @POST
    @Path("/availableRoom")
    @JWTTokenNeeded(allowed = Role.ADMIN)
    public Response getAvailableRoom(LazyLoadRequest req){

        SortOrder sortOrder = SortOrder.ASCENDING;
        if (req.getSortOrder() != null) {
            sortOrder = "ASC".equalsIgnoreCase(req.getSortOrder())
                    ? SortOrder.ASCENDING
                    : SortOrder.DESCENDING;
        }
        List<Rooms> availableRoom = roomDAO.findPaginatedEntities(
                buildFilterMetaMap(req.getFilters()),
                req.getExactFilters(),
                req.getFirst(),
                req.getPageSize(),
                req.getSortField(),
                sortOrder,
                req.isUnAllocatedUser()
        );
        int count = roomDAO.getTotalEntityCount(
                buildFilterMetaMap(req.getFilters()),
                req.getExactFilters()
        );

        List<RoomsDTO> roomsDTOList = DTOMapper.roomTableList(availableRoom);
        Map<String, Object> res = new HashMap<>();
        res.put("available_room",roomsDTOList);
        res.put("count",count);
        return Response.ok(res).build();
    }

}
