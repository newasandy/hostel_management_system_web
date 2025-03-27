import daoInterface.UsersDAO;
import model.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.AuthenticationService;
import utils.PasswordUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UsersDAO usersDAO;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testLoginService_SuccessfulLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "correctPassword";
        String hashedPassword = PasswordUtils.getHashPassword(password);

        Users mockUser = new Users();
        mockUser.setEmail(email);
        mockUser.setPasswords(hashedPassword);

        when(usersDAO.getByEmail(email)).thenReturn(mockUser);

        Users result = authenticationService.loginService(email, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(usersDAO, times(1)).getByEmail(email);
    }


    @Test
    void testLoginService_UserNotFound() {
        String email = "nonexistent@example.com";
        String password = "anyPassword";

        when(usersDAO.getByEmail(email)).thenReturn(null);

        Users result = authenticationService.loginService(email, password);

        assertNull(result);
        verify(usersDAO, times(1)).getByEmail(email);
    }

    @Test
    void testLoginService_WrongPassword() {
        String email = "test@example.com";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        String hashedPassword = PasswordUtils.getHashPassword(correctPassword);
        Users mockUser = new Users();
        mockUser.setEmail(email);
        mockUser.setPasswords(hashedPassword);
        when(usersDAO.getByEmail(email)).thenReturn(mockUser);
        Users result = authenticationService.loginService(email, wrongPassword);
        assertNull(result);
        verify(usersDAO, times(1)).getByEmail(email);
    }

}
