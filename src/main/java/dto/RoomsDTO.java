package dto;

import model.Rooms;


public class RoomsDTO {
    private Long id;
    private int roomNumber;
    private int capacity;
    private boolean status;

    public RoomsDTO() {
    }

    public RoomsDTO(Long id, int roomNumber, int capacity, boolean status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.status = status;
    }
    public RoomsDTO(Rooms rooms) {
        this.id = rooms.getId();
        this.roomNumber = rooms.getRoomNumber();
        this.capacity = rooms.getCapacity();
        this.status = rooms.isStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
