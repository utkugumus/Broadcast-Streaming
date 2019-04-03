import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

public class S_Voice {

	public static int port = 333;
	public static InetAddress adress;
	public static TargetDataLine audio_in;
	public static DatagramSocket server;
	public static boolean control = false;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

/*		JFrame frame = new JFrame("server");
		frame.setSize(300,300);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		JButton button = new JButton("Start");
		button.setVisible(true);
		frame.add(button);
		frame.setVisible(true);*/
		
		
		
		try {
			server= new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		 try {
			adress=  InetAddress.getByName("192.168.43.252");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
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
			

			byte[] buff = new byte[512];
			int i=0;
			while (true)
			{
				
				audio_in.read(buff, 0, buff.length);
				DatagramPacket data = new DatagramPacket(buff, buff.length, adress, 555);
				System.out.println("send #" + i++);
				try {
					server.send(data);
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
