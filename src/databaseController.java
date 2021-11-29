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
    //TAKES IN THE SEARCH AND RETURNS A VECTOR WITH SEARCH RESULT
    public static Vector<String> executeBookSearch(String bookEntry) {
        Vector<String> books = new Vector<>();

        try {
            Statement stmt = con.createStatement();

            //HAVE TO RUN THE CORRECT SQL QUERY TO OUTPUT THE BOOK CONTENTS
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
    
    // Update/Refresh Entries in FINES table
    public static void update() {
		try {
			Statement stmt = con.createStatement();
			
			// Increment existing fines by $0.25 if Paid == false
			String sql1 = "UPDATE sys.FINES "
					+ "SET Fine_amt = Fine_amt + 0.25 "
					+ "WHERE Paid = false; ";
			stmt.executeUpdate(sql1);
			
			// If late book has been returned, insert into FINES table with Fine_amt value based on date in - due date
			String sql2 = "INSERT IGNORE INTO sys.FINES "
					+ "SELECT bl.Loan_id, DATEDIFF(bl.Date_in, bl.Due_date)*0.25, false "
					+ "FROM sys.BOOK_LOANS AS bl "
					+ "WHERE Date_in IS NOT NULL AND (SELECT DATEDIFF(Date_in, Due_date)) > 0; ";
			stmt.executeUpdate(sql2);
			
			// If late book is still out, insert into FINES table with Fine_amt value based on current date - due date
			String sql3 = "INSERT IGNORE INTO sys.FINES "
					+ "SELECT bl.Loan_id, DATEDIFF(CURDATE(), bl.Due_date)*0.25, false "
					+ "FROM sys.BOOK_LOANS AS bl "
					+ "WHERE Date_in IS NULL AND (SELECT DATEDIFF(CURDATE(), Due_date)) > 0; ";
			stmt.executeUpdate(sql3);
			
			// After each update, filter out previously paid fines
			String sql4 = "DELETE FROM sys.FINES WHERE Paid = true; ";
			stmt.executeUpdate(sql4);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    // Mechanism to enter fine payments (using textfields + button)
    public static void enterPayment(String Loan_id, float amount) {
    	try {
    		Statement stmt = con.createStatement();
    		
    		// Update Fine_amt of loan_id entered by user by amount entered by user only if the book has been returned
    		String sql1 = "UPDATE sys.FINES AS fi SET Fine_amt = Fine_amt - '"+amount+"' "
    				+ "WHERE fi.Loan_id = '"+Loan_id+"' AND fi.Loan_id = ( "
    				+ "SELECT Loan_id "
    				+ "FROM sys.BOOK_LOANS AS bl "
    				+ "WHERE Date_in IS NOT NULL AND bl.Loan_id = fi.Loan_id);";
    		stmt.executeUpdate(sql1);
    		
    		// Update Paid to true if book loan has been paid off
    		String sql2 = "UPDATE sys.FINES "
    				+ "SET Paid = true "
    				+ "WHERE Fine_amt <= 0;";
    		stmt.executeUpdate(sql2);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    

    //ADD YOU FUNCTIONS AND MAKE A CALL OF THIS IN THE CLASS YOU ARE IN
    //DOING THIS MAKES THE CODE LESS MESSY

}

















