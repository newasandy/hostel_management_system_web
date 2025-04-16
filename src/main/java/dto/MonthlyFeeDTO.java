package dto;

import model.MonthlyFee;


public class MonthlyFeeDTO {

    private Long id;
    private int year;

    private String month;
    private double due;


    public MonthlyFeeDTO(MonthlyFee fee) {
        this.id = fee.getId();
        this.year = fee.getYear();
        this.month = fee.getMonth();
        this.due = fee.getDue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        this.due = due;
    }
}
