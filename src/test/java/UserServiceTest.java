import daoImp.AddressDAOImp;
import daoInterface.RoomAllocationDAO;
import daoInterface.UsersDAO;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;
import views.stateModel.StatusMessageModel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersDAO usersDAO;

    @Mock
    private AddressDAOImp addressDAOImp;

    @Mock
    private RoomAllocationDAO roomAllocationDAO;

    @InjectMocks
    private UserService userService;

    private UserType userType;
    private Rooms selectRoom;
    private String name ;
    private String email ;
    private String password ;
    private String country ;
    private String district ;
    private String rmcMc ;
    private int wardNo ;

    @BeforeEach
    void setup(){
        userType = new UserType();
        selectRoom = new Rooms();
        userType.setUserTypes("USER");
        name = "test case";
        email = "testcase@gmmail.com";
        password = "testcase";
        country = "nepal";
        district = "test dist";
        rmcMc = "test case";
        wardNo = 2;
    }

//    @Test
//    void create_newUser_Test(){
//
//        when(usersDAO.getByEmail(email)).thenReturn(null);
//
//        when(usersDAO.add(any(Users.class))).thenReturn(true);
//        when(addressDAOImp.add(any(Address.class))).thenReturn(true);
//        when(roomAllocationDAO.add(any(RoomAllocation.class))).thenReturn(true);
//
//        StatusMessageModel result = userService.registerNewStudent(name,email,password,userType,country,district,rmcMc,wardNo, selectRoom);
//
//        assertTrue(result.isStatus());
//        assertEquals("User Register Successfully", result.getMessage());
//
//    }
//
//    @Test
//    void create_newUser_alreadyExist(){
//        Users existUser = new Users();
//        when(usersDAO.getByEmail(email)).thenReturn(existUser);
//
//        StatusMessageModel result = userService.registerNewStudent(name,email,password,userType,country,district,rmcMc,wardNo, selectRoom);
//
//        assertFalse(result.isStatus());
//        assertEquals("User Already Exist", result.getMessage());
//    }
//
//    @Test
//    void create_newUser_failed(){
//        when(usersDAO.getByEmail(email)).thenReturn(null);
//
//        when(usersDAO.add(any(Users.class))).thenReturn(false);
//
//        StatusMessageModel result = userService.registerNewStudent(name,email,password,userType,country,district,rmcMc,wardNo, selectRoom);
//
//        assertFalse(result.isStatus());
//        assertEquals("User Register Unsuccessful", result.getMessage());
//    }

}
