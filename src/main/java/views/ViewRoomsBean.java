package views;

import daoImp.RoomDAOImp;
import daoInterface.RoomDAO;
import model.Rooms;
import model.StatusMessageModel;
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
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private final RoomsService roomsService = new RoomsService(roomDAO);

    private int roomNumber;
    private int capacity;

    private List<Rooms> orginalRoomsList;
    private List<Rooms> viewRoomsList;

    private Rooms selectRoom;
    private boolean isEditMode = false;

    @PostConstruct
    public void init(){
        orginalRoomsList = roomDAO.getAll();
        viewRoomsList = new ArrayList<>(orginalRoomsList);
    }
    public void refreshRoomList(){
        orginalRoomsList = roomDAO.getAll();
        viewRoomsList = new ArrayList<>(orginalRoomsList);
    }

    public void prepareEditRoom(Rooms room) {
        this.selectRoom = room;
        this.isEditMode = true;
    }

    public void prepareAddRoom() {
        resetFields();
        this.isEditMode = false;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
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

    public void saveOrUpdateRoom() {
        if (isEditMode) {
            updateRoom(); // Call the update logic
        } else {
            addNewRoom(); // Call the add logic
        }
        resetFields(); // Reset the form after the operation
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


    public void resetFields(){
        this.roomNumber = 0;
        this.capacity = 0;
    }
}
