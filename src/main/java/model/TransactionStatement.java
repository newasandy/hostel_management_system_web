package model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction_statement", schema = "hostelmanagement")
public class TransactionStatement extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Users studentId;

    @ManyToOne
    @JoinColumn(name = "fee_id")
    private MonthlyFee feeId;

    @Column(name = "payment_date", nullable = false)
    private Timestamp paymentDate;

    @Column(name = "pay_amount", nullable = false)
    private double payAmount;

    public TransactionStatement() {
    }

    public TransactionStatement(Users studentId, MonthlyFee feeId, Timestamp paymentDate, double payAmount) {
        this.studentId = studentId;
        this.feeId = feeId;
        this.paymentDate = paymentDate;
        this.payAmount = payAmount;
    }

    public Users getStudentId() {
        return studentId;
    }

    public void setStudentId(Users studentId) {
        this.studentId = studentId;
    }

    public MonthlyFee getFeeId() {
        return feeId;
    }

    public void setFeeId(MonthlyFee feeId) {
        this.feeId = feeId;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }
}
