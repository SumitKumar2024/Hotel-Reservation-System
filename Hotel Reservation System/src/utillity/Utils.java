package utillity;
import dao.DatabaseConnection;
import service.ReservationService;

public class Utils {

    public static void exit () throws InterruptedException {
            System.out.print("Exiting System");
            int i = 5;
            while (i != 0) {
                System.out.print(".");
                Thread.sleep(450);
                i--;
            }
            System.out.println();
            System.out.println("Thank You For Using Hotel Reservation System!!!");
        }
    }

