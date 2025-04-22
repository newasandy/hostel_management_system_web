package dto;

public class AddressDTO {
    private Long id;
    private Long userId;
    private String country;
    private String district;
    private String rmcMc;
    private int wardNo;

    public AddressDTO() {}

    public AddressDTO(Long id, Long userId, String country, String district, String rmcMc, int wardNo) {
        this.id = id;
        this.userId = userId;
        this.country = country;
        this.district = district;
        this.rmcMc = rmcMc;
        this.wardNo = wardNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public int getWardNo() {
        return wardNo;
    }

    public void setWardNo(int wardNo) {
        this.wardNo = wardNo;
    }
}

