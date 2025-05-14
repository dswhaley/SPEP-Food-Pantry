import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TestAddNeighbor {
    static String url  = "jdbc:postgresql://localhost:5432/SPEP";
    static String username = "postgres";
    static String password = "foodpantry";


    @BeforeAll
    public static void dropTestNeighbor(){
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "DELETE FROM neighbor WHERE firstname = 'test'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }
    @Test
    public void addWithoutAddress(){
        AddToDB adb = new AddToDB();
        assertTrue(adb.addNeighbor("111", "test", "normal", "email", "2025:05:13", null
                , 0, 0, 0, 0, 0, "notes", null, null, null));
        assertTrue(compareNeighborToDatabase(adb.getNID("test", "normal", "111"), "111", "test", "normal", "email", "2025:05:13", null
                , 0, 0, 0, 0, 0, "notes"));

    }

    @Test
    public void addWithAddress(){
        AddToDB adb = new AddToDB();
        assertTrue(adb.addNeighbor("111", "test", "address", "email", "2025:05:13", null
                , 0, 0, 0, 0, 0, "notes", "streetAddress", "city", "zipcode"));
        assertTrue(compareAddressToDatabase(adb.getNID("test", "address", "111"), "streetAddress", "city", "zipcode"));
        assertTrue(compareNeighborToDatabase(adb.getNID("test", "address", "111"), "111", "test", "address", "email", "2025:05:13", null
                , 0, 0, 0, 0, 0, "notes"));

    }

    @Test
    public void addNullCreationDate(){
        AddToDB adb = new AddToDB();
        assertTrue(adb.addNeighbor("112", "test", "creationDate", "email", null, null
                , 0, 0, 0, 0, 0, "notes", null, null, null));
        // Check if the creation date is set to today's date
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        String creationDate = getCreationDate(adb.getNID("test", "creationDate", "112"));

        assertEquals(today, creationDate);
        assertTrue(compareNeighborToDatabase(adb.getNID("test", "creationDate", "112"),"112", "test", "creationDate", "email", today, null
                , 0, 0, 0, 0, 0, "notes"));
    }

    public String getCreationDate(int NID){
        String sql = "SELECT creationdate FROM neighbor WHERE NID = ?";
        try(Connection conn = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, NID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("creationdate");
            }
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
        }

        return null;
    }



    /**
     * Checks if a Neighbor record with the specified attributes exists in the database and matches exactly.
     * @param phoneNumber The neighbor's phone number
     * @param firstname The neighbor's first name
     * @param lastname The neighbor's last name
     * @param email The neighbor's email
     * @param creationDate The creation date (as YYYY-MM-DD string)
     * @param lastVisit The last visit date (as YYYY-MM-DD string)
     * @param flag The flag value
     * @param numberOfPeople The number of people
     * @param numSeniors The number of seniors
     * @param numAdults The number of adults
     * @param numChildren The number of children
     * @param notes The notes
     * @return true if exactly one matching Neighbor record is found, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean compareNeighborToDatabase(int NID, String phoneNumber, String firstname, String lastname,
                                             String email, String creationDate, String lastVisit, int flag,
                                             int numberOfPeople, int numSeniors, int numAdults, int numChildren, String notes) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM neighbor WHERE NID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, NID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (!Objects.equals(phoneNumber, rs.getString("phonenumber"))) {
                    System.out.println("Phone number does not match");
                    return false;
                }
                if (!Objects.equals(firstname, rs.getString("firstname"))) {
                    System.out.println("First name does not match");
                    return false;
                }
                if (!Objects.equals(lastname, rs.getString("lastname"))) {
                    System.out.println("Last name does not match");
                    return false;
                }
                if (!Objects.equals(email, rs.getString("email"))) {
                    System.out.println("Email does not match");
                    return false;
                }

                // Format DB date to the same format you use
                String dbCreationDate = rs.getString("creationdate");
                if (!Objects.equals(creationDate, dbCreationDate)) {
                    System.out.println("Creation date does not match: expected " + creationDate + ", got " + dbCreationDate);
                    return false;
                }

                String dbLastVisit = rs.getDate("lastvisit") != null
                        ? rs.getDate("lastvisit").toLocalDate().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"))
                        : null;
                if (!Objects.equals(lastVisit, dbLastVisit)) {
                    System.out.println("Last visit does not match");
                    return false;
                }

                if (flag != rs.getInt("flag")) {
                    System.out.println("Flag does not match");
                    return false;
                }
                if (numberOfPeople != rs.getInt("numberofpeople")) {
                    System.out.println("Number of people does not match");
                    return false;
                }
                if (numSeniors != rs.getInt("numseniors")) {
                    System.out.println("Number of seniors does not match");
                    return false;
                }
                if (numAdults != rs.getInt("numadults")) {
                    System.out.println("Number of adults does not match");
                    return false;
                }
                if (numChildren != rs.getInt("numchildren")) {
                    System.out.println("Number of children does not match");
                    return false;
                }

                if (!Objects.equals(notes, rs.getString("notes"))) {
                    System.out.println("Notes do not match");
                    return false;
                }

                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
            return false;
        }
    }



    public boolean compareAddressToDatabase(int NID, String streetAddress, String town, String zipcode){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM address WHERE nid = ? AND town_city = ? AND streetaddress = ? AND zipcode = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, NID);
                ps.setString(2, town.strip());
                ps.setString(3, streetAddress.strip());
                ps.setString(4, zipcode.strip());

                ResultSet rs = ps.executeQuery();
                // Check if there’s at least one row
                if (rs.next()) {

                    // Since the query filters all fields, a single row match is sufficient
                    if(NID == rs.getInt("nid") && town.equals(rs.getString("town_city")) && streetAddress.equals(rs.getString("streetaddress")) && zipcode.equals(rs.getString("zipcode"))){
                        return true; // All fields match
                    } else {
                        System.out.println("Address does not match");
                    }

                }

                return false;
        } catch (SQLException e){
            System.out.println("Failed to connect to database: " + e.getMessage());
            return false; // Database error
        }
    }
}
