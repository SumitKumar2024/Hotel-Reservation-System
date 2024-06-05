import dao.DatabaseConnection;
import service.ReservationService;
import utillity.Utils;

import java.sql.SQLException;
import java.util.Scanner;

public class HotelReservationSystem {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DatabaseConnection dbConnection = new DatabaseConnection();
        ReservationService reservationService = new ReservationService(dbConnection.getConnection());

        while (true) {
            System.out.println();
            System.out.println("HOTEL MANAGEMENT SYSTEM");
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. Reserve a room");
            System.out.println("2. View Reservations");
            System.out.println("3. Get Room Number");
            System.out.println("4. Update Reservations");
            System.out.println("5. Delete Reservations");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    reservationService.reserveRoom(scanner);
                    break;
                case 2:
                    reservationService.viewReservations();
                    break;
                case 3:
                    reservationService.getRoomNumber(scanner);
                    break;
                case 4:
                    reservationService.updateReservation(scanner);
                    break;
                case 5:
                    reservationService.deleteReservation(scanner);
                    break;
                case 0:
                    try{
                    Utils.exit();}
                    catch (InterruptedException e){
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    System.out.println("Thread was interrupted during exit.");
                    scanner.close();}
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
