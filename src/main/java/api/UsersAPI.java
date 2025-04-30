package api;

import daoImp.UserTypeDAOImp;
import daoInterface.UsersDAO;
import dto.LazyLoadRequest;
import dto.UserTypeDTO;
import dto.UsersDTO;
import dto.dtoMapper.DTOMapper;
import dto.dtoMapper.FilterMateMapper;
import model.Users;
import org.primefaces.model.SortOrder;
import service.UserService;
import utils.JWTTokenNeeded;
import utils.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersAPI {
    @Inject
    private UsersDAO usersDAO;

    @Inject
    private UserTypeDAOImp userTypeDAOImp;

    @Inject
    private UserService userService;

    @POST
    @Path("/table")
    @JWTTokenNeeded(allowed = Role.ADMIN)
    public Response getUserDetails(LazyLoadRequest req){

        SortOrder sortOrder = SortOrder.ASCENDING;
        if (req.getSortOrder() != null) {
            sortOrder = "ASC".equalsIgnoreCase(req.getSortOrder())
                    ? SortOrder.ASCENDING
                    : SortOrder.DESCENDING;
        }
        List<Users> page = usersDAO.findPaginatedEntities(
                FilterMateMapper.buildFilterMetaMap(req.getFilters()),

                req.getExactFilters(),
                req.getFirst(),
                req.getPageSize(),
                req.getSortField(),
                sortOrder,
                req.isUnAllocatedUser()
        );
        int count = usersDAO.getTotalEntityCount(
                FilterMateMapper.buildFilterMetaMap(req.getFilters()),
                req.getExactFilters()
        );
        List<UsersDTO> usersList = DTOMapper.userTableList(page);
        Map<String, Object> res = new HashMap<>();
        res.put("user_list",usersList);
        res.put("count",count);
        return Response.ok(res).build();
    }

    @GET
    @Path("/userType")
    @JWTTokenNeeded(allowed = Role.ADMIN)
    public Response getUserRole(){
        List<UserTypeDTO>userTypeList = DTOMapper.userTypeList(userTypeDAOImp.getAll());
        return Response.ok(userTypeList).build();
    }

    @POST
    @Path("/updateUser")
    @JWTTokenNeeded(allowed = Role.ADMIN)
    public Response updateUser(UsersDTO updateUserDetail){
        Users users = usersDAO.getById(updateUserDetail.getId());
        if (users == null){
            Map<String, Object> res = new HashMap<>();
            res.put("message","No user selected");
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        Users entityUser = DTOMapper.toUserEntity(updateUserDetail,users);

        if (userService.updateStudent(entityUser)){
            Map<String, Object> res = new HashMap<>();
            res.put("message","Update Success");
            return Response.ok(res).build();
        }else {
            Map<String, Object> res = new HashMap<>();
            res.put("message","User Not Update");
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

    }

}
