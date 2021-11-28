import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.sql.*;
import java.sql.DriverManager;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //ADD BORROWER FUNCTION -- COMPLETED
    public static void addBorrower(String ssn, String firstName, String lastName, String email, String address, String city, String state, String phone) {

        String currentID = "";
        String newID = "";

        try {
            Statement stmt  = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM sys.BORROWER WHERE Ssn in ('"+ssn+"')");

            if (rs1.next()) {
                JOptionPane.showMessageDialog(null, "The borrower already exists in the system");
            }
            else {
                Statement stmt2  = con.createStatement();
                ResultSet rs = stmt2.executeQuery("SELECT MAX(Card_id) FROM sys.BORROWER"); //RETURNS ID001000
                if (rs.next()) {
                    currentID = rs.getString(1);
                }

                String temp = currentID.substring(4, currentID.length());
                int tempInt = Integer.parseInt(temp);
                tempInt++;
                newID = "ID00" + tempInt;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Statement stmt  = con.createStatement();
            stmt.executeUpdate("INSERT INTO sys.BORROWER (Card_id, Ssn, firstName, lastName, email, address, city, state, phone) VALUE ('"+newID+"','"+ssn+"','"+firstName+"','"+lastName+"','"+email+"','"+address+"','"+city+"','"+state+"','"+phone+"')");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //ADD YOU FUNCTIONS AND MAKE A CALL OF THIS IN THE CLASS YOU ARE IN
    //DOING THIS MAKES THE CODE LESS MESSY

}

















