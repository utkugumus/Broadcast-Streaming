import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

public class C_Voice {

	public static int port = 333;
	public static SourceDataLine audio_in;
	public static DatagramSocket client;
	public static boolean control1 = false;
	
	
	public static void main(String[] args) 
	{
		
	/*	JFrame frame = new JFrame("client");
		frame.setSize(300,300);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		JButton button = new JButton("Start");
		button.setVisible(true);
		frame.add(button);
		frame.setVisible(true);*/
		
		
		
		
		
		// TODO Auto-generated method stub
		try {
			client = new DatagramSocket(555);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
	
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
		
		byte[] buff = new byte[512];
		DatagramPacket incoming = new DatagramPacket(buff, buff.length);
		
		
		while(true)
		{
			try {
				client.receive(incoming);
				buff = incoming.getData();
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
