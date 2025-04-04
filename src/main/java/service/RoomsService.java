package service;

import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import model.RoomAllocation;
import model.Rooms;
import views.stateModel.StatusMessageModel;
import model.Users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;

@RequestScoped
public class RoomsService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private RoomAllocationDAO roomAllocationDAO;

    @Transactional
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

    @Transactional
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
