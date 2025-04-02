import daoImp.TransactionStatementDAOImp;
import daoInterface.MonthlyFeeDAO;
import model.MonthlyFee;
import model.StatusMessageModel;
import model.TransactionStatement;
import model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.MonthlyFeeService;

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

//    @Test
//    void testAssignStudentMonthlyFee_SuccessfulAssignment() {
//        when(monthlyFeeDAO.checkAssignFee(testStudent.getId(), currentMonth, currentYear))
//                .thenReturn(null);
//        when(monthlyFeeDAO.add(any(MonthlyFee.class))).thenReturn(true);
//
//        StatusMessageModel result = monthlyFeeService.assignStudentMonthlyFee(testStudent, testFeeAmount);
//
//        assertTrue(result.isStatus());
//        assertEquals("Assign Fee Successfully", result.getMessage());
//
//        verify(monthlyFeeDAO).add(argThat(fee ->
//                fee.getStudentId().equals(testStudent) &&
//                        fee.getFeeAmount() == testFeeAmount &&
//                        fee.getDue() == testFeeAmount &&
//                        fee.getPaid() == 0 &&
//                        fee.getMonth().equals(currentMonth) &&
//                        fee.getYear() == currentYear &&
//                        fee.getIssueDate() != null
//        ));
//    }
//
//    @Test
//    void testAssignStudentMonthlyFee_FeeAlreadyAssigned() {
//        MonthlyFee existingFee = new MonthlyFee();
//        when(monthlyFeeDAO.checkAssignFee(testStudent.getId(), currentMonth, currentYear))
//                .thenReturn(existingFee);
//
//        StatusMessageModel result = monthlyFeeService.assignStudentMonthlyFee(testStudent, testFeeAmount);
//
//        assertFalse(result.isStatus());
//        assertEquals("This Month Fee Already Assign", result.getMessage());
//        verify(monthlyFeeDAO, never()).add(any());
//    }
//
//    @Test
//    void testPayFee_SuccessfulPayment() {
//        when(monthlyFeeDAO.update(any(MonthlyFee.class))).thenReturn(true);
//        when(transactionStatementDAOImp.add(any(TransactionStatement.class))).thenReturn(true);
//
//        StatusMessageModel result = monthlyFeeService.payFee(testFee, testPayAmount);
//
//        assertTrue(result.isStatus());
//        assertEquals("Fee Payment Success", result.getMessage());
//
//        verify(monthlyFeeDAO).update(argThat(fee ->
//                fee.getPaid() == 500.0 &&
//                        fee.getDue() == 500.0
//        ));
//
//        verify(transactionStatementDAOImp).add(any(TransactionStatement.class));
//    }
}
