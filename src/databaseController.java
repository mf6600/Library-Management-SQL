import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class databaseController {


    private static Connection con = null;

    public static JFrame frame_two = new JFrame("Results");
    public static JTable table;
    public static DefaultTableModel defaultTableModel;

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
    public static void executeBookSearch(String bookEntry) {
        frame_two.setLayout(new FlowLayout());
        frame_two.setSize(1000, 1000);

        //setting the properties of jtable and defaulttablemodel
        defaultTableModel = new DefaultTableModel();
        table = new JTable(defaultTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 100));
        table.setFillsViewportHeight(true);
        frame_two.add(new JScrollPane(table));
        defaultTableModel.addColumn("ISBN10");
        defaultTableModel.addColumn("TITLE");
        defaultTableModel.addColumn("AUTHOR(S)");

        JButton btnHome = new JButton("Home");
        btnHome.setBounds(10, 11, 89, 23);
        frame_two.add(btnHome);
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame_two.dispose();
                Home h = new Home();
                h.setVisible(true);
            }
        });

        try {
            String sql = "SELECT * FROM sys.BOOKS WHERE title LIKE '%" + bookEntry + " %';";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String isbn10 = rs.getString("ISBN10");
                String title = rs.getString("Title");
                String authors = rs.getString("Author");
                defaultTableModel.addRow(new Object[] {isbn10,title, authors});
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment( JLabel.CENTER );
                table.setDefaultRenderer(String.class, centerRenderer);
                frame_two.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame_two.setVisible(true);
                frame_two.validate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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

















