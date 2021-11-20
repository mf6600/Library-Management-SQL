import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JList;


public class Search extends JFrame {

	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Search(String entry) {
		setTitle("Search");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnHome = new JButton("Home");
		btnHome.setBounds(10, 11, 89, 23);
		contentPane.add(btnHome);
		btnHome.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 dispose();
	        	 Home h = new Home();
	        	 h.setVisible(true);
	          }
	       });
		
		JList list = new JList();
		list.setBounds(20, 45, 404, 205);
		contentPane.add(list);
	}

}
