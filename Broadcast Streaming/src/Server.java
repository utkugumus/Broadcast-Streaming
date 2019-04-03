import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JViewport;


import javax.swing.JLabel;
import java.awt.Font;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;


import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Server {

	public JFrame frame;
	private JTextField textField;
	public  int portno;
	public  String comboboxText;
	public int count=0;
	public String url="";
	public static JComboBox comboBox;
	public static 	JButton btnSelectVideoFile;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 553, 395);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(113, 23, 104, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblPortNumber = new JLabel("Port Number:");
		lblPortNumber.setForeground(Color.WHITE);
		lblPortNumber.setFont(new Font("Nirmala UI", Font.BOLD, 14));
		lblPortNumber.setBounds(10, 11, 104, 41);
		frame.getContentPane().add(lblPortNumber);
		
		JButton btnStartServer = new JButton("Start Server");
		
		btnStartServer.setForeground(Color.WHITE);
		btnStartServer.setBackground(new Color(102, 0, 0));
		btnStartServer.setBounds(113, 54, 104, 29);
		frame.getContentPane().add(btnStartServer);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Camera", "Video", "Music"}));
		comboBox.setBounds(360, 23, 84, 20);
		frame.getContentPane().add(comboBox);
		
		JLabel lblNewLabel = new JLabel("Stream Type:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Nirmala UI", Font.BOLD, 14));
		lblNewLabel.setBounds(260, 17, 104, 29);
		frame.getContentPane().add(lblNewLabel);
		
		btnSelectVideoFile = new JButton("Select Video");
		btnSelectVideoFile.setForeground(Color.WHITE);
		btnSelectVideoFile.setBackground(new Color(102, 0, 0));
		btnSelectVideoFile.setBounds(328, 54, 116, 29);
		btnSelectVideoFile.setVisible(true);
		//btnSelectVideoFile.setEnabled(false);
		frame.getContentPane().add(btnSelectVideoFile);
		frame.getContentPane().setVisible(true);
		
		
		btnSelectVideoFile.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser jf = new JFileChooser();
				jf.showOpenDialog(frame);
				File f;
				f = jf.getSelectedFile();
				url = f.getPath();	
			}
		});
		
		
		
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				portno = Integer.valueOf(textField.getText()) ;
				comboboxText = (String)comboBox.getSelectedItem();
				count++;
				
			} 
		});
				
				
		
	}
	
}
