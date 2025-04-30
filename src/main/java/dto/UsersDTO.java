package dto;

import dto.dtoMapper.DTOMapper;
import model.Users;

public class UsersDTO {
    private Long id;
    private String fullName;
    private String email;
    private String passwords;
    private UserTypeDTO role;
    private boolean status;
    private AddressDTO address;
    private RoomsDTO room;

    public UsersDTO() {}

    public UsersDTO(Users users) {
        this.id = users.getId();
        this.fullName = users.getFullName();
        this.email = users.getEmail();
        this.passwords = null;
        this.role = DTOMapper.toUserType(users.getRoles());
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

    public UserTypeDTO getRole() {
        return role;
    }

    public void setRole(UserTypeDTO role) {
        this.role = role;
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

    public RoomsDTO getRoom() {
        return room;
    }

    public void setRoom(RoomsDTO room) {
        this.room = room;
    }
}

