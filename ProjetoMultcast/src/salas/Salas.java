package salas;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Salas {

	private String ipMultcast;
	private List<InetAddress> membros = new ArrayList<InetAddress>();

	public List<InetAddress> getMembros() {
		return membros;
	}

	public void setMembros(InetAddress membros) {
		this.membros.add(membros);
	}

	public String getIpMultcast() {
		return ipMultcast;
	}

	public void setIpMultcast(String ipMultcast) {
		this.ipMultcast = ipMultcast;
	}

}
