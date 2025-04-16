package dto;

import model.MonthlyFee;
import model.Users;

import java.util.List;
import java.util.stream.Collectors;

public class UserFee {

    private String fullName;
    private String email;
    private List<MonthlyFeeDTO> monthlyFees;

    public UserFee(Users user, List<MonthlyFee> fees) {
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.monthlyFees = fees.stream().map(MonthlyFeeDTO::new).collect(Collectors.toList());
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MonthlyFeeDTO> getMonthlyFees() {
        return monthlyFees;
    }

    public void setMonthlyFees(List<MonthlyFeeDTO> monthlyFees) {
        this.monthlyFees = monthlyFees;
    }
}
