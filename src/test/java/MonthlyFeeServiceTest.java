import daoImp.TransactionStatementDAOImp;
import daoInterface.MonthlyFeeDAO;
import model.MonthlyFee;
import views.stateModel.StatusMessageModel;
import model.TransactionStatement;
import model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.MonthlyFeeService;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.Year;

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
    private MonthlyFee testFee;
    private double testPayAmount;

    @BeforeEach
    void setUp() {
        testStudent = new Users();
        testStudent.setId(1L);
        testFeeAmount = 1000.0;
        currentMonth = LocalDate.now().getMonth().toString();
        currentYear = Year.now().getValue();

        testFee = new MonthlyFee();
        testFee.setId(1L);
        testFee.setStudentId(testStudent);
        testFee.setFeeAmount(1000.0);
        testFee.setPaid(200.0);
        testFee.setDue(800.0);

        testPayAmount = 300.0;
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
    void testAssignStudentMonthlyFee_PersistenceException() {
        when(monthlyFeeDAO.checkAssignFee(anyLong(), anyString(), anyInt()))
                .thenThrow(new PersistenceException("Database connection failed"));

        StatusMessageModel result = monthlyFeeService.assignStudentMonthlyFee(testStudent, testFeeAmount);

        assertFalse(result.isStatus());
        assertEquals("A database error occurred while assign fee.", result.getMessage());
    }

    @Test
    void testAssignStudentMonthly_RuntimeException(){
        when(monthlyFeeDAO.checkAssignFee(anyLong(),anyString(),anyInt()))
                .thenThrow(new RuntimeException("Unexpected Error"));

        StatusMessageModel result = monthlyFeeService.assignStudentMonthlyFee(testStudent, testFeeAmount);

        assertFalse(result.isStatus());
        assertEquals("An unexpected error occurred.", result.getMessage());

    }

    @Test
    void testAssignStudentFee_PersistenceExceptionWhileAdd(){
        when(monthlyFeeDAO.checkAssignFee(testStudent.getId(),currentMonth,currentYear))
                .thenReturn(null);

        when(monthlyFeeDAO.add(any(MonthlyFee.class)))
                .thenThrow(new PersistenceException("Insert Failed"));

        StatusMessageModel result = monthlyFeeService.assignStudentMonthlyFee(testStudent,testFeeAmount);
        assertFalse(result.isStatus());
        assertEquals("A database error occurred while assign fee.", result.getMessage());

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

    @Test
    void testPayFeeByAdmin_SuccessfulPayment() {
        String status = "COMPLETED";
        when(monthlyFeeDAO.update(any(MonthlyFee.class))).thenReturn(true);
        when(transactionStatementDAOImp.add(any(TransactionStatement.class))).thenReturn(true);

        StatusMessageModel result = monthlyFeeService.payFee(testFee, testPayAmount, status);

        assertTrue(result.isStatus());
        assertEquals("Fee Payment Success", result.getMessage());

        verify(monthlyFeeDAO).update(argThat(fee ->
                fee.getPaid() == 500.0 &&
                        fee.getDue() == 500.0
        ));

        verify(transactionStatementDAOImp).add(any(TransactionStatement.class));
    }

    @Test
    void testFeeByAdmin_PersistenceExceptionWhilePayFeePassUpdate(){
        when(monthlyFeeDAO.update(any(MonthlyFee.class))).thenReturn(true);
        when(transactionStatementDAOImp.add(any(TransactionStatement.class)))
                .thenThrow(new PersistenceException("Add Transaction Statement is failed"));
        StatusMessageModel result = monthlyFeeService.payFee(testFee,testPayAmount,"COMPLETED");
        assertFalse(result.isStatus());
        assertEquals("Fee Payment Success but not Add Transaction History", result.getMessage());
        verify(monthlyFeeDAO).update(argThat(fee->
                fee.getPaid() == 500.0 &&
                fee.getDue() == 500.0
        ));

        verify(transactionStatementDAOImp).add(any(TransactionStatement.class));
    }

    @Test
    void testFeeByAdmin_PersistenceException(){
        when(monthlyFeeDAO.update(any(MonthlyFee.class)))
                .thenThrow(new PersistenceException("Database error occurred"));
        StatusMessageModel result = monthlyFeeService.payFee(testFee,testPayAmount,"COMPLETED");
        assertFalse(result.isStatus());
        assertEquals("A database error occurred while pay fee.", result.getMessage());
        verify(monthlyFeeDAO).update(argThat(fee ->
            fee.getPaid() == 500.0 &&
                    fee.getDue() == 500.0
        ));

    }

    @Test
    void testPayFeeByUser_SuccessfulPayment() {
        String status = "PENDING";

        when(transactionStatementDAOImp.add(any(TransactionStatement.class))).thenReturn(true);

        StatusMessageModel result = monthlyFeeService.payFee(testFee, testPayAmount, status);

        assertTrue(result.isStatus());
        assertEquals("Your Payment Request Successfully", result.getMessage());

        verify(transactionStatementDAOImp).add(argThat(fee ->
                fee.getPayAmount() == 300 &&
                        fee.getStatementDue() == 500.0
        ));

        verify(transactionStatementDAOImp).add(any(TransactionStatement.class));
    }
}
