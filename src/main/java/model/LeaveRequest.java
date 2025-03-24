package model;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "leave_request", schema = "hostelmanagement")
public class LeaveRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Users studentId;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "apply_date", nullable = false)
    private Timestamp applyDate;

    @Column(name = "start_from", nullable = false)
    private LocalDate startFrom;

    @Column(name = "end_on", nullable = false)
    private LocalDate endOn;

    @Enumerated(EnumType.STRING)
    private Status status;

    public LeaveRequest() {
    }

    public LeaveRequest(Users studentId, String reason, Timestamp applyDate, LocalDate startFrom, LocalDate endOn, Status status) {
        this.studentId = studentId;
        this.reason = reason;
        this.applyDate = applyDate;
        this.startFrom = startFrom;
        this.endOn = endOn;
        this.status = status;
    }

    public Users getStudentId() {
        return studentId;
    }

    public void setStudentId(Users studentId) {
        this.studentId = studentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Timestamp applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDate getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(LocalDate startFrom) {
        this.startFrom = startFrom;
    }

    public LocalDate getEndOn() {
        return endOn;
    }

    public void setEndOn(LocalDate endOn) {
        this.endOn = endOn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        ACCEPTED, PENDING, REJECTED
    }
}
