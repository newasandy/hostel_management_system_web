package service;

import daoImp.AddressDAOImp;
import daoInterface.RoomAllocationDAO;
import daoInterface.UsersDAO;
import model.*;
import utils.PasswordUtils;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Timestamp;
import java.util.Date;

@ApplicationScoped
public class UserService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();


    private UsersDAO usersDAO;

    private AddressDAOImp addressDAOImp;

    private RoomAllocationDAO roomAllocationDAO;

    public UserService(UsersDAO usersDAO, AddressDAOImp addressDAOImp, RoomAllocationDAO roomAllocationDAO) {
        this.usersDAO = usersDAO;
        this.addressDAOImp = addressDAOImp;
        this.roomAllocationDAO = roomAllocationDAO;
    }

    public StatusMessageModel registerNewStudent(String name, String email , String password, UserType role, String country, String district, String rmcMc, int wardNo, Rooms selectRoom){
        Users checkUser = usersDAO.getByEmail(email);
        if (checkUser == null){
            Users regUser = new Users();

            regUser.setFullName(name);
            regUser.setEmail(email);
            regUser.setPasswords(PasswordUtils.getHashPassword(password));
            regUser.setRoles(role);
            regUser.setStatus(true);

            RoomAllocation roomAllocation = new RoomAllocation();
            roomAllocation.setRoomId(selectRoom);
            Date date = new Date();
            Timestamp allocatedDate = new Timestamp(date.getTime());
            roomAllocation.setAllocationDate(allocatedDate);

            Address regUserAddress = new Address();

            regUserAddress.setCountry(country);
            regUserAddress.setDistrict(district);
            regUserAddress.setRmcMc(rmcMc);
            regUserAddress.setWardNo(wardNo);

            if (usersDAO.add(regUser)){
                regUserAddress.setUser(regUser);
                roomAllocation.setStudentId(regUser);
                if (addressDAOImp.add(regUserAddress) && roomAllocationDAO.add(roomAllocation)){
                    statusMessageModel.setStatus(true);
                    statusMessageModel.setMessage("User Register Successfully");
                }else {
                    statusMessageModel.setStatus(false);
                    statusMessageModel.setMessage("User Register but not register address and room");
                }
            }else {
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("User Register Unsuccessful");
            }
        }else {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("User Already Exist");
        }
        return statusMessageModel;
    }
}
