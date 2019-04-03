import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


public class SServer {
	public static DatagramSocket server,server2;
	public static Webcam webcam;
	public static BufferedImage bm;
	public static int clientsnumber = 0,cientsPORT;
	public static InetAddress []clients›P = new InetAddress[5];
	public static InetAddress cients›P;
	public static int []clientsPORT = new int[5]; 
	public static DatagramPacket packet;
	public static ImageIcon im;
	public static InetAddress address,address2 ;
	public static JLabel label;
	private static PanelImagen panelImagen;
	public static JTextField sndmsg ;
	public static JTextArea jtext;
	public static ServerSocket MessageServer;
	public static ServerSocket VoiceServer;
	public static Socket []MessageSockets = new Socket[5];
	public static Socket []VoiceSockets = new Socket[5];
	public static InMessage[] st = new InMessage[5];
	public static Voice[] vc = new Voice[5];
	public static DataInputStream[] inFromClient = new DataInputStream[5];
	public static DataOutputStream[] outToClient = new DataOutputStream[5];
	//public static DataInputStream[] invoiceclient = new DataInputStream[5];
	public static DataOutputStream[] outvoiceclient = new DataOutputStream[5];
	public static TargetDataLine audio_in;
	public static String rcvmail = "";
	public static EmbeddedMediaPlayer mediaPlayer ;
	public static MediaPlayerFactory mediaPlayerFactory ;
	public static CanvasVideoSurface videoSurface;
	public static Canvas canvas;
	public static Server sv;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		sv = new Server();
		sv.frame.setVisible(true);
		while(sv.count==0) {
			System.out.println("");
		}
		
		try {
			MessageServer = new ServerSocket(321);
			VoiceServer = new ServerSocket(555);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		JFrame frame = new JFrame("Server");
		frame.setSize(900,580);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		panelImagen = new PanelImagen();
		panelImagen.setBounds(0, 0, 400, 230);
		panelImagen.setBackground(Color.BLACK);
		
		
		canvas = new Canvas();
		canvas.setBounds(0, 0, 400, 230);
		canvas.setBackground(Color.BLACK);
		
		
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
		button.setVisible(true);
		button.setMnemonic(KeyEvent.VK_ENTER);
		frame.add(button);
		
		if(sv.comboboxText == "Camera")
		{
			frame.add(panelImagen);
		}
		else if(sv.comboboxText == "Video")
		{
			canvas.setVisible(true);
			frame.add(canvas);
		}
		
		
		frame.setVisible(true);
		System.out.println(sv.portno);
		System.out.println(sv.comboboxText);
			try {
				server = new DatagramSocket(sv.portno);
				server.setBroadcast(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
			
			
			mediaPlayerFactory = new MediaPlayerFactory();
			mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
			 videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
			mediaPlayer.setVideoSurface(videoSurface);
			
			
			
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String sndmail = "-Server: " + sndmsg.getText().trim(); 

				for(int i = 0; i < clientsnumber; i++)
				{
					try {
						outToClient[i].writeUTF(sndmail);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				jtext.setText(jtext.getText().trim() + "\n" + sndmail );
				sndmsg.setText("");
			}
		});	
		
		AudioFormat format = getaudioformat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if(!AudioSystem.isLineSupported(info))
		{
			System.out.println("not support");
			System.exit(0);
		}
		try {
			audio_in = (TargetDataLine) AudioSystem.getLine(info);
			audio_in.open(format);
			audio_in.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(true)
		{
		 byte [] clibuf = new byte[100];
			
		 DatagramPacket ClientsWait = new DatagramPacket(clibuf, clibuf.length);
		 try {
			server.receive(ClientsWait);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 clients›P[clientsnumber] = ClientsWait.getAddress();
			 clientsPORT[clientsnumber] = ClientsWait.getPort();
			
			  try {
				MessageSockets[clientsnumber] = MessageServer.accept();
				VoiceSockets[clientsnumber] = VoiceServer.accept(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  try {
				inFromClient[clientsnumber] = new DataInputStream(MessageSockets[clientsnumber].getInputStream());
				outToClient[clientsnumber] = new DataOutputStream(MessageSockets[clientsnumber].getOutputStream());
				
				outvoiceclient[clientsnumber] = new DataOutputStream(VoiceSockets[clientsnumber].getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			  
			  
			  st[clientsnumber] = new InMessage(clientsnumber);
			  st[clientsnumber].start();
			  
			  vc[clientsnumber] = new Voice();
			  vc[clientsnumber].start();
			  
			 if(sv.comboboxText == "Camera") 
			 {
			System.out.print("ife girdi.");
			 thread2 camera = new thread2();
			 camera.start();
			 
			 }
			 else if ( sv.comboboxText == "Video" )
			 {
				T_Video video = new T_Video();
				video.start();
			 }
			 
			 
			 clientsnumber++;
		
		}
			 
		
	}
	
	
	static class thread2 extends Thread 
	{
		public void run()
		{
			Webcam webcam = Webcam.getDefault();
			webcam.setViewSize(WebcamResolution.VGA.getSize());
			webcam.open();
			while (true)  {
				
        		 BufferedImage framee = webcam.getImage(); //Obtiene imagen de la webcam
                 panelImagen.setFondo(framee);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             try {
				ImageIO.write(framee, "jpeg", baos);
				 baos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
             byte[] image_buffer = baos.toByteArray();
             
             
             for(int i=0; i<clientsnumber;i++)
             {
             DatagramPacket packet = new DatagramPacket(image_buffer, image_buffer.length,clients›P[i],clientsPORT[i]);
             try {
				server.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             }
             
			}// while scope close
		}
		
	}
	
	static class InMessage extends Thread 
	{
		int noclient;
		
		public InMessage (int a)
		{
			noclient = a;
		}
		
		
		public void run()
		{
			while(true) 
			{
				
				try 
				{
					rcvmail= inFromClient[noclient].readUTF();
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jtext.setText(jtext.getText().trim() + "\n" +  rcvmail);
				
				for(int i = 0; i < clientsnumber; i++)
				{
					try {
						outToClient[i].writeUTF(rcvmail);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
		}
	}
	
	static class T_Video extends Thread 
	{
		
		public void run()
		{
			mediaPlayer.playMedia(sv.url);
			while (true)  {
				
        		 BufferedImage framee =  mediaPlayer.getVideoSurfaceContents(); //Obtiene imagen de la webcam
                 panelImagen.setFondo(framee);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             try {
            	 ImageIO.write(framee, "jpeg", baos);
				 baos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
             byte[] image_buffer = baos.toByteArray();
             
             
             for(int i=0; i<clientsnumber;i++)
             {
             DatagramPacket packet = new DatagramPacket(image_buffer, image_buffer.length,clients›P[i],clientsPORT[i]);
             try {
				server.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             }
             
			}// while scope
		}
	}
	
	static class Voice extends Thread
	{
		
		
		public void run() {
	

				byte[] buff = new byte[audio_in.getBufferSize()/5];
				int i=0;
				while (true)
				{
					
					int count = audio_in.read(buff, 0, buff.length);
				
					if(count>0) {
					
					for(int k=0; k < clientsnumber; k++)
					{	
					System.out.println("send #" + i++);
					try {
						outvoiceclient[k].write(buff,0,count);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
					}
				}

			
			
			
			
			
		}
		
		
	
			
			
		}
	
	public static AudioFormat  getaudioformat()
	{
		float sampleRate = 16000.0F;
		int sampleSizeInbits = 16;
		int channel = 1;
		boolean signed = true;
		boolean bigEndian = false;
		
		return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
		
	}
	
	}
	
	
	

