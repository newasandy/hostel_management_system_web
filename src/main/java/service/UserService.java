package service;

import daoImp.AddressDAOImp;
import daoInterface.UsersDAO;
import model.*;
import utils.PasswordUtils;
import views.stateModel.StatusMessageModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class UserService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private AddressDAOImp addressDAOImp;

    @Inject
    private RoomsService roomsService;

    public StatusMessageModel registerNewStudent(String name, String email , String password, UserType role, String country, String district, String rmcMc, int wardNo, Rooms selectRoom){
        try{
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
                regUserAddress.setUser(regUser);

                regUser.setAddress(regUserAddress);

                if (usersDAO.add(regUser)){
                    if (roomsService.allocateStudentInRoom(regUser,selectRoom).isStatus()){
                        statusMessageModel.setStatus(true);
                        statusMessageModel.setMessage("User Register Successfully");
                    }else {
                        statusMessageModel.setStatus(false);
                        statusMessageModel.setMessage("User Register but not room allocated");
                    }
                }else {
                    statusMessageModel.setStatus(false);
                    statusMessageModel.setMessage("User Register Unsuccessful");
                }
            }else {
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("User Already Exist");
            }
        } catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while register new student.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public boolean updateStudent(Users selectStudent){
        try{
            if (!selectStudent.isStatus()){
                roomsService.unallocatedInactiveStudent(selectStudent);
                return usersDAO.update(selectStudent);
            }
            return usersDAO.update(selectStudent);
        }catch (PersistenceException e){
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
