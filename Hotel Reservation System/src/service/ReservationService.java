package service;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReservationService {
    private Connection connection;

    public ReservationService(Connection connection) {
        this.connection = connection;
    }

    public void reserveRoom(Scanner scanner) {
        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.print("Enter email name: ");
            String guestEmail = scanner.next();
            scanner.nextLine();
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();
            scanner.nextLine();
            System.out.print("Enter room number: ");
            String roomNumber = scanner.next();
            scanner.nextLine();
            System.out.print("Enter room type: ");
            String roomType = scanner.next();
            scanner.nextLine();
            System.out.print("Enter room price: ");
            String roomPrice = scanner.next();
            scanner.nextLine();

            String sql = "INSERT INTO reservations (guest_name, guest_email, contact_number, room_number, room_type, room_price) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, guestName);
                statement.setString(2, guestEmail);
                statement.setString(3, contactNumber);
                statement.setString(4, roomNumber);
                statement.setString(5, roomType);
                statement.setString(6, roomPrice);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Reservation successful!");
                } else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewReservations() throws SQLException {
        String sql = "SELECT reservation_id, guest_name, guest_email, contact_number, room_number, room_type, room_price, reservation_date FROM reservations";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+------------+----------------------+-----------------+---------------+------------+-------------+-----------------------+");
            System.out.println("| Reservation ID | Guest Name | Email                | Contact Number  | Room Number   | Room Type  | Room Price  | Reservation Date      |");
            System.out.println("+----------------+------------+----------------------+-----------------+---------------+------------+-------------+-----------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                String guestEmail = resultSet.getString("guest_email");
                String contactNumber = resultSet.getString("contact_number");
                String roomNumber = resultSet.getString("room_number");
                String roomType = resultSet.getString("room_type");
                String roomPrice = resultSet.getString("room_price");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-10s | %-20s | %-15s | %-13s | %-11s | %-11s | %-18s |\n",
                        reservationId, guestName, guestEmail, contactNumber, roomNumber, roomType, roomPrice, reservationDate);
            }

            System.out.println("+----------------+------------+----------------------+-----------------+---------------+------------+-------------+-----------------------+");
        }
    }

    public void getRoomNumber(Scanner scanner) {
        try {
            System.out.print("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            String sql = "SELECT room_number FROM reservations WHERE reservation_id = ? AND guest_name = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);
                statement.setString(2, guestName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int roomNumber = resultSet.getInt("room_number");
                        System.out.println("Reservation ID:" + reservationId +
                                "\nGuest Name is " + guestName + "\nand Room Number is:" + roomNumber);
                    } else {
                        System.out.println("Reservation not found for the given ID and guest name.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReservation(Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!reservationExists(reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new guest email: ");
            String newGuestEmail = scanner.nextLine();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            System.out.print("Enter new room type: ");
            String newRoomType = scanner.nextLine();
            System.out.print("Enter new room price: ");
            int newRoomPrice = scanner.nextInt();

            String sql = "UPDATE reservations SET guest_name = ?, guest_email = ?, contact_number = ?, room_number = ?, room_type = ?, room_price = ? WHERE reservation_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newGuestName);
                statement.setString(2, newGuestEmail);
                statement.setString(3, newContactNumber);
                statement.setInt(4, newRoomNumber);
                statement.setString(5, newRoomType);
                statement.setInt(6, newRoomPrice);
                statement.setInt(7, reservationId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean reservationExists(int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // If there's a result, the reservation exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }
}
