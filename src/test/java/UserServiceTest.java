import daoImp.AddressDAOImp;
import daoInterface.UsersDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersDAO usersDAO;

    @Mock
    private AddressDAOImp addressDAOImp;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup(){

    }

}
