package dto;

import model.UserType;

public class UserTypeDTO {
    private Long id;
    private String roles;

    public UserTypeDTO() {
    }

    public UserTypeDTO(UserType userType) {
        this.id = userType.getId();
        this.roles = userType.getUserTypes();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
