import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int roomNumber;
    private final String roomType;
    private final double pricePerNight;
    private boolean isBooked;

    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public void display() {
        System.out.printf("%-10d %-18s Rs. %-12.2f %s%n",
                roomNumber, roomType, pricePerNight,
                isBooked ? "Booked" : "Available");
    }
}
