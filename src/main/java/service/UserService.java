package service;

import daoImp.AddressDAOImp;
import daoInterface.UsersDAO;
import model.Address;
import model.StatusMessageModel;
import model.UserType;
import model.Users;
import utils.PasswordUtils;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();


    private UsersDAO usersDAO;


    private AddressDAOImp addressDAOImp;

    public UserService(UsersDAO usersDAO, AddressDAOImp addressDAOImp) {
        this.usersDAO = usersDAO;
        this.addressDAOImp = addressDAOImp;
    }

    public StatusMessageModel registerNewStudent(String name, String email , String password, UserType role, String country, String district, String rmcMc, int wardNo){
        Users checkUser = usersDAO.getByEmail(email);
        if (checkUser == null){
            Users regUser = new Users();

            regUser.setFullName(name);
            regUser.setEmail(email);
            regUser.setPasswords(PasswordUtils.getHashPassword(password));
            regUser.setRoles(role);
            regUser.setStatus(true);

            Address regUserAddress = new Address();

            regUserAddress.setCountry(country);
            regUserAddress.setDistrict(district);
            regUserAddress.setRmcMc(rmcMc);
            regUserAddress.setWardNo(wardNo);

            if (usersDAO.add(regUser)){
                regUserAddress.setUser(regUser);
                if (addressDAOImp.add(regUserAddress)){
                    statusMessageModel.setStatus(true);
                    statusMessageModel.setMessage("User Register Successfully");
                }else {
                    statusMessageModel.setStatus(false);
                    statusMessageModel.setMessage("User Register but not register address");
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
