import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
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

public class LoginScreen {

	private JFrame frame;
	private JTextField usernameField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen window = new LoginScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 548, 370);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		usernameField = new JTextField();
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		usernameField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblWelcomeBack = new JLabel("Welcome Back!");
		lblWelcomeBack.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeBack.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		JButton btnLoginAsA = new JButton("Login as a Reader");
		btnLoginAsA.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnChiefEditor = new JButton("Chief-Editor Registration");
		btnChiefEditor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(82)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(usernameField, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
						.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPassword, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
							.addGap(214))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblUsername, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
							.addGap(208)))
					.addGap(82))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(43)
					.addComponent(lblWelcomeBack, GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
					.addGap(43))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(179)
					.addComponent(btnLogin, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
					.addGap(179))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(157)
					.addComponent(btnLoginAsA, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
					.addGap(157))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(433, Short.MAX_VALUE)
					.addComponent(btnChiefEditor)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(lblWelcomeBack, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(lblUsername)
					.addGap(5)
					.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(lblPassword)
					.addGap(5)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(btnLoginAsA, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
					.addComponent(btnChiefEditor, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
