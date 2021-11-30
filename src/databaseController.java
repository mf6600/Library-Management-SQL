import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.mysql.cj.jdbc.MysqlDataSource;

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
    
    public static void checkOut(String isbn, String cardId) {
    	
    
    	try{
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 14);
			String dateDue = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
			
			//System.out.println(date + " " + dueDate);
	        
			Statement stmt1= con.createStatement();
			ResultSet rs1 = stmt1.executeQuery("select * from sys.BOOKS where Isbn10='"+isbn+"';");
			Statement stmt2 = con.createStatement();
			ResultSet rs2 = stmt2.executeQuery("select * from sys.BORROWER where Card_id='"+cardId+"';");

			if(rs1.next()&&rs2.next())
			{
				Statement stmt3 = con.createStatement();
				ResultSet rs3 = stmt3.executeQuery("select * from sys.BOOK_LOANS where Isbn='"+isbn+"' and Date_in is null;");
				

				if(rs3.next())
				{
					JOptionPane.showMessageDialog(null, "This book has already been issued");	
					
				}
				else
				{
					Statement stmt4 = con.createStatement();
					ResultSet rs4 = stmt4.executeQuery("select * from sys.BOOK_LOANS where Card_id='"+cardId+"' and Date_in is null and Due_date<CAST(CURRENT_TIMESTAMP AS DATE);");
					if(rs4.next())
					{
						JOptionPane.showMessageDialog(null, "The borrower already has an overdue book");	
						
					}
					else
					{
						if(maxBookLoans(cardId)){
							JOptionPane.showMessageDialog(null, "The borrower already has 3 active book loans");
							
						}
						else
						{
							if(checkFines(cardId)){
								JOptionPane.showMessageDialog(null, "The borrower has to pay some fine");
							
							}
							else{
								createBookLoan(isbn, cardId, date, dateDue);
								JOptionPane.showMessageDialog(null, "New Book Loan Created");
						
							}
						}
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Invalid Isbn or Card Id");	
				
			}
    	} catch(SQLException ex) {
    		System.out.println("Error in connection 2: " + ex.getMessage());
    	}
	
		}
    
    public static Boolean maxBookLoans(String cardId) throws SQLException {
    	Statement stmt5 = con.createStatement();
		ResultSet rs5 = stmt5.executeQuery("select count(*) from sys.BOOK_LOANS where Card_id='"+cardId+"' and Date_in is null;");
		rs5.next();
		if(rs5.getInt(1)>=3)
		{
			return true;
			
		}
		
		return false;
    }
    
    public static Boolean checkFines(String cardId) throws SQLException {
    	Statement stmt6 = con.createStatement();
		ResultSet rs6 = stmt6.executeQuery("select * from sys.BOOK_LOANS AS B, sys.FINES as F where Card_id='"+cardId+"' and B.Loan_id=F.Loan_id and paid=0;");	
		if(rs6.next())
		{
			return true;
		}
		
		return false;
    }
    
    public static void createBookLoan(String isbn, String cardId, String date, String dueDate) throws SQLException {
    	Statement stmt = con.createStatement();
		ResultSet rs= stmt.executeQuery("select max(Loan_id) from sys.BOOK_LOANS");
		int loan_id=0;
		if(rs.next())
		{				 	
			loan_id=rs.getInt(1)+1;
		}

		String sql="INSERT INTO sys.BOOK_LOANS (Loan_id, Isbn, Card_id, Date_out, Due_date) VALUES ("+loan_id+", '"+isbn+"', '"+cardId+"', '"+date+"', '"+dueDate+"');";
		Statement stmt7 = con.createStatement();
		stmt7.executeUpdate(sql);
    }
    
    public static void checkIn(int loan_id, String date) throws SQLException {
		Statement stmt1=con.createStatement();
		stmt1.executeUpdate("Update sys.BOOK_LOANS set Date_in='"+date+"' where Loan_id="+loan_id+";");
		
		Statement stmt2=con.createStatement();
		ResultSet rs= stmt2.executeQuery("Select * from sys.BOOK_LOANS where Loan_id="+loan_id+" and Due_date<Date_in;");
		
		if(rs.next()) {
			JOptionPane.showMessageDialog(null, "Book is overdue. There are fines.");
			//new Fines();
		}
		else {
        	JOptionPane.showMessageDialog(null, "Checked in. No fines applied");
        	new CheckIn();
        }
    }
    
    public static ResultSet checkInMethods(String isbn, String card, String borrower) throws SQLException {
    	Statement stmt=con.createStatement();
		ResultSet rs;
		
		if(card.equals("") && borrower.equals(""))//only isbn
		{
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn="+isbn+" and Date_in is null;");
		}
		else if(isbn.equals("") && borrower.equals(""))//only card_id
		{
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Card_id="+card+" and Date_in is null;");
		}
		else if(isbn.equals("") && card.equals(""))//only borrower name
		{
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Card_id in (Select Card_id from sys.borrower where Bname like '%"+borrower+"%') and Date_in is null;");
		}
		else if(borrower.equals(""))//isbn and card_id
		{
			
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn="+isbn+" and Card_id="+card+" and Date_in is null;");
			System.out.println(rs.getString("Card_id"));
			
		}
		else if(isbn.equals(""))//card_id and borrower name
		{
			rs=stmt.executeQuery("(Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Card_id="+card+" and Date_in is null)");
		}
		else if(card.equals(""))//isbn and borrower name
		{
			rs=stmt.executeQuery("(Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn="+isbn+" and Card_id in (Select Card_id from sys.borrower where Bname like '%"+borrower+"%') and Date_in is null)");
		}
		else//all 3 given
		{
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn="+isbn+" and Card_id="+card+" and Date_in is null;");
		}
		
		return rs;
    }






    //SEARCH BOOK FUNCTION -- YET TO BE COMPLETED
    public static Vector<String> executeBookSearch(String command) {
        Vector<String> books = new Vector<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Title FROM sys.BOOKS");

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


    //ADD YOU FUNCTIONS AND MAKE A CALL OF THIS IN THE CLASS YOU ARE IN
    //DOING THIS MAKES THE CODE LESS MESSY

}

















