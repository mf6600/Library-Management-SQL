import com.mysql.cj.jdbc.MysqlDataSource;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JList;

import java.io.IOException;
import java.util.*;
import java.sql.*;


public class Search extends JFrame {

	private JPanel contentPane;
	private Vector<String> searchList = null;
	
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Search(String entry) throws SQLException, IOException {
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





		Vector<String> titles = new Vector<String>();



		databaseController db = new databaseController();
		titles = db.executeBookSearch("SELECT Titles FROM sys.BOOK");


		JList list = new JList(titles);
		list.setBounds(20, 45, 404, 205);
		contentPane.add(list);


	}

}
