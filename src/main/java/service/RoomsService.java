package service;

import daoInterface.RoomDAO;
import model.Rooms;
import model.StatusMessageModel;

public class RoomsService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private RoomDAO roomDAO;

    public RoomsService(RoomDAO roomDAO){
        this.roomDAO = roomDAO;
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

}
