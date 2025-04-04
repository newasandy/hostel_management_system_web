import daoInterface.VisitorsDAO;
import views.stateModel.StatusMessageModel;
import model.Users;
import model.Visitors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.VisitorService;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class VisitorServicesTest {

    @Mock
    private VisitorsDAO visitorsDAO;

    @InjectMocks
    private VisitorService visitorService;


    private String fullName;
    private String reason;
    private Users student;
    private String relation;

    @BeforeEach
    void setup(){
        student = new Users();
        fullName = "test visitor";
        reason = "test reason";
        relation = "test relation";
    }

    @Test
    void create_newVisitor(){

        when(visitorsDAO.add(any(Visitors.class))).thenReturn(true);

        StatusMessageModel result = visitorService.addVisitor(fullName,reason,student,relation);

        assertTrue(result.isStatus());

        assertEquals("New Visitor Added Successfully", result.getMessage());

        verify(visitorsDAO, times(1)).add(argThat(visitors ->
                visitors.getStudentId().equals(student) &&
                visitors.getReason().equals(reason) &&
                visitors.getRelation().equals(relation) &&
                visitors.getFullName().equals(fullName)
        ));
    }

    @Test
    void create_newVisitor_failed(){

        when(visitorsDAO.add(any(Visitors.class))).thenReturn(false);

        StatusMessageModel result = visitorService.addVisitor(fullName,reason,student,relation);

        assertFalse(result.isStatus());

        assertEquals("Visitor Added Failed", result.getMessage());

        verify(visitorsDAO, times(1)).add(argThat(visitors ->
                visitors.getStudentId().equals(student) &&
                        visitors.getReason().equals(reason) &&
                        visitors.getRelation().equals(relation) &&
                        visitors.getFullName().equals(fullName)
        ));
    }
}
