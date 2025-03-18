package daoInterface;

import model.Rooms;

import java.util.List;

public interface RoomDAO extends BaseDAO<Rooms>{
    Rooms findByRoomNumber(int roomNumber);
    List<Rooms> getAvailableRoom();
}