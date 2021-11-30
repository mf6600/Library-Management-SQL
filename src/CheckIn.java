import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.sql.Connection;


public class CheckIn extends JFrame {

	private JFrame mainFrame;
	private JPanel contentPane;
	private Object Color;
	static Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckIn frame = new CheckIn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CheckIn() {
		
		JTextField tfText0;
		JTextField tfText1;
		JButton btBut0;
		JButton btBut1;
		JLabel lbLabel0;
		JLabel lbLabel1;
		
		setTitle("Check-In");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		contentPane = new JPanel();
		GridBagLayout gbPanel0 = new GridBagLayout();
		gbPanel0.columnWidths=new int[]{0,0};
		gbPanel0.rowHeights = new int[]{0, 0, 0,0,0,0};
		gbPanel0.columnWeights = new double[]{Double.MIN_VALUE, Double.MIN_VALUE};
		gbPanel0.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		contentPane.setLayout( gbPanel0 );
		
		tfText0 = new JTextField( );
		tfText0.setFont(new Font("Ariel",Font.PLAIN,14));
		GridBagConstraints gbc_isbnText=new GridBagConstraints();
		gbc_isbnText.fill=GridBagConstraints.HORIZONTAL;
		gbc_isbnText.insets=new Insets(0,0,5,0);
		gbc_isbnText.gridx=1;
		gbc_isbnText.gridy=2;
		//gbc_isbnText.gridwidth=2;
		contentPane.add(tfText0, gbc_isbnText);
		//controlPanel.add(searchText);
		tfText0.setColumns(15);

		   tfText1 = new JTextField( );
		   tfText0.setFont(new Font("Ariel",Font.PLAIN,14));
			GridBagConstraints gbc_CardNo=new GridBagConstraints();
			gbc_CardNo.fill=GridBagConstraints.HORIZONTAL;
			gbc_CardNo.insets=new Insets(0,0,5,0);
			gbc_CardNo.gridx=1;
			gbc_CardNo.gridy=2;
			//gbc_isbnText.gridwidth=2;
			contentPane.add(tfText0, gbc_CardNo);
			//controlPanel.add(searchText);
			tfText0.setColumns(15);

		 //Book ISBN
			JLabel lblIsbn=new JLabel("     ISBN* :",JLabel.LEFT);
			lblIsbn.setFont(new Font("Times New Roman",Font.BOLD,14));
			GridBagConstraints gbc_lblIsbn=new GridBagConstraints();
			gbc_lblIsbn.insets=new Insets(0,0,5,0);
			gbc_lblIsbn.gridx=0;
			gbc_lblIsbn.gridy=2;
			//gbc_lblIsbn.gridwidth=2;
			contentPane.add(lblIsbn, gbc_lblIsbn);
			
			
			//Borrower Card No.
			JLabel lblcard=new JLabel("Card No.* :",JLabel.LEFT);
			lblcard.setFont(new Font("Times New Roman",Font.BOLD,14));
			GridBagConstraints gbc_lblcard=new GridBagConstraints();
			gbc_lblcard.insets=new Insets(0,0,5,0);
			gbc_lblcard.gridx=0;
			gbc_lblcard.gridy=3;
			//gbc_lblIsbn.gridwidth=2;
			contentPane.add(lblcard, gbc_lblcard);
			
			//Card No.Text Box
			JTextField cardText = new JTextField();
			cardText.setFont(new Font("Ariel",Font.PLAIN,14));
			//cardText.setSize(200, 50);;
			GridBagConstraints gbc_cardText =new GridBagConstraints();
			gbc_cardText.fill=GridBagConstraints.HORIZONTAL;
			gbc_cardText.insets=new Insets(0,0,5,0);
			gbc_cardText.gridx=1;
			gbc_cardText.gridy=3;
			//gbc_isbnText.gridwidth=2;
			contentPane.add(cardText, gbc_cardText);
			//controlPanel.add(searchText);
			cardText.setColumns(15);
			
			//Borrower's Name
			JLabel borrowerLabel=new JLabel("Borrower's Name :",JLabel.LEFT);
			borrowerLabel.setFont(new Font("Times New Roman",Font.BOLD,14));
			GridBagConstraints gbcBorrowerLabel=new GridBagConstraints();
			gbcBorrowerLabel.insets=new Insets(0,0,5,0);
			gbcBorrowerLabel.gridx=0;
			gbcBorrowerLabel.gridy=4;
			//gbc_lblIsbn.gridwidth=2;
			contentPane.add(borrowerLabel, gbcBorrowerLabel);
			
			JTextField BorrowerText = new JTextField();
			cardText.setFont(new Font("Ariel",Font.PLAIN,14));
			//cardText.setSize(200, 50);;
			GridBagConstraints gbcBorrowerText =new GridBagConstraints();
			gbcBorrowerText.fill=GridBagConstraints.HORIZONTAL;
			gbcBorrowerText.insets=new Insets(0,0,5,0);
			gbcBorrowerText.gridx=1;
			gbcBorrowerText.gridy=4;
			//gbc_isbnText.gridwidth=2;
			contentPane.add(BorrowerText, gbcBorrowerText);
			//controlPanel.add(searchText);
			BorrowerText.setColumns(15);
			
			JButton checkIn=new JButton("Check In");

		   setDefaultCloseOperation( EXIT_ON_CLOSE );

		   setContentPane( contentPane );
		   pack();
		   setVisible( true );
		   
		   checkIn.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					if(tfText0.getText().equals("") && cardText.getText().equals("") && BorrowerText.getText().equals(""))
					{ 
						JOptionPane.showMessageDialog(null, "Please fill in the required fields");
					}
					else{
					
						try{
							
							new ViewCheckedInBooks(tfText0.getText(), cardText.getText(), BorrowerText.getText());
//							//System.out.println(date + " " + dueDate);
//					
//							String url = "library-management.cupcvhuzco7w.us-east-2.rds.amazonaws.com";
//							String user = "admin";
//							String password = "groupllama";
//			        
//							databaseController conn = new databaseController();
//							conn=DriverManager.getConnection(url, user, password);
//							Statement stmt1= conn.createStatement();
//							ResultSet rs1 = stmt1.executeQuery("select * from sys.BOOK where Isbn='"+isbn+"';");
//							Statement stmt2 = conn.createStatement();
//							ResultSet rs2 = stmt2.executeQuery("select * from sys.BORROWER where Card_id='"+cardId+"';");
//					
//							if(rs1.next()&&rs2.next())
//							{
//								Statement stmt3 = conn.createStatement();
//								ResultSet rs3 = stmt3.executeQuery("select * from sys.book_loans where Isbn='"+isbn+"' and Date_in is null;");
//								if(rs3.next())
//								{
//									JOptionPane.showMessageDialog(null, "This book has already been issued");	
//									tfText0.setText(""); 
//								}
//						else
//						{
//							Statement stmt4 = conn.createStatement();
//							ResultSet rs4 = stmt4.executeQuery("select * from sys.book_loans where Card_id='"+cardId+"' and Date_in is null and Due_date<CAST(CURRENT_TIMESTAMP AS DATE);");
//							if(rs4.next())
//							{
//								JOptionPane.showMessageDialog(null, "The borrower already has an overdue book");	
//								tfText0.setText(""); 
//								cardText.setText("");
//							}
//							else
//							{
//								Statement stmt5 = conn.createStatement();
//								ResultSet rs5 = stmt5.executeQuery("select count(*) from proj1.book_loans where Card_id='"+cardId+"' and Date_in is null;");
//								rs5.next();
//								if(rs5.getInt(1)>=3)
//								{
//									JOptionPane.showMessageDialog(null, "The borrower already has 3 active book loans");
//									tfText0.setText(""); 
//									cardText.setText("");
//								}
//								else
//								{
//									Statement stmt6 = conn.createStatement();
//									ResultSet rs6 = stmt6.executeQuery("select * from sys.book_loans b,fines f where Card_id="+cardId+" and b.Loan_id=f.Loan_id and paid=0;");	
//									if(rs6.next())
//									{
//										JOptionPane.showMessageDialog(null, "The borrower has to pay some fine");
//										tfText0.setText(""); 
//										cardText.setText("");
//									}
//									else{
//										Statement stmt = conn.createStatement();
//										ResultSet rs= stmt.executeQuery("select max(Loan_id) from book_loans");
//										int id=0;
//										if(rs.next())
//										{				 	
//										 id=rs.getInt(1)+1;
//										}
//										
//										String sql="INSERT INTO Book_loans (Loan_id, Isbn, Card_id, Date_out, Due_date) VALUES ("+id+", '"+isbn+"', "+cardId+", '"+date+"', '"+dueDate+"');";
//										Statement stmt7 = conn.createStatement();
//										stmt7.executeUpdate(sql);
//										JOptionPane.showMessageDialog(null, "Done");
//										tfText0.setText(""); 
//										cardText.setText("");
//									}
//								}
//							}
//						}
//					}
//					else
//					{
//						JOptionPane.showMessageDialog(null, "Invalid Isbn or Card Id");	
//						tfText0.setText("");
//						cardText.setText("");
//					}
							
					tfText0.setText("");
					cardText.setText("");
					
					
			    }
				catch(Exception ex) {
					System.out.println("Error in connection 1: " + ex.getMessage());
				}
				
				}
				}
			});
		   
		   
		   JLabel space2=new JLabel("  ",JLabel.CENTER);
			//searchLabel.setFont(new F);
			GridBagConstraints gbc_space2=new GridBagConstraints();
			gbc_space2.insets=new Insets(0,0,5,0);
			gbc_space2.gridx=0;
			gbc_space2.gridy=5;
			gbc_space2.gridwidth=2;
			contentPane.add(space2, gbc_space2);
			
		   GridBagConstraints gbc_btnCheckIn=new GridBagConstraints();
		   gbc_btnCheckIn.fill=GridBagConstraints.HORIZONTAL;
		   gbc_btnCheckIn.insets = new Insets(0, 0, 5, 0);
		   gbc_btnCheckIn.gridx = 0;
		   gbc_btnCheckIn.gridy = 6;
		   gbc_btnCheckIn.gridwidth=2;
			contentPane.add(checkIn,gbc_btnCheckIn);
			
			JLabel space3=new JLabel("  ",JLabel.CENTER);
			//searchLabel.setFont(new F);
			GridBagConstraints gbc_space3=new GridBagConstraints();
			gbc_space3.insets=new Insets(0,0,5,0);
			gbc_space3.gridx=0;
			gbc_space3.gridy=5;
			gbc_space3.gridwidth=2;
			contentPane.add(space3, gbc_space3);
			
			//Close Button
			JButton close=new JButton("Back");
			close.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					//mainFrame.setVisible(false);
					new GUI();
				}
			});
			GridBagConstraints gbc_btnClose=new GridBagConstraints();
			gbc_btnClose.fill=GridBagConstraints.HORIZONTAL;
			gbc_btnClose.insets = new Insets(0, 0, 5, 0);
			gbc_btnClose.gridx = 0;
			gbc_btnClose.gridy = 8;
			gbc_btnClose.anchor=GridBagConstraints.PAGE_END;
			gbc_btnClose.gridwidth=2;
			contentPane.add(close, gbc_btnClose);		

		}
	}


