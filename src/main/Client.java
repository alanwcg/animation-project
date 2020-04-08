package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 720;
	private static final int HEIGHT = 480;
	
	private Socket s;
	private DataOutputStream dout;
	private JFrame frame;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	public Font newFont;
	
	public Client() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.initializeFrame();
		
		try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(50f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setBackground(Color.black);
		
		JLabel text = new JLabel("Waiting For Input...");
		text.setFont(newFont);
		text.setForeground(Color.white);
		text.setBounds(30, 200, 700, 40);
		
		this.add(text);
		
		frame.addKeyListener(this);
		frame.setVisible(true);
		this.requestFocus();
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.connect();
	}
	
	public void connect() {
		try {
			// Creates a socket (host, port)
			s = new Socket("localhost", 6666);
			// Creates a stream of output data (will use the socket as deliver channel)
			dout = new DataOutputStream(s.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeFrame() {
		frame = new JFrame("Client");
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
	}
	
	private void sendData(String output) {
		try {
			// Writes a string in the stream
			dout.writeUTF(output);
			// Sends the stream of data
			dout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*private static void closeStream() {
		try {
			// Closes the stream of data
			dout.close();
			// Closes the socket
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			sendData("right");
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			sendData("left");
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			sendData("up");
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			sendData("down");
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			sendData("x");
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendData("enter");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			sendData("rightR");
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			sendData("leftR");
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			sendData("upR");
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			sendData("downR");
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			sendData("xR");
		}
	}
}
