package api;


import daoInterface.MonthlyFeeDAO;
import daoInterface.UsersDAO;
import dto.UserFee;
import model.MonthlyFee;
import model.Users;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/monthlyFee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeeAPI {

    @Inject
    private MonthlyFeeDAO monthlyFeeDAO;

    @Inject
    private UsersDAO usersDAO;

    @GET
    @Path("/user/{email}")
    public Response getMonthlyFees(@PathParam("email") String email){
        Users user = usersDAO.getByEmail(email);
        if (user != null && user.isStatus()){
            List<MonthlyFee> monthlyFeeList = monthlyFeeDAO.getUserFeeDetails(user.getId());

            UserFee userFee = new UserFee(user,monthlyFeeList);
            return Response.ok(userFee).build();
        }else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found or inactive");
            errorResponse.put("email", email);

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorResponse)
                    .build();
        }
    }
}
