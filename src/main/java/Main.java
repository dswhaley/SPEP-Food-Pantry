import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
      System.out.println("Welcome to SPEP Food Pantry Database!");
      AddToDB adb = new AddToDB();
      while(true){
            System.out.println("Please select an option:");
            System.out.println("1. Add a neighbor");
            System.out.println("2. Remove a neighbor");
            System.out.println("3. Search for a neighbor");
            System.out.println("4. Exit");
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                // Call method to add a neighbor
                System.out.println("Enter the neighbor's phonenumber:");
                String phonenumber = scanner.next();
                System.out.println("Enter the neighbor's firstname:");
                String firstname = scanner.next();
                System.out.println("Enter the neighbor's lastname:");
                String lastname = scanner.next();
                System.out.println("Enter the neighbor's email:");
                String email = scanner.next();
                System.out.println("Enter the neighbor's numpeople in family:");
                int numpeople = scanner.nextInt();
                System.out.println("Enter the neighbor's num seniors in family:");
                int numseniors = scanner.nextInt();
                System.out.println("Enter the neighbor's num adults in family:");
                int numadults = scanner.nextInt();
                System.out.println("Enter the neighbor's num children in family:");
                int numchildren = scanner.nextInt();
                System.out.println("Enter the neighbor's street address:");
                String streetaddress = scanner.next();
                System.out.println("Enter the neighbor's city:");
                String city = scanner.next();
                System.out.println("Enter the neighbor's zipcode:");
                String zipcode = scanner.next();
                adb.addNeighbor(phonenumber, firstname, lastname, email,
                                 null, null, 0, numpeople, numseniors, numadults, numchildren,
                                 null, streetaddress, city, zipcode);
                System.out.println("Successfully added the neighbor to the database.");
                break;
                case 2:
                   System.out.println("Enter the neighbors firstName");
                   firstname = scanner.next();
                   System.out.println("Enter the neighbors lastName");
                   lastname = scanner.next();
                   System.out.println("Enter the neighbors phoneNumber");
                   phonenumber = scanner.next();

                   adb.removeNeighbor(adb.getNID(firstname, lastname, phonenumber));
                break;
                case 3:
                // Call method to search for a neighbor
                break;
                case 4:
                System.out.println("Exiting...");
                return;
                default:
                System.out.println("Invalid choice, please try again.");
            }
      }
   }
}
