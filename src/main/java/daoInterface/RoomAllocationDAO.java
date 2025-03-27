package daoInterface;

import model.RoomAllocation;
import model.Rooms;

import java.sql.Timestamp;
import java.util.List;

public interface RoomAllocationDAO extends BaseDAO<RoomAllocation> {
    Long getRoomOccupancy(Rooms roomId);
    List<RoomAllocation> getUserAllocated(Long userId);
    boolean disableRoomUnallocatedStudent(Long roomId, Timestamp unallocationDate);
    Long getCountOnlyAllocated();
    RoomAllocation getRecentUserRoomAllocation(Long userId);
}