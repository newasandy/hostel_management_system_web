import daoImp.TransactionStatementDAOImp;
import daoInterface.MonthlyFeeDAO;
import model.MonthlyFee;
import model.StatusMessageModel;
import model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.MonthlyFeeService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MonthlyFeeServiceTest {

    @Mock
    private MonthlyFeeDAO monthlyFeeDAO;

    @Mock
    private TransactionStatementDAOImp transactionStatementDAOImp;

    @InjectMocks
    private MonthlyFeeService monthlyFeeService;

    private Users testStudent;
    private double testFeeAmount;
    private String currentMonth;
    private int currentYear;

    @BeforeEach
    void setUp() {
        testStudent = new Users();
        testStudent.setId(1L);
        testFeeAmount = 1000.0;
        currentMonth = LocalDate.now().getMonth().toString();
        currentYear = Year.now().getValue();
    }

    @Test
    void testAssignStudentMonthlyFee_SuccessfulAssignment() {
        when(monthlyFeeDAO.checkAssignFee(testStudent.getId(), currentMonth, currentYear))
                .thenReturn(null);
        when(monthlyFeeDAO.add(any(MonthlyFee.class))).thenReturn(true);

        StatusMessageModel result = monthlyFeeService.assignStudentMonthlyFee(testStudent, testFeeAmount);

        assertTrue(result.isStatus());
        assertEquals("Assign Fee Successfully", result.getMessage());

        verify(monthlyFeeDAO).add(argThat(fee ->
                fee.getStudentId().equals(testStudent) &&
                        fee.getFeeAmount() == testFeeAmount &&
                        fee.getDue() == testFeeAmount &&
                        fee.getPaid() == 0 &&
                        fee.getMonth().equals(currentMonth) &&
                        fee.getYear() == currentYear &&
                        fee.getIssueDate() != null
        ));
    }

    @Test
    void testAssignStudentMonthlyFee_FeeAlreadyAssigned() {
        MonthlyFee existingFee = new MonthlyFee();
        when(monthlyFeeDAO.checkAssignFee(testStudent.getId(), currentMonth, currentYear))
                .thenReturn(existingFee);

        StatusMessageModel result = monthlyFeeService.assignStudentMonthlyFee(testStudent, testFeeAmount);

        assertFalse(result.isStatus());
        assertEquals("This Month Fee Already Assign", result.getMessage());
        verify(monthlyFeeDAO, never()).add(any());
    }
}
