package views.stateModel;

import model.RoomAllocation;
import model.Rooms;
import model.Users;

public class RoomState {

    private int roomNumber;
    private int capacity;
    private int updatedCapacity;
    private Rooms selectRoom;
    private Users selectStudent;

    private Long updateRoomId;

    private GenericLazyDataModel<Rooms> orginalRoomsList;
    private GenericLazyDataModel<Rooms> viewRoomsList;
    private GenericLazyDataModel<RoomAllocation> roomAllocationList;

    private GenericLazyDataModel<Users> unallocatedUser;
    private GenericLazyDataModel<Rooms> availableRoom;


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

    public GenericLazyDataModel<Rooms> getOrginalRoomsList() {
        return orginalRoomsList;
    }

    public void setOrginalRoomsList(GenericLazyDataModel<Rooms> orginalRoomsList) {
        this.orginalRoomsList = orginalRoomsList;
    }

    public GenericLazyDataModel<Rooms> getViewRoomsList() {
        return viewRoomsList;
    }

    public void setViewRoomsList(GenericLazyDataModel<Rooms> viewRoomsList) {
        this.viewRoomsList = viewRoomsList;
    }

    public GenericLazyDataModel<RoomAllocation> getRoomAllocationList() {
        return roomAllocationList;
    }

    public void setRoomAllocationList(GenericLazyDataModel<RoomAllocation> roomAllocationList) {
        this.roomAllocationList = roomAllocationList;
    }

    public GenericLazyDataModel<Users> getUnallocatedUser() {
        return unallocatedUser;
    }

    public void setUnallocatedUser(GenericLazyDataModel<Users> unallocatedUser) {
        this.unallocatedUser = unallocatedUser;
    }

    public GenericLazyDataModel<Rooms> getAvailableRoom() {
        return availableRoom;
    }

    public void setAvailableRoom(GenericLazyDataModel<Rooms> availableRoom) {
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
