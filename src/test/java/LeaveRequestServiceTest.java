import daoInterface.LeaveRequestDAO;
import model.LeaveRequest;
import views.stateModel.StatusMessageModel;
import model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.LeaveRequestService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestDAO leaveRequestDAO;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    private Users testStudent;
    private LocalDate testStartDate;
    private LocalDate testEndDate;
    private String testReason;

    @BeforeEach
    void setUp() {
        testStudent = new Users();
        testStudent.setId(1L);
        testStartDate = LocalDate.now().plusDays(1);
        testEndDate = LocalDate.now().plusDays(3);
        testReason = "Family emergency";
    }

    @Test
    void testApplyLeaveRequest_Success() {
        when(leaveRequestDAO.add(any(LeaveRequest.class))).thenReturn(true);


        StatusMessageModel result = leaveRequestService.applyLeaveRequest(
                testStudent, testReason, testStartDate, testEndDate);

        assertTrue(result.isStatus());
        assertEquals("Leave Application is Submit Successfully", result.getMessage());

        verify(leaveRequestDAO, times(1)).add(argThat(leaveRequest ->
                leaveRequest.getStudentId().equals(testStudent) &&
                        leaveRequest.getReason().equals(testReason) &&
                        leaveRequest.getStartFrom().equals(testStartDate) &&
                        leaveRequest.getEndOn().equals(testEndDate) &&
                        leaveRequest.getStatus() == LeaveRequest.Status.PENDING
        ));
    }

    @Test
    void testApplyLeaveRequest_Failure() {
        // Arrange
        when(leaveRequestDAO.add(any(LeaveRequest.class))).thenReturn(false);

        // Act
        StatusMessageModel result = leaveRequestService.applyLeaveRequest(
                testStudent, testReason, testStartDate, testEndDate);

        // Assert
        assertFalse(result.isStatus());
        assertEquals("!! Leave Application is not Submit", result.getMessage());
        verify(leaveRequestDAO, times(1)).add(any(LeaveRequest.class));
    }
}
