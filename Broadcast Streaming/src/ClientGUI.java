import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientGUI {

	public JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	public String ip,kullaniciadi;
	public int portno,count=0;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 464, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblKullancAdnz = new JLabel("Kullan\u0131c\u0131 Ad\u0131n\u0131z:");
		lblKullancAdnz.setFont(new Font("Nirmala UI", Font.BOLD, 14));
		lblKullancAdnz.setForeground(Color.WHITE);
		lblKullancAdnz.setBounds(18, 26, 112, 25);
		frame.getContentPane().add(lblKullancAdnz);
		
		textField = new JTextField();
		textField.setBounds(148, 30, 100, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblServerIpAdress = new JLabel("Server IP Adress:");
		lblServerIpAdress.setForeground(Color.WHITE);
		lblServerIpAdress.setFont(new Font("Nirmala UI", Font.BOLD, 14));
		lblServerIpAdress.setBounds(18, 73, 124, 25);
		frame.getContentPane().add(lblServerIpAdress);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(148, 77, 100, 20);
		frame.getContentPane().add(textField_1);
		
		JLabel lblServerPortNo = new JLabel("Server Port No:");
		lblServerPortNo.setForeground(Color.WHITE);
		lblServerPortNo.setFont(new Font("Nirmala UI", Font.BOLD, 14));
		lblServerPortNo.setBounds(18, 119, 124, 25);
		frame.getContentPane().add(lblServerPortNo);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(148, 123, 100, 20);
		frame.getContentPane().add(textField_2);
		
		JButton btnConnection = new JButton("Connection");
		btnConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				portno = Integer.valueOf(textField_2.getText()) ;
				kullaniciadi = (String)textField.getText();
				ip = (String) textField_1.getText();
				count++;
				
			}
		});
		btnConnection.setBackground(new Color(102, 0, 51));
		btnConnection.setForeground(Color.WHITE);
		btnConnection.setBounds(148, 167, 100, 25);
		frame.getContentPane().add(btnConnection);
	}
}
