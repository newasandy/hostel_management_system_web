package service;

import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import model.RoomAllocation;
import model.Rooms;
import views.stateModel.StatusMessageModel;
import model.Users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;

@RequestScoped
@Transactional
public class RoomsService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private RoomAllocationDAO roomAllocationDAO;

    public StatusMessageModel addNewRoom(int roomNumber , int capacity){
        try{
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
        }catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while Create new Room.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public StatusMessageModel allocateStudentInRoom(Users selectStudent, Rooms selectRoom){
        try{
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
        }catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while allocate student.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public boolean updateRoom(Rooms selectRoom, int updatedCapacity){
        try{
            selectRoom.setCapacity(updatedCapacity);
            return roomDAO.update(selectRoom);
        }catch (PersistenceException e){
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public StatusMessageModel disableRoom(Rooms selectRoom){
        try{
            selectRoom.setStatus(false);
            if (roomDAO.update(selectRoom)){
                if (unallocatedStudentFromDisableRoom(selectRoom)){
                    statusMessageModel.setStatus(true);
                    statusMessageModel.setMessage("Room disable and Unallocated student from room.");
                }else {
                    statusMessageModel.setStatus(false);
                    statusMessageModel.setMessage("Room disable and but Failed to unallocated student.");
                }
            }else {
                statusMessageModel.setStatus(false);
                statusMessageModel.setMessage("Failed to disable room.");
            }
        }catch (PersistenceException e){
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("A database error occurred while disabling the room.");
        } catch (Exception e) {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("An unexpected error occurred.");
        }
        return statusMessageModel;
    }

    public boolean unallocatedStudentFromDisableRoom(Rooms disableRoom){
        try{
            Date date = new Date();
            Timestamp unAllocatedDate = new Timestamp(date.getTime());
            if (roomAllocationDAO.getRoomOccupancy(disableRoom) > 0){
                return roomAllocationDAO.disableRoomUnallocatedStudent(disableRoom.getId(), unAllocatedDate);
            }
            return true;
        }catch (PersistenceException e){
            return false;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean unallocatedStudent(RoomAllocation selectUnallocated){
        try{
            Date date = new Date();
            Timestamp unAllocatedDate = new Timestamp(date.getTime());
            selectUnallocated.setUnallocationDate(unAllocatedDate);
            return roomAllocationDAO.update(selectUnallocated);
        } catch (PersistenceException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
