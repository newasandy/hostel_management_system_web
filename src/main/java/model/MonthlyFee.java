package model;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.Year;
import java.util.List;

@Entity
@Table(name = "monthly_fee", schema = "hostelmanagement")
public class MonthlyFee extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Users studentId;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "month", nullable = false , length = 20)
    private String month;

    @Column(name = "issue_date", nullable = false)
    private Timestamp issueDate;

    @Column(name = "fee_amount", nullable = false)
    private double feeAmount;

    @Column(name = "paid", nullable = false)
    private double paid;

    @Column(name = "due", nullable = false)
    private double due;

    @OneToMany(mappedBy = "feeId", cascade = CascadeType.ALL)
    private List<TransactionStatement> transactionStatements;

    public MonthlyFee() {
    }

    public MonthlyFee(Users studentId, int year, String month, Timestamp issueDate, double feeAmount, double paid, double due, List<TransactionStatement> transactionStatements) {
        this.studentId = studentId;
        this.year = year;
        this.month = month;
        this.issueDate = issueDate;
        this.feeAmount = feeAmount;
        this.paid = paid;
        this.due = due;
        this.transactionStatements = transactionStatements;
    }

    public Users getStudentId() {
        return studentId;
    }

    public void setStudentId(Users studentId) {
        this.studentId = studentId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Timestamp getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        this.due = due;
    }

    public List<TransactionStatement> getTransactionStatements() {
        return transactionStatements;
    }

    public void setTransactionStatements(List<TransactionStatement> transactionStatements) {
        this.transactionStatements = transactionStatements;
    }
}
