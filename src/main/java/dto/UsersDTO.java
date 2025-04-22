package dto;

import dto.dtoMapper.DTOMapper;
import model.Users;

public class UsersDTO {
    private Long id;
    private String fullName;
    private String email;
    private String passwords;
    private Long roleId;
    private boolean status;
    private AddressDTO address;

    public UsersDTO() {}

    public UsersDTO(Long id, String fullName, String email, String passwords, Long roleId, boolean status, AddressDTO address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwords = passwords;
        this.roleId = roleId;
        this.status = status;
        this.address = address;
    }

    public UsersDTO(Users users) {
        this.id = users.getId();
        this.fullName = users.getFullName();
        this.email = users.getEmail();
        this.passwords = null;
        this.roleId = users.getRoles().getId();
        this.status = users.isStatus();
        this.address = DTOMapper.toAddressDTO(users.getAddress());
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}

