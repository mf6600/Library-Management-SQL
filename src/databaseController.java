import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
















