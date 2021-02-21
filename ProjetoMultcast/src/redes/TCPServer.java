package redes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import salas.Salas;

public class TCPServer {

	public static void main(String args[]) {

		ServerSocket listenSocket = null;

		try {
			// Porta do servidor
			int serverPort = 7896;

			// Fica ouvindo a porta do servidor esperando uma conexao.
			listenSocket = new ServerSocket(serverPort);
			System.out.println("Servidor: ouvindo porta TCP/7896.");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				new Connection(clientSocket);
			}
		} catch (IOException e) {
			System.out.println("Listen socket:" + e.getMessage());
		} finally {
			if (listenSocket != null)
				try {
					listenSocket.close();
					System.out.println("Servidor: liberando porta TCP/7896.");
				} catch (IOException e) {
					/* close falhou */
				}
		}
	}
}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;

	public Connection(Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Conexão:" + e.getMessage());
		}
	}

	ArrayList<Salas> salas = new ArrayList<Salas>();

	public void run() {
		try { // servidor de repetição

			String data = in.readUTF(); // le a linha da entrada
			System.out.println("Recebido: " + data);

			switch (data) {

			case "CRIAR SALA":

				long digito = 0;
				String ipMultCast = null;

				if (salas != null) {
					digito = salas.stream().mapToLong(l -> Integer.parseInt(l.getIpMultcast().substring(8, 9))).count() + 1;

					ipMultCast = "228.5.6." + String.valueOf(digito);
				} else {
					ipMultCast = "228.5.6." + "1";
				}

				Salas sala = new Salas();
				
				sala.setIpMultcast(ipMultCast);
				sala.setMembros(clientSocket.getInetAddress());
				salas.add(sala);

				data = "Sala criada IP;" + ipMultCast;

				break;
				
			case "LISTAS SALAS":
				
			out.writeUTF(salas.toString());
				
				break;

			}

			out.writeUTF(data);
			
			
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			try {
				clientSocket.close();
				System.out.println("Servidor: fechando conexão com cliente.");
			} catch (IOException e) {
				/* close falhou */
			}
		}

	}
}
