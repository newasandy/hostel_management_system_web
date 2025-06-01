package service;

import daoImp.AddressDAOImp;
import daoInterface.UsersDAO;
import model.*;
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

    public StatusMessageModel registerNewStudent(Users regUser, Rooms selectRoom){
        try{
            Users checkUser = usersDAO.getByEmail(regUser.getEmail());
            if(checkUser !=null){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("User Already Exist");
                return statusMessageModel;
            }

            if (!usersDAO.add(regUser)){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("User Register Unsuccessful");
                return statusMessageModel;
            }

            if (!roomsService.allocateStudentInRoom(regUser,selectRoom).isStatus()){
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("User Register but not room allocated");
                return statusMessageModel;
            }
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("User Register Successfully");

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
