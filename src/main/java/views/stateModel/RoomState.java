package views.stateModel;

import model.RoomAllocation;
import model.Rooms;
import model.Users;

import java.util.List;

public class RoomState {

    private int roomNumber;
    private int capacity;
    private int updatedCapacity;
    private Rooms selectRoom;
    private Users selectStudent;

    private Long updateRoomId;

    private List<Rooms> orginalRoomsList;
    private List<Rooms> viewRoomsList;
    private List<RoomAllocation> roomAllocationList;

    private List<Users> unallocatedUser;
    private List<Rooms> availableRoom;


    public RoomState() {
    }

    public Long getUpdateRoomId() {
        return updateRoomId;
    }

    public void setUpdateRoomId(Long updateRoomId) {
        this.updateRoomId = updateRoomId;
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

    public int getUpdatedCapacity() {
        return updatedCapacity;
    }

    public void setUpdatedCapacity(int updatedCapacity) {
        this.updatedCapacity = updatedCapacity;
    }

    public List<Rooms> getOrginalRoomsList() {
        return orginalRoomsList;
    }

    public void setOrginalRoomsList(List<Rooms> orginalRoomsList) {
        this.orginalRoomsList = orginalRoomsList;
    }

    public List<Rooms> getViewRoomsList() {
        return viewRoomsList;
    }

    public void setViewRoomsList(List<Rooms> viewRoomsList) {
        this.viewRoomsList = viewRoomsList;
    }

    public List<RoomAllocation> getRoomAllocationList() {
        return roomAllocationList;
    }

    public void setRoomAllocationList(List<RoomAllocation> roomAllocationList) {
        this.roomAllocationList = roomAllocationList;
    }

    public List<Users> getUnallocatedUser() {
        return unallocatedUser;
    }

    public void setUnallocatedUser(List<Users> unallocatedUser) {
        this.unallocatedUser = unallocatedUser;
    }

    public List<Rooms> getAvailableRoom() {
        return availableRoom;
    }

    public void setAvailableRoom(List<Rooms> availableRoom) {
        this.availableRoom = availableRoom;
    }

    public Rooms getSelectRoom() {
        return selectRoom;
    }

    public void setSelectRoom(Rooms selectRoom) {
        this.selectRoom = selectRoom;
    }

    public Users getSelectStudent() {
        return selectStudent;
    }

    public void setSelectStudent(Users selectStudent) {
        this.selectStudent = selectStudent;
    }

    public void resetFields(){
        this.roomNumber = 0;
        this.capacity = 0;
        this.selectRoom = null;
        this.selectStudent = null;
    }
}
