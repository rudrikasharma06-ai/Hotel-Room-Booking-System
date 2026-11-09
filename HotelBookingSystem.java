import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelBookingSystem {
    private static final String DATA_FILE = "rooms.dat";
    private final ArrayList<Room> rooms = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HotelBookingSystem hotel = new HotelBookingSystem();
        hotel.loadRooms();
        hotel.runMenu();
        hotel.saveRooms();
    }

    private void runMenu() {
        while (true) {
            System.out.println("\n=== Hotel Room Booking System ===");
            System.out.println("1. Add a room");
            System.out.println("2. Display all rooms");
            System.out.println("3. Book a room");
            System.out.println("4. Check out a room");
            System.out.println("0. Save and exit");

            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    displayRooms();
                    break;
                case 3:
                    bookRoom();
                    break;
                case 4:
                    checkOutRoom();
                    break;
                case 0:
                case Integer.MIN_VALUE:
                    return;
                default:
                    System.out.println("Please choose an option from 0 to 4.");
            }
        }
    }

    private void addRoom() {
        int roomNumber = readInt("Enter room number: ");
        if (roomNumber <= 0) {
            System.out.println("Room number must be a positive whole number.");
            return;
        }
        if (findRoom(roomNumber) != null) {
            System.out.println("A room with that number already exists.");
            return;
        }

        System.out.print("Enter room type: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String roomType = scanner.nextLine().trim();
        if (roomType.isEmpty()) {
            System.out.println("Room type cannot be empty.");
            return;
        }

        double price = readDouble("Enter price per night (Rs.): ");
        if (!(price > 0) || Double.isInfinite(price)) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        rooms.add(new Room(roomNumber, roomType, price));
        System.out.println("Room added successfully.");
    }

    private void displayRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms have been added.");
            return;
        }

        System.out.printf("%-10s %-18s %-16s %s%n",
                "Room No.", "Type", "Price/Night", "Status");
        for (Room room : rooms) {
            room.display();
        }
    }

    private void bookRoom() {
        int roomNumber = readInt("Enter room number to book: ");
        Room room = findRoom(roomNumber);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        try {
            if (room.isBooked()) {
                throw new RoomNotAvailableException(
                        "Room " + roomNumber + " is already booked.");
            }
            room.setBooked(true);
            System.out.println("Room " + roomNumber + " booked successfully.");
        } catch (RoomNotAvailableException exception) {
            System.out.println("Booking failed: " + exception.getMessage());
        }
    }

    private void checkOutRoom() {
        int roomNumber = readInt("Enter room number to check out: ");
        Room room = findRoom(roomNumber);
        if (room == null) {
            System.out.println("Room not found.");
        } else if (!room.isBooked()) {
            System.out.println("Room " + roomNumber + " is not currently booked.");
        } else {
            room.setBooked(false);
            System.out.println("Room " + roomNumber + " checked out successfully.");
        }
    }

    private Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return Integer.MIN_VALUE;
            }
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return -1;
            }
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input. Please enter a numeric price.");
            }
        }
    }

    private void loadRooms() {
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            Object storedRooms = input.readObject();
            if (!(storedRooms instanceof ArrayList<?>)) {
                throw new IOException("The saved data has an unexpected format.");
            }
            for (Object item : (ArrayList<?>) storedRooms) {
                if (!(item instanceof Room)) {
                    throw new IOException("The saved room list contains invalid data.");
                }
                rooms.add((Room) item);
            }
            System.out.println("Loaded " + rooms.size() + " room(s) from " + DATA_FILE + ".");
        } catch (java.io.FileNotFoundException exception) {
            System.out.println("No saved room list found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Could not load saved rooms: " + exception.getMessage());
            rooms.clear();
        }
    }

    private void saveRooms() {
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            output.writeObject(rooms);
            System.out.println("Saved " + rooms.size() + " room(s) to " + DATA_FILE + ".");
        } catch (IOException exception) {
            System.out.println("Could not save rooms: " + exception.getMessage());
        }
    }
}
