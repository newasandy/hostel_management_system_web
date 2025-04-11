package views.stateModel;

import model.Address;
import model.Rooms;
import model.UserType;
import model.Users;

import java.util.List;

public class UserState {


    private String userRole = "GUEST";

    private String name;
    private String email;
    private String password;
    private String role;
    private String country;
    private String district;
    private String rmcMc;
    private int wardNumber;
    private UserType selectUserType;
    private Rooms selectRoom;

    private List<UserType> userTypes;
    private Users selectUser;
    private Address selectUserAddress;

    private List<Users> activeUserlist;

    private List<Users> onlyStudent;
    private String searchItem;

    private int cdTime;

    private boolean selectRoomForNewUser = false;

    public UserState() {
    }

    public int getCdTime() {
        return cdTime;
    }

    public void setCdTime(int cdTime) {
        this.cdTime = cdTime;
    }

    public List<Users> getActiveUserlist() {
        return activeUserlist;
    }

    public void setActiveUserlist(List<Users> activeUserlist) {
        this.activeUserlist = activeUserlist;
    }

    public String getUserRole() {
        return userRole;
    }

    public boolean isGuest() {
        return "GUEST".equals(userRole);
    }

    public boolean isUser() {
        return "USER".equals(userRole);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(userRole);
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRmcMc() {
        return rmcMc;
    }

    public void setRmcMc(String rmcMc) {
        this.rmcMc = rmcMc;
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public UserType getSelectUserType() {
        return selectUserType;
    }

    public void setSelectUserType(UserType selectUserType) {
        this.selectUserType = selectUserType;
    }

    public Rooms getSelectRoom() {
        return selectRoom;
    }

    public void setSelectRoom(Rooms selectRoom) {
        this.selectRoom = selectRoom;
    }

    public List<UserType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<UserType> userTypes) {
        this.userTypes = userTypes;
    }

    public Users getSelectUser() {
        return selectUser;
    }

    public void setSelectUser(Users selectUser) {
        this.selectUser = selectUser;
    }

    public Address getSelectUserAddress() {
        return selectUserAddress;
    }

    public void setSelectUserAddress(Address selectUserAddress) {
        this.selectUserAddress = selectUserAddress;
    }

    public boolean isSelectRoomForNewUser() {
        return selectRoomForNewUser;
    }

    public void setSelectRoomForNewUser(boolean selectRoomForNewUser) {
        this.selectRoomForNewUser = selectRoomForNewUser;
    }

    public List<Users> getOnlyStudent() {
        return onlyStudent;
    }

    public void setOnlyStudent(List<Users> onlyStudent) {
        this.onlyStudent = onlyStudent;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public void resetFields() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.role = "";
        this.selectUserType = null;
        this.country = "";
        this.district = "";
        this.rmcMc = "";
        this.wardNumber = 1;
        this.selectRoom = null;
    }
}
