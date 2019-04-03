import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	public static DatagramSocket socket,socket2;
	public static JLabel label;
	public static BufferedImage bm;
	public static ImageIcon im;
	public static   ByteArrayInputStream bain;
	public static InetAddress Serveraddress ;
	public static Image ime;
	private static PanelImagen panelImagen;
	public static Socket MessageSocket,VoiceSocket; 
	public static DataInputStream din, v_din;
	public static DataOutputStream dout;
	public static String msgin = "";
	public static JTextArea jtext;
	public static JTextField sndmsg;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ClientGUI client = new ClientGUI();
		client.frame.setVisible(true);
		while(client.count==0) {
			System.out.println("");
		}
		
		
		JFrame frame = new JFrame("Client");
		frame.setSize(900,580);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		panelImagen = new PanelImagen();
		panelImagen.setBounds(0, 0, 400, 230);
		panelImagen.setBackground(Color.BLACK);
		frame.add(panelImagen);
		
		jtext = new JTextArea();
		jtext.setBounds(0, 231, 900, 250);
		jtext.setBackground(Color.DARK_GRAY);
		jtext.setForeground(Color.WHITE);
		jtext.setEditable(false);
		frame.add(jtext);
		
		sndmsg = new JTextField();
		sndmsg.setBackground(Color.GRAY);
		sndmsg.setForeground(Color.WHITE);
		sndmsg.setFont(new Font("Nirmala UI", Font.BOLD, 14));
		sndmsg.setBounds(0, 482, 750, 58);
		sndmsg.setVisible(true);
		frame.add(sndmsg);
		
		JButton button = new JButton("Gonder");
		button.setBounds(750, 482, 133, 59);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Nirmala UI", Font.BOLD, 14));
		button.setBackground(Color.DARK_GRAY);
		button.setMnemonic(KeyEvent.VK_ENTER);
		button.setVisible(true);
		frame.add(button);
		
		frame.setVisible(true);
		
		try {
			MessageSocket = new Socket(client.ip,321);
			VoiceSocket = new Socket(client.ip,555);
			v_din = new  DataInputStream(VoiceSocket.getInputStream());
			din = new DataInputStream(MessageSocket.getInputStream());
			dout = new DataOutputStream(MessageSocket.getOutputStream());
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			Serveraddress = InetAddress.getByName(client.ip);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			try {
				socket = new DatagramSocket();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			
				String sndmail = "-" + client.kullaniciadi + ": " + sndmsg.getText().trim(); 
				
				try {
					dout.writeUTF(sndmail);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				sndmsg.setText("");
			}
		});	
			
		 byte[] buff1 = new byte[100]; 
		 buff1 = "Hello".getBytes();
		DatagramPacket packett = new DatagramPacket(buff1, buff1.length,Serveraddress,client.portno);
		try {
			socket.send(packett);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InMessage TMessage = new InMessage();
		TMessage.start();
		
		thread1 Camera = new thread1();
		Camera.start();
		
		Voice voice = new Voice();
		voice.start();
		
		
	} 
		
	static class thread1 extends Thread {
		
		public void run() {
			
			 
				 while(true) {
					 byte[] buffer = new byte[1024*64];
					   DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			            try {
							socket.receive(packet);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            System.out.println(packet.getLength());
			           byte[] buff = packet.getData();
						try {
							bain = new ByteArrayInputStream(buff);
							bm = ImageIO.read(bain);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                panelImagen.setFondo(bm);
			        }

			
		}
	}
	
	static class InMessage extends Thread 
	{
		public void run()
		{
			while(msgin!="exit") 
			{
				
				try 
				{
					msgin= din.readUTF();
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jtext.setText(jtext.getText().trim() + "\n" +  msgin);
			}
		}
	}
	
	static class Voice extends Thread
	{
		public static SourceDataLine audio_in;
		public void run()
		{
		
			AudioFormat format = getaudioformat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			if(!AudioSystem.isLineSupported(info))
			{
				System.out.println("not support");
				System.exit(0);
			}
			try {
				audio_in = (SourceDataLine) AudioSystem.getLine(info);
				audio_in.open(format);
				audio_in.start();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			byte[] buff = new byte[audio_in.getBufferSize()/5];
			//DatagramPacket incoming = new DatagramPacket(buff, buff.length);
			
			while(true)
			{
				try {
					//socket.receive(incoming);
				//	v_din.read(buff,0,buff.length);
					v_din.readFully(buff, 0, buff.length);
				//	buff = incoming.getData();
					audio_in.write(buff, 0, buff.length);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
		}
		
		public static AudioFormat  getaudioformat()
		{
			float sampleRate = 8000.0F;
			int sampleSizeInbits = 16;
			int channel = 2;
			boolean signed = true;
			boolean bigEndian = false;
			
			return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
			
		}
		
		
	}
	
	
	
	
	
	}


