package redes;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class MulticastPeer {

	private static MulticastSocket mSocket = null;
	private static InetAddress groupIp = null;
	private static String ipReservado = null;

	public MulticastPeer(String ipReservado) {
		super();
		this.ipReservado = ipReservado;

	}

	public void JoinGroup() {

		// args provê o conteúdo da mensagem e o endereço do grupo multicast (p. ex.
		// "228.5.6.7")
		// 0 igual a mensagem
		// 1 igual ao IP

		try {
			groupIp = InetAddress.getByName(ipReservado);

			mSocket = new MulticastSocket(6789);
			mSocket.joinGroup(groupIp);

			System.out.println("Pode começar a digitar, para sair digite SAIR!");

			Scanner mensagem = new Scanner(System.in);
			String text = "Teste";
			
			while (!text.equalsIgnoreCase("SAIR!") ) {
				
				 text = mensagem.next();

				byte[] message = text.getBytes();
				DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, 6789);
				mSocket.send(messageOut);
				byte[] buffer = new byte[10000000];

				DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
				mSocket.receive(messageIn);
				System.out.println("Recebido do cliente:" + new String(messageIn.getData()).trim());
				buffer = new byte[10000000];
			}
			
			LeaveGroup();

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
	}

	public static void LeaveGroup() {

		try {
			mSocket.leaveGroup(groupIp);

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (mSocket != null)
				mSocket.close();
		}

	}

}
