package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_type", schema = "hostelmanagement")
public class UserType extends BaseEntity{

    @Column(name = "types" , nullable = false , length = 20)
    private String userTypes;

    @OneToMany(mappedBy = "roles")
    private List<Users> users;

    public UserType() {
    }

    public UserType(String userTypes) {
        this.userTypes = userTypes;
    }

    public String getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(String userTypes) {
        this.userTypes = userTypes;
    }
}
