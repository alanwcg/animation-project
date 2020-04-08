package main;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import world.World;

public class Server {
	
	private ServerSocket ss;
	private Socket s;
	private DataInputStream dis;
	
	private String input;
	
	private boolean isConnected;
	
	public Server() {
		Game game = new Game();
		game.start();
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.listen();
	}
	
	public void listen() {
		openConnection();
		while(true) {
			while(isConnected) {
				readInput();
			}
			openConnection();
		}
	}
	
	private void openConnection() {
		try {
			// Creates a server with socket (port number)
			ss = new ServerSocket(6666);
			// Establishes a connection through the socket
			s = ss.accept();
			// Creates a data stream and awaits insertion 
			dis = new DataInputStream(s.getInputStream());
			isConnected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void closeConnection() {
		try {
			//Closes the data stream
			dis.close();
			//Closes the socket
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readInput() {
		try {
			//Reads the client input
			input = dis.readUTF();
			processInput(input);
		} catch (IOException e) {
			closeConnection();
			isConnected = false;
		}
	}
	
	private void processInput(String input) {
		if(input.contentEquals("right")) {
			World.player.right = true;
		} else if(input.contentEquals("left")) {
			World.player.left = true;
		}
		
		if(input.contentEquals("up")) {
			World.player.up = true;
		} else if(input.contentEquals("down")) {
			World.player.down = true;
		}
		
		if(input.contentEquals("x")) {
			World.player.hasShot = true;
		}
		
		if(input.contentEquals("enter")) {
			if(Game.state == "GAME_OVER") {
				Game.restartGame = true;
			}
		}
		
		
		if(input.contentEquals("rightR")) {
			World.player.right = false;
		} else if(input.contentEquals("leftR")) {
			World.player.left = false;
		}
		
		if(input.contentEquals("upR")) {
			World.player.up = false;
		} else if(input.contentEquals("downR")) {
			World.player.down = false;
		}
		
		if(input.contentEquals("xR")) {
			World.player.hasShot = false;
		}
	}
}
