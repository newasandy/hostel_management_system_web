package dto.dtoMapper;

import dto.AddressDTO;
import dto.RoomsDTO;
import dto.UserTypeDTO;
import dto.UsersDTO;
import model.Address;
import model.Rooms;
import model.UserType;
import model.Users;
import utils.PasswordUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    public static AddressDTO toAddressDTO(Address entity) {
        if (entity == null) return null;
        AddressDTO dto = new AddressDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setCountry(entity.getCountry());
        dto.setDistrict(entity.getDistrict());
        dto.setRmcMc(entity.getRmcMc());
        dto.setWardNo(entity.getWardNo());
        return dto;
    }

    public static Address toAddressEntity(AddressDTO addressDTO, Users user){
        if (addressDTO == null) return null;
        Address address = new Address();
        address.setId(addressDTO.getId());
        address.setCountry(addressDTO.getCountry());
        address.setDistrict(addressDTO.getDistrict());
        address.setRmcMc(addressDTO.getRmcMc());
        address.setWardNo(addressDTO.getWardNo());
        address.setUser(user);
        return address;
    }

    public static UsersDTO toUserDTO(Users entity) {
        if (entity == null) return null;
        UsersDTO dto = new UsersDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setPasswords(entity.getPasswords());
        dto.setRole(toUserType(entity.getRoles()));
        dto.setStatus(entity.isStatus());
        dto.setAddress(toAddressDTO(entity.getAddress()));
        return dto;
    }

    public static Users toUserEntity(UsersDTO usersDTO , Users user){
        if (usersDTO == null)return null;
        user.setFullName(usersDTO.getFullName());
        user.setEmail(usersDTO.getEmail());
        user.setStatus(usersDTO.isStatus());
        user.setAddress(toAddressEntity(usersDTO.getAddress(),user));
        return user;
    }

    public static Users toRegisterUserEntity(UsersDTO usersDTO){
        if (usersDTO == null) return null;
        Users user = new Users();
        user.setFullName(usersDTO.getFullName());
        user.setEmail(usersDTO.getEmail());
        user.setPasswords(PasswordUtils.getHashPassword(usersDTO.getPasswords()));
        user.setRoles(toUserTypeEntity(usersDTO.getRole()));
        user.setStatus(usersDTO.isStatus());
        user.setAddress(toAddressEntity(usersDTO.getAddress(),user));
        return user;
    }

    public static UserTypeDTO toUserType(UserType entity){
        if (entity == null) return null;
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(entity.getId());
        userTypeDTO.setRoles(entity.getUserTypes());
        return userTypeDTO;
    }

    public static UserType toUserTypeEntity(UserTypeDTO userTypeDTO){
        if (userTypeDTO == null)return null;
        UserType userType = new UserType();
        userType.setId(userTypeDTO.getId());
        userType.setUserTypes(userTypeDTO.getRoles());
        return userType;
    }

    public static Rooms toRoomEntity(RoomsDTO roomsDTO){
        if (roomsDTO == null)return null;
        Rooms rooms = new Rooms();
        rooms.setId(roomsDTO.getId());
        rooms.setRoomNumber(roomsDTO.getRoomNumber());
        rooms.setCapacity(roomsDTO.getCapacity());
        return rooms;
    }

    public static List<UserTypeDTO> userTypeList(List<UserType> userTypes){
        List<UserTypeDTO> userTypeDTOList;
        userTypeDTOList = userTypes.stream().map(UserTypeDTO::new).collect(Collectors.toList());
        return userTypeDTOList;
    }

    public static List<UsersDTO> userTableList(List<Users> user){
        List<UsersDTO> usersDTOList ;
        usersDTOList = user.stream().map(UsersDTO::new).collect(Collectors.toList());
        return usersDTOList;
    }
    public static List<RoomsDTO> roomTableList(List<Rooms> rooms){
        List<RoomsDTO> roomsDTOList;
        roomsDTOList = rooms.stream().map(RoomsDTO::new).collect(Collectors.toList());
        return roomsDTOList;
    }
}
