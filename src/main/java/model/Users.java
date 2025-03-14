package model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "passwords", nullable = false, length = 100)
    private String passwords;

    @Column(name = "roles", nullable = false, length = 20)
    private String roles;

    @Column(name = "status", nullable = false)
    private boolean status;

    public Users() {
    }

    public Users(Long id, String fullName, String email, String passwords, String roles, boolean status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwords = passwords;
        this.roles = roles;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
