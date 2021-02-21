package redes;

import java.net.*;
import java.io.*;

public class TCPClient {
	public static void main(String args[]) {
		// arguments supply message and hostname
		Socket s = null;
		try {
			int serverPort = 7896;
			s = new Socket(args[1], serverPort);
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(args[0]);
			String data = in.readUTF(); // ê uma linha do fluxo de dados
			System.out.println("Recebido: " + data);

			if (args[0].equalsIgnoreCase("ENTRAR NA SALA")) {

				System.out.println("Você entrou no grupo: " + args[2]);

				MulticastPeer multCast = new MulticastPeer(args[2]);
				multCast.JoinGroup();

			}

		} catch (UnknownHostException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
		}
	}
}
