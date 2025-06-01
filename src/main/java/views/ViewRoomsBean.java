package views;

import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import daoInterface.UsersDAO;
import model.*;
import service.RoomsService;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.GenericLazyDataModel;
import views.stateModel.RoomState;
import views.stateModel.StatusMessageModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

@Named
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

    private RoomState roomState;
    private StatusMessageModel statusMessageModel;
    private Map<String, Object> matchFilterAll;
    private Map<String, Object> matchFilterUnAllocated;
    private Map<String, Object> matchFilterAvailableRoom;
    private Map<String, Object> matchFilterAllocateList;


    @PostConstruct
    public void init(){
        try{
            roomState = new RoomState();
            statusMessageModel = new StatusMessageModel();
            refreshRoomList();
        }catch (Exception e){
            throw new RuntimeException("Failed ti init",e);
        }
    }

    public void refreshRoomList(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if ("USER".equals(JwtUtils.getUserRole(SessionUtils.getToken(request)))){
            Users loginUser = usersDAO.getByEmail(JwtUtils.getUserEmail(SessionUtils.getToken(request)));
            matchFilterAllocateList = new HashMap<>();
            matchFilterAllocateList.put("studentId",loginUser);
            roomState.setRoomAllocationList(new GenericLazyDataModel<>(roomAllocationDAO,matchFilterAllocateList,false));
        }
        if ("ADMIN".equals(JwtUtils.getUserRole(SessionUtils.getToken(request)))){
            matchFilterAll = new HashMap<>();
            roomState.setViewRoomsList(new GenericLazyDataModel<>(roomDAO,matchFilterAll, false));
            matchFilterUnAllocated = new HashMap<>();
            roomState.setUnallocatedUser(new GenericLazyDataModel<>(usersDAO,matchFilterUnAllocated,true));
            matchFilterAvailableRoom = new HashMap<>();
            roomState.setAvailableRoom(new GenericLazyDataModel<>(roomDAO,matchFilterAvailableRoom,true));

            List<RoomAllocation> orginalRoomAllocationList = roomAllocationDAO.getAll();
            Collections.sort(orginalRoomAllocationList, new Comparator<RoomAllocation>() {
                @Override
                public int compare(RoomAllocation v1, RoomAllocation v2) {
                    return v2.getAllocationDate().compareTo(v1.getAllocationDate());
                }
            });
            matchFilterAllocateList = new HashMap<>();
            roomState.setRoomAllocationList(new GenericLazyDataModel<>(roomAllocationDAO,matchFilterAllocateList,false));
        }
    }

    public void addNewRoom(){
        try{
            if (roomState.getRoomNumber() <= 0 && roomState.getCapacity() <= 0){
                showMessage(FacesMessage.SEVERITY_ERROR,"Error","Invalid Room number and capacity");
                return;
            }
            statusMessageModel = roomsService.addNewRoom(roomState.getRoomNumber(),roomState.getCapacity());
            if (!statusMessageModel.isStatus()){
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
                return;
            }
            refreshRoomList();
            roomState.resetFields();
            showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
        }
    }

    public void updateRoom(){
        try{
            Rooms forUpdateRoom = roomDAO.getById(roomState.getUpdateRoomId());
            if (roomState.getUpdatedCapacity() >= roomAllocationDAO.getRoomOccupancy(forUpdateRoom)){
                if (roomsService.updateRoom(forUpdateRoom,roomState.getUpdatedCapacity())){
                    refreshRoomList();
                    showMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Room Successfully");
                }else {
                    refreshRoomList();
                    showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Room Not Update 1");
                }
            }else {
                refreshRoomList();
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Capacity is less then Room Occupancy");
            }
        }catch (Exception e){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room 2");
        }
    }

    public void disableRoom(Rooms room){
        try{
            statusMessageModel = roomsService.disableRoom(room);
            if (statusMessageModel.isStatus()){
                showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
            }
        }catch (Exception e){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room");
        }
    }

    public void enableRoom(Rooms room){
        try{
            if (roomsService.enableRoom(room)){
                showMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Room Successfully");
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room");
            }
        }catch (Exception e){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room");
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
                showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
            }
            return "/admin/viewRoomAllocation.xhtml?faces-redirect=true";
        }catch (Exception e){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update Room");
            return null;
        }
    }

    public void unAllocatedStudent(RoomAllocation unallocated){
        try{
            if (roomsService.unallocatedStudent(unallocated)){
                showMessage(FacesMessage.SEVERITY_INFO, "Success", "Unallocated Successfully");
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Unallocated");
            }
        }catch (Exception e){
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Unallocated");
        }
    }


    private void showMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
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
        roomState.setUpdateRoomId(room.getId());
        roomState.setUpdatedCapacity(room.getCapacity());
    }

}
