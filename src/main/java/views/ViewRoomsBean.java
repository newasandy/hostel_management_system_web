package views;

import daoImp.RoomAllocationDAOImp;
import daoImp.RoomDAOImp;
import daoImp.UserDAOImpl;
import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import daoInterface.UsersDAO;
import model.Rooms;
import model.StatusMessageModel;
import model.Users;
import service.RoomsService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ViewRoomsBean implements Serializable {

    private RoomDAO roomDAO = new RoomDAOImp();
    private UsersDAO usersDAO = new UserDAOImpl();
    private RoomAllocationDAO roomAllocationDAO = new RoomAllocationDAOImp();
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private final RoomsService roomsService = new RoomsService(roomDAO, roomAllocationDAO);

    private int roomNumber;
    private int capacity;

    private List<Rooms> orginalRoomsList;
    private List<Rooms> viewRoomsList;

    private List<Users> unallocatedUser;
    private List<Rooms> availableRoom;

    private Rooms selectRoom;
    private Users selectStudent;
    private boolean isEditMode;

    @PostConstruct
    public void init(){
        orginalRoomsList = roomDAO.getAll();
        viewRoomsList = new ArrayList<>(orginalRoomsList);
        unallocatedUser = usersDAO.getUnallocatedUsers();
        availableRoom = roomDAO.getAvailableRoom();
    }
    public void refreshRoomList(){
        orginalRoomsList = roomDAO.getAll();
        viewRoomsList = new ArrayList<>(orginalRoomsList);
        unallocatedUser = usersDAO.getUnallocatedUsers();
        availableRoom = roomDAO.getAvailableRoom();
    }

    public List<Rooms> getAvailableRoom() {
        return availableRoom;
    }

    public List<Users> getUnallocatedUser() {
        return unallocatedUser;
    }

    public Users getSelectStudent() {
        return selectStudent;
    }

    public void setSelectStudent(Users selectStudent) {
        this.selectStudent = selectStudent;
    }

    public Rooms getSelectRoom() {
        return selectRoom;
    }

    public void setSelectRoom(Rooms selectRoom) {
        this.selectRoom = selectRoom;
    }

    public boolean getIsEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public List<Rooms> getViewRoomsList() {
        return viewRoomsList;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void prepareEditRoom(Rooms room) {
        this.selectRoom = room;
    }

    public void prepareAddRoom() {
        resetFields();
        this.isEditMode = false;
    }

    public void saveOrUpdateRoom() {
        if (isEditMode) {
            updateRoom();
        } else {
            addNewRoom();
        }
        resetFields();
    }

    public void handleButtonAction(Users user) {
        if (selectStudent != null && selectStudent.equals(user)) {
            onCancel();
        } else {
            // Select the new student
            setSelectStudent(user);
        }
    }

    public void addNewRoom(){
        statusMessageModel = roomsService.addNewRoom(roomNumber,capacity);
        try{
            if (statusMessageModel.isStatus()){
                refreshRoomList();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
        }
    }

    public void updateRoom(){
        try{
            if (roomDAO.update(selectRoom)){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Room Successfully"));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
            }
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
        }
    }

    public void disableRoom(Rooms room){
        room.setStatus(false);
        try{
            if (roomDAO.update(room)){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Room Successfully"));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
            }
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
        }
    }

    public void enableRoom(Rooms room){
        room.setStatus(true);
        try{
            if (roomDAO.update(room)){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Room Successfully"));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
            }
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
        }
    }

    public void allocateStudentInARoom(){

    }

    public void onCancel(){
        this.selectStudent = null;
    }


    public void resetFields(){
        this.roomNumber = 1;
        this.capacity = 1;
    }
}
