import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.sql.DriverManager;
import java.util.*;

public class databaseController {


    private static Connection con = null;

    //CONSTRUCTOR
    public databaseController() {
        executeController();
    }

    //CONNECTS TO DATABASE
    public static void executeController() {

        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("admin");
            dataSource.setPassword("groupllama");
            dataSource.setServerName("library-management.cupcvhuzco7w.us-east-2.rds.amazonaws.com");

            con = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    //SEARCH BOOK FUNCTION -- YET TO BE COMPLETED
    public static Vector<String> executeBookSearch(String command) {
        Vector<String> books = new Vector<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Title FROM sys.BOOK");

            while (rs.next()) {
                System.out.println(rs.getString("Title"));
                books.add(rs.getString("Title"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    public static void addBorrower(int ssn, String firstName, String lastName, String email, String address, String city, String state, String phone) {

        String card_id = ID10;
        String newId = null;

        try {
            Statement stmt  = con.createStatement();
            stmt.executeUpdate("INSERT INTO sys.BORROWER (Card_id, Ssn, Bname, Address, Phone) VALUE ('"+newId+"','"+ssn+"','"+firstName+"','"+lastName+"','"+email+"','"+address+"','"+city+"','"+state+"','"+phone+"')");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //ADD YOU FUNCTIONS AND MAKE A CALL OF THIS IN THE CLASS YOU ARE IN
    //DOING THIS MAKES THE CODE LESS MESSY

}

















