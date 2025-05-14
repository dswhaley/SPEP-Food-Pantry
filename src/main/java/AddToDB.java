import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddToDB {
    String url  = "jdbc:postgresql://localhost:5432/SPEP";
    String username = "postgres";
    String password = "foodpantry";
    public AddToDB(){
    }

    public boolean addNeighbor(String phoneNumber, String firstname, String lastname,
                               String email, String creationDate, String lastVisit, int flag,
                               int numberOfPeople, int numSeniors, int numAdults, int numChildren, String notes,
                               String streetAddress, String city, String zipcode) {

        if(creationDate == null){
            LocalDate today = LocalDate.now();
            creationDate = today.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "INSERT INTO neighbor (phonenumber, firstname, lastname, email, creationdate, lastvisit, flag, numberofpeople, numseniors, numadults, numchildren, notes) " +
                    "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber.strip());
            ps.setString(2, firstname.strip());
            ps.setString(3, lastname.strip());
            ps.setString(4, email.strip());
            ps.setString(5, safeStrip(creationDate));
            ps.setString(6, safeStrip(lastVisit));
            ps.setInt(7, flag);
            ps.setInt(8, numberOfPeople);
            ps.setInt(9, numSeniors);
            ps.setInt(10, numAdults);
            ps.setInt(11, numChildren);
            ps.setString(12, safeStrip(notes));
            int rowsAffected = ps.executeUpdate();

            if(rowsAffected != 1){
                System.out.println("Failed to add neighbor to the database.");
                return false;
            }

            int NID = getNID(firstname, lastname, phoneNumber);

            if(NID == -1){
                System.out.println("Failed to retrieve NID for the newly added neighbor.");
                return false;
            }

            if(streetAddress != null && city != null && zipcode != null){
                addAddressToDB(NID, streetAddress, city, zipcode);
            }

            return true;
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }

        return true; // Return true if the addition was successful
    }

    public int getNID(String firstName, String lastName, String phoneNumber){
        String sql = "SELECT NID FROM neighbor WHERE firstname = ? AND lastname = ? AND phonenumber = ?";
        try(Connection conn = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, firstName.strip());
            ps.setString(2, lastName.strip());
            ps.setString(3, phoneNumber.strip());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("NID");
            } else {
                System.out.println("No NID found for the given neighbor.");
            }
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return -1;
    }

    public boolean addAddressToDB(int NID, String streetAddress, String city, String zipcode){
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "INSERT INTO address (nid, town_city, streetaddress, zipcode) " +
                    "VALUES( ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, NID);
            ps.setString(2, safeStrip(city));
            ps.setString(3, safeStrip(streetAddress));
            ps.setString(4, safeStrip(zipcode));
            int rowsAffected = ps.executeUpdate(); // Execute the insert statement
            if(rowsAffected == 1){
                return true;
            } else {
                System.out.println("Failed to add address to the database.");
                return false;
            }

        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }

        return false;
    }

    public String safeStrip(String s){
        if(s == null){
            return null;
        }
        return s.strip();
    }

    public boolean removeNeighbor(int NID){
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "DELETE FROM neighbor WHERE NID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, NID);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 1){;
                System.out.println("Failed to remove neighbor from the database.");
                return false;
            }

            sql = "DELETE FROM address WHERE NID = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, NID);
            rowsAffected = ps.executeUpdate();
            if(rowsAffected == 1){
                return true;
            } else {
                System.out.println("Failed to remove address from the database.");
                return false;
            }
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return false;
    }

    public boolean setFlag(int NID, int flag){
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "UPDATE neighbor SET flag = ? WHERE NID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, flag);
            ps.setInt(2, NID);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 1){
                return true;
            } else {
                System.out.println("Failed to update flag in the database.");
                return false;
            }
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return false;
    }

    public boolean setNote(int NID, String note){
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "UPDATE neighbor SET notes = ? WHERE NID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, note);
            ps.setInt(2, NID);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 1){
                return true;
            } else {
                System.out.println("Failed to update note in the database.");
                return false;
            }
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return false;
    }
}

