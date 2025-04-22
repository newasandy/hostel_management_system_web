package dto.dtoMapper;

import dto.AddressDTO;
import dto.UsersDTO;
import model.Address;
import model.Users;

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

    public static UsersDTO toUserDTO(Users entity) {
        if (entity == null) return null;
        UsersDTO dto = new UsersDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setPasswords(entity.getPasswords());
        dto.setRoleId(entity.getRoles().getId());
        dto.setStatus(entity.isStatus());
        dto.setAddress(toAddressDTO(entity.getAddress()));
        return dto;
    }

    public static List<UsersDTO> userTableList(List<Users> user){
        List<UsersDTO> usersDTOList ;
        usersDTOList = user.stream().map(UsersDTO::new).collect(Collectors.toList());
        return usersDTOList;
    }

    // Add reverse mapping methods if needed
}
