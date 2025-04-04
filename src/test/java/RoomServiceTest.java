import daoInterface.RoomAllocationDAO;
import daoInterface.RoomDAO;
import model.Rooms;
import views.stateModel.StatusMessageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.RoomsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private RoomAllocationDAO roomAllocationDAO;

    @InjectMocks
    private RoomsService roomsService;

    private int roomNumber;
    private int capacity;

    @BeforeEach
    void setup(){
        roomNumber = 101;
        capacity = 3;
    }

    @Test
    void addNewRoom_TestSuccess(){

        when(roomDAO.findByRoomNumber(roomNumber)).thenReturn(null);

        when(roomDAO.add(any(Rooms.class))).thenReturn(true);

        StatusMessageModel result = roomsService.addNewRoom(roomNumber,capacity);

        assertTrue(result.isStatus());
        assertEquals("Room Added Successfully", result.getMessage());

        verify(roomDAO).add(argThat(rooms -> rooms.getRoomNumber() == roomNumber &&
                rooms.getCapacity() == capacity));
    }

    @Test
    void addNewRoomAlreadyExist(){
        Rooms existRoom = new Rooms();
        when(roomDAO.findByRoomNumber(roomNumber)).thenReturn(existRoom);

        StatusMessageModel result = roomsService.addNewRoom(roomNumber,capacity);

        assertFalse(result.isStatus());
        assertEquals("!! Room Already Exist", result.getMessage());

        verify(roomDAO, never()).add(any());

    }
}
