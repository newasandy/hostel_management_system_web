package service;

import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import model.RoomAllocation;
import model.Rooms;
import model.StatusMessageModel;
import model.Users;

import java.sql.Timestamp;
import java.util.Date;

public class RoomsService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private RoomDAO roomDAO;
    private RoomAllocationDAO roomAllocationDAO;

    public RoomsService(RoomDAO roomDAO, RoomAllocationDAO roomAllocationDAO){
        this.roomDAO = roomDAO;
        this.roomAllocationDAO = roomAllocationDAO;
    }

    public StatusMessageModel addNewRoom(int roomNumber , int capacity){
        Rooms addRoom = new Rooms();
        addRoom.setRoomNumber(roomNumber);
        addRoom.setCapacity(capacity);
        addRoom.setStatus(true);
        Rooms checkRoom = roomDAO.findByRoomNumber(roomNumber);
        if (checkRoom == null){
            if (roomDAO.add(addRoom)){
                statusMessageModel.setStatus(true);
                statusMessageModel.setMessage("Room Added Successfully");
            }else {
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("!! Room Not Added");
            }
        }else {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("!! Room Already Exist");
        }
        return statusMessageModel;
    }

    public StatusMessageModel allocateStudentInRoom(Users selectStudent, Rooms selectRoom){
        RoomAllocation roomAllocation = new RoomAllocation();
        roomAllocation.setStudentId(selectStudent);
        roomAllocation.setRoomId(selectRoom);
        Date date = new Date();
        Timestamp allocatedDate = new Timestamp(date.getTime());
        roomAllocation.setAllocationDate(allocatedDate);
        if (roomAllocationDAO.add(roomAllocation)){
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("Allocated student Success");
        }else {

            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("Allocation Unsuccessful");
        }
        return statusMessageModel;
    }
}
