import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;

public class CheckIn extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

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
		
		setTitle("Check-In");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(10, 227, 89, 23);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 dispose();
	        	 Home h = new Home();
	        	 h.setVisible(true);
	          }
	       });
		
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(10, 11, 89, 23);
		contentPane.add(btnSearch);
		
		textField = new JTextField();
		textField.setBounds(97, 12, 327, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JList list = new JList();
		list.setBounds(10, 45, 414, 173);
		contentPane.add(list);
	}

}
