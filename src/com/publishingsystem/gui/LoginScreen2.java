package com.publishingsystem.gui;
import java.awt.EventQueue;
import com.publishingsystem.mainclasses.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen2 {

	private JFrame frmLogInScreen;
	private JTextField emailField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen2 window = new LoginScreen2();
					window.frmLogInScreen.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginScreen2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogInScreen = new JFrame();
		frmLogInScreen.setTitle("Login");
		frmLogInScreen.setBounds(100, 100, 700, 500);
		frmLogInScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogInScreen.setVisible(true);

		JLabel lblEmail = new JLabel("Email Address:");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 15));

		emailField = new JTextField();
		emailField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		emailField.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			    // 1. Get email and password entered then validate
			    String email = emailField.getText();
			    String password = new String(passwordField.getPassword());

			    boolean validCredentials = true;
			    if (email.isEmpty() || password.isEmpty()) validCredentials = false;

			    if (validCredentials) {
			        // 2. Get stored hash and salt from database for given email
			        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/?user=team022&password=6b78cf2f")) {
			            Statement statement = con.createStatement();
			            statement.executeUpdate("USE team022");
			            ResultSet res = statement.executeQuery(
			                    "SELECT * FROM Academic WHERE emailAddress = '" + email + "'");

			            /*statement.executeUpdate("CREATE TABLE Academic ("
			                    + "academicID INT PRIMARY KEY AUTO_INCREMENT, "
			                    + "title TEXT, "
			                    + "forenames TEXT, "
			                    + "surname TEXT, "
			                    + "university TEXT, "
			                    + "emailAddress TEXT, "
			                    + "passwordHash TEXT, "
			                    + "salt VARBINARY(256))");*/

			            int dbAcademicID = 0;
			            String dbTitle = null;
			            String dbForenames = null;
			            String dbSurname = null;
			            String dbUniversity = null;
			            String dbEmailAddress = null;
			            String dbHash = null;
			            String dbSalt = null;
			            while (res.next()) {
			                dbAcademicID = res.getInt(1);
			                dbTitle = res.getString(2);
			                dbForenames = res.getString(3);
			                dbSurname = res.getString(4);
			                dbUniversity = res.getString(5);
			                dbEmailAddress = res.getString(6);
			                dbHash = res.getString(7);
			                dbSalt = res.getString(8);
			            }
			            System.out.println(dbAcademicID + ", " + dbHash + ", " + dbSalt);

			            //Academic loggedInUser = new Academic(dbAcademicID, dbTitle, dbForenames, dbSurname, dbEmailAddress, dbUniversity);
			            Author loggedInUser = new Author(dbAcademicID, dbTitle, dbForenames, dbSurname, dbEmailAddress, dbUniversity);
			            System.out.println(loggedInUser);

			            if (dbAcademicID == 0) {
			                JOptionPane.showMessageDialog(null, "Incorrect email or password", "Login", 0);
			            } else {
		                    // 3. Generate hash based on fetched salt and entered password
		                    Hash newHash = new Hash(password, dbSalt);
		                    boolean correctPassword = newHash.getHash().equals(dbHash);

		                    // 4. Check if this hash is same as stored hash
		                    if (correctPassword) {
		                        JOptionPane.showMessageDialog(null, "Login Successful", "Login", 1);
		                        new AuthorMainWindow(loggedInUser);
		                        frmLogInScreen.dispose();
		                    } else JOptionPane.showMessageDialog(null, "Incorrect email or password", "Login", 0);
			            }
			        } catch (SQLException ex) {ex.printStackTrace();}
			    } else JOptionPane.showMessageDialog(null, "Please fill in all fields", "Login", 0);

			    // 4. Clear password variables
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblWelcomeBack = new JLabel("Welcome Back!");
		lblWelcomeBack.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeBack.setFont(new Font("Tahoma", Font.PLAIN, 25));

		JButton btnLoginAsA = new JButton("Login as a Reader");
		btnLoginAsA.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				new JournalWindow();
				frmLogInScreen.dispose();
			}
		});
		btnLoginAsA.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnAcademicRegister = new JButton("Register as an Academic");
		btnAcademicRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				new RegistrationWindow();
			}
		});
		btnAcademicRegister.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout groupLayout = new GroupLayout(frmLogInScreen.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(43)
					.addComponent(lblWelcomeBack, GroupLayout.PREFERRED_SIZE, 643, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(100)
					.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(100)
					.addComponent(emailField, GroupLayout.PREFERRED_SIZE, 529, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(100)
					.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(100)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 529, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(300)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(290)
					.addComponent(btnLoginAsA))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(480)
					.addComponent(btnAcademicRegister, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
					.addGap(20))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(lblWelcomeBack, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(lblEmail)
					.addGap(5)
					.addComponent(emailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(lblPassword)
					.addGap(5)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(btnLoginAsA, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
					.addComponent(btnAcademicRegister, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
		frmLogInScreen.getContentPane().setLayout(groupLayout);
	}
}