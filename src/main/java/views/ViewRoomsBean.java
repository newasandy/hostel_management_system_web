package views;

import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import daoInterface.UsersDAO;
import model.*;
import service.RoomsService;
import utils.GetCookiesValues;
import views.stateModel.RoomState;
import views.stateModel.StatusMessageModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Named("viewRoomsBean")
@ViewScoped
public class ViewRoomsBean implements Serializable {

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private RoomAllocationDAO roomAllocationDAO;

    @Inject
    private RoomsService roomsService;

    private RoomState roomState = new RoomState();
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    @PostConstruct
    public void init(){
        refreshRoomList();
    }

    public void refreshRoomList(){
        roomState.setViewRoomsList(roomDAO.getAll());
        roomState.setUnallocatedUser(usersDAO.getUnallocatedUsers());
        roomState.setAvailableRoom(roomDAO.getAvailableRoom());
        if ("USER".equals(GetCookiesValues.getUserRoleFromCookie())){
            Users loginUser = usersDAO.getByEmail(GetCookiesValues.getEmailFromCookie());
            roomState.setRoomAllocationList(roomAllocationDAO.getUserAllocated(loginUser.getId()));
        }
        if ("ADMIN".equals(GetCookiesValues.getUserRoleFromCookie())){
            List<RoomAllocation> orginalRoomAllocationList = roomAllocationDAO.getAll();
            Collections.sort(orginalRoomAllocationList, new Comparator<RoomAllocation>() {
                @Override
                public int compare(RoomAllocation v1, RoomAllocation v2) {
                    return v2.getAllocationDate().compareTo(v1.getAllocationDate());
                }
            });
            roomState.setRoomAllocationList(orginalRoomAllocationList);
        }
    }

    public void addNewRoom(){
        statusMessageModel = roomsService.addNewRoom(roomState.getRoomNumber(),roomState.getCapacity());
        try{
            if (statusMessageModel.isStatus()){
                refreshRoomList();
                roomState.resetFields();
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

    @Transactional
    public void updateRoom(){
        try{
            if (roomState.getUpdatedCapacity() >= roomAllocationDAO.getRoomOccupancy(roomState.getSelectRoom())){
                roomState.getSelectRoom().setCapacity(roomState.getUpdatedCapacity());
                if (roomDAO.update(roomState.getSelectRoom())){
                    refreshRoomList();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Room Successfully"));
                }else {
                    refreshRoomList();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Room Not Update"));
                }
            }else {
                refreshRoomList();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Capacity is less then Room Occupancy"));
            }
        }catch (Exception e){
            refreshRoomList();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
        }
    }

    @Transactional
    public void disableRoom(Rooms room){
        room.setStatus(false);
        try{
            if (roomDAO.update(room)){
                disableRoomUnallocatedStudent(room);
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

    @Transactional
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

    public String forAllocation(){
        return "/admin/roomAllocation.xhtml?faces-redirect=true";
    }

    public String allocateStudentInARoom(){
        statusMessageModel = roomsService.allocateStudentInRoom(roomState.getSelectStudent(),roomState.getSelectRoom());
        try{
            if (statusMessageModel.isStatus()){
                roomState.resetFields();
                refreshRoomList();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
            }
            return "/admin/viewRoomAllocation.xhtml?faces-redirect=true";
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room"));
            return null;
        }
    }

    @Transactional
    public void unAllocatedStudent(RoomAllocation unallocated){
        Date date = new Date();
        Timestamp unAllocatedDate = new Timestamp(date.getTime());
        unallocated.setUnallocationDate(unAllocatedDate);
        try{
            if (roomAllocationDAO.update(unallocated)){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Unallocated Successfully"));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Unallocated"));
            }
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Unallocated"));
        }
    }

    @Transactional
    public void disableRoomUnallocatedStudent(Rooms disableRoom){
        Date date = new Date();
        Timestamp unAllocatedDate = new Timestamp(date.getTime());
        try{
            if (roomAllocationDAO.getRoomOccupancy(disableRoom) > 0){
                if (roomAllocationDAO.disableRoomUnallocatedStudent(disableRoom.getId(), unAllocatedDate)){
                    refreshRoomList();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Unallocated All Student from Disable Room"));
                }else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Unallocated all Student"));
                }
            }
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Unallocated  all Student"));
        }
    }

    public RoomState getRoomState() {
        return roomState;
    }

    public void prepareSelectStudent(Users selectStudent){
        this.roomState.setSelectStudent(selectStudent);
    }
    public void prepareSelectRoom(Rooms selectRoom){
        this.roomState.setSelectRoom(selectRoom);
    }

    public void prepareEditRoom(Rooms room) {
        roomState.setSelectRoom(room);
        roomState.setUpdatedCapacity(room.getCapacity());
    }

}
