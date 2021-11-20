import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class CreateBorrower extends JFrame {

	private JPanel contentPane;
	private JTextField textSSN;
	private JTextField textFirstName;
	private JTextField textLastName;
	private JTextField textEmail;
	private JTextField textAddress;
	private JTextField textCity;
	private JTextField textState;
	private JTextField textZip;
	private JTextField textPhone;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public CreateBorrower() {
		setTitle("Create Borrower");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSSN = new JLabel("SSN");
		lblSSN.setBounds(34, 11, 46, 14);
		contentPane.add(lblSSN);
		
		textSSN = new JTextField();
		textSSN.setBounds(99, 8, 313, 20);
		contentPane.add(textSSN);
		textSSN.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(34, 39, 69, 14);
		contentPane.add(lblFirstName);
		
		textFirstName = new JTextField();
		textFirstName.setBounds(92, 36, 86, 20);
		contentPane.add(textFirstName);
		textFirstName.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(188, 42, 86, 14);
		contentPane.add(lblLastName);
		
		textLastName = new JTextField();
		textLastName.setBounds(305, 42, 107, 20);
		contentPane.add(textLastName);
		textLastName.setColumns(10);
		
		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setBounds(34, 76, 46, 14);
		contentPane.add(lblEmail);
		
		textEmail = new JTextField();
		textEmail.setBounds(111, 73, 301, 20);
		contentPane.add(textEmail);
		textEmail.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(34, 104, 46, 14);
		contentPane.add(lblAddress);
		
		textAddress = new JTextField();
		textAddress.setBounds(111, 101, 301, 20);
		contentPane.add(textAddress);
		textAddress.setColumns(10);
		
		JLabel lblCity = new JLabel("City");
		lblCity.setBounds(34, 143, 46, 14);
		contentPane.add(lblCity);
		
		textCity = new JTextField();
		textCity.setBounds(62, 140, 86, 20);
		contentPane.add(textCity);
		textCity.setColumns(10);
		
		JLabel lblState = new JLabel("State");
		lblState.setBounds(158, 143, 46, 14);
		contentPane.add(lblState);
		
		textState = new JTextField();
		textState.setBounds(188, 140, 86, 20);
		contentPane.add(textState);
		textState.setColumns(10);
		
		JLabel lblZip = new JLabel("Zip");
		lblZip.setBounds(281, 143, 46, 14);
		contentPane.add(lblZip);
		
		textZip = new JTextField();
		textZip.setBounds(326, 140, 86, 20);
		contentPane.add(textZip);
		textZip.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone Number");
		lblPhone.setBounds(34, 183, 86, 14);
		contentPane.add(lblPhone);
		
		textPhone = new JTextField();
		textPhone.setBounds(130, 180, 144, 20);
		contentPane.add(textPhone);
		textPhone.setColumns(10);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(89, 212, 89, 23);
		contentPane.add(btnCreate);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(textZip.getText().isEmpty() || textState.getText().isEmpty() || textCity.getText().isEmpty() || textAddress.getText().isEmpty() || textPhone.getText().isEmpty() || textEmail.getText().isEmpty() || textLastName.getText().isEmpty() || textFirstName.getText().isEmpty() || textSSN.getText().isEmpty())) {
					// enter textfields in to new borrower object
					dispose();
					Home h = new Home();
		        	h.setVisible(true);
				}
			}
		});
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(254, 212, 89, 23);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 dispose();
	        	 Home h = new Home();
	        	 h.setVisible(true);
	          }
	       });
	}
}
