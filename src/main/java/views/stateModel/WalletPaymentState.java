package views.stateModel;

public class WalletPaymentState {

    private String username;
    private double balance;
    private String usernameOrContact;

    public WalletPaymentState() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUsernameOrContact() {
        return usernameOrContact;
    }

    public void setUsernameOrContact(String usernameOrContact) {
        this.usernameOrContact = usernameOrContact;
    }
}
