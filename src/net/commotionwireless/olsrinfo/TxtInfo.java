
package net.commotionwireless.olsrinfo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Scrape the output of <tt>olsrd</tt>'s txtinfo plugin into
 * arrays of Strings.
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class TxtInfo {

	String host = "127.0.0.1";
	int port = 2006;

	public TxtInfo() {
	}

	public TxtInfo(String sethost) {
		host = sethost;
	}

	public TxtInfo(String sethost, int setport) {
		host = sethost;
		port = setport;
	}

	public String[] request(String req) throws IOException {
		Socket sock = null;
		BufferedReader in = null;
		PrintWriter out = null;
		List<String> retlist = new ArrayList<String>();

		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()), 8192);
			out = new PrintWriter(sock.getOutputStream(), true);
		} catch (UnknownHostException e) {
			throw new IOException();
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for socket to " + host + ":"
					+ Integer.toString(port));
		}
		out.println(req);
		String line;
		while ((line = in.readLine()) != null) {
			if (!line.equals(""))
				retlist.add(line);
		}
		// the txtinfo plugin drops the connection once it outputs
		out.close();
		in.close();
		sock.close();

		return retlist.toArray(new String[retlist.size()]);
	}

	public String[][] command(String cmd) {
		String[] data = null;

		final Set<String> supportedCommands = new HashSet<String>(
				Arrays.asList(new String[] { "/2ho", // two-hop neighbors
						"/con", // conf file
						"/gat", // gateways
						"/hna", // Host and Network Association
						"/int", // network interfaces
						"/lin", // links
						"/mid", // MID
						"/nei", // neighbors
						"/rou", // routes
						"/top", // topology
				}));
		if (!supportedCommands.contains(cmd))
			System.out.println("Unsupported command: " + cmd);

		try {
			data = request(cmd);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for socket to " + host + ":"
					+ Integer.toString(port));
		}
		int startpos = -1;
		for (int i = 0; i < data.length; i++) {
			if (data[i].startsWith("Table: ")) {
				startpos = startpos+i + 3;
				break;
			}
		}
		if (startpos >= data.length || startpos == -1)
			return new String[0][0];
		int fields = data[startpos].split("\t").length;
		String[][] ret = new String[data.length - startpos][fields];
		for (int i = 0; i < ret.length; i++)
			ret[i] = data[i + startpos].split("\t");
		return ret;
	}

	public String[][] command2(String cmd) {
		String[] data = null;

		final Set<String> supportedCommands = new HashSet<String>(
				Arrays.asList(new String[] { "/2ho", // two-hop neighbors
						"/con", // conf file
						"/gat", // gateways
						"/hna", // Host and Network Association
						"/int", // network interfaces
						"/lin", // links
						"/mid", // MID
						"/nei", // neighbors
						"/rou", // routes
						"/top", // topology
				}));
		if (!supportedCommands.contains(cmd))
			System.out.println("Unsupported command: " + cmd);

		try {
			data = request(cmd);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for socket to " + host + ":"
					+ Integer.toString(port));
		}
		
		int startpos = -1;
		for (int i = 0; i < data.length; i++) {
			if (data[i].startsWith("Table: ")) {
				startpos = i + 2;
				break;
			}
		}
		if (startpos >= data.length || startpos == -1)
			return new String[0][0];
		int fields = data[startpos].split("\t").length;
		String[][] ret = new String[data.length - startpos][fields];
		for (int i = 0; i < ret.length; i++)
			ret[i] = data[i + startpos].split("\t");
		
		
		return ret;
	}
	
	/**
	 * 2-hop neighbors on the mesh
	 * 
	 * @return array of per-IP arrays of IP address, SYM, MPR, MPRS,
	 *         Willingness, and 2 Hop Neighbors
	 */
	public String[][] twohop() {
		return command("/2ho");
	}

	/**
	 * immediate neighbors on the mesh
	 * 
	 * @return array of per-IP arrays of IP address, SYM, MPR, MPRS,
	 *         Willingness, and 2 Hop Neighbors
	 */
	public String[][] neighbors() {
		return command("/nei");
	}

	/**
	 * direct connections on the mesh, i.e. nodes with direct IP connectivity
	 * via Ad-hoc
	 * 
	 * @return array of per-IP arrays of Local IP, Remote IP, Hysteresis, LQ,
	 *         NLQ, and Cost
	 */
	public String[][] links() {
		return command2("/lin");
	}

	/**
	 * IP routes to nodes on the mesh
	 * 
	 * @return array of per-IP arrays of Destination, Gateway IP, Metric, ETX,
	 *         and Interface
	 */
	public String[][] routes() {
		return command("/rou");
	}

	/**
	 * Host and Network Association (for supporting dynamic internet gateways)
	 * 
	 * @return array of per-IP arrays of Destination and Gateway
	 */
	public String[][] hna() {
		return command("/hna");
	}

	/**
	 * Multiple Interface Declaration
	 * 
	 * @return array of per-IP arrays of IP address and Aliases
	 */
	public String[][] mid() {
		return command("/mid");
	}

	/**
	 * topology of the whole mesh
	 * 
	 * @return array of per-IP arrays of Destination IP, Last hop IP, LQ, NLQ,
	 *         and Cost
	 */
	public String[][] topology() {
		return command("/top");
	}

	/**
	 * the network interfaces that olsrd is aware of
	 * 
	 * @return array of per-IP arrays of Destination IP, Last hop IP, LQ, NLQ,
	 *         and Cost
	 */
	public String[][] interfaces() {
		return command("/int");
	}

	/**
	 * the gateways to other networks that this node knows about
	 * 
	 * @return array of per-IP arrays of Status, Gateway IP, ETX, Hopcount,
	 *         Uplink, Downlink, IPv4, IPv6, Prefix
	 */
	public String[][] gateways() {
		return command("/gat");
	}

	/**
	 * for testing from the command line
	 */
	public static void main(String[] args) throws IOException {
		TxtInfo txtinfo = new TxtInfo();
		System.out.println("NEIGHBORS----------");
		for (String[] s : txtinfo.neighbors()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
		System.out.println("LINKS----------");
		for (String[] s : txtinfo.links()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
		System.out.println("ROUTES----------");
		for (String[] s : txtinfo.routes()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
		System.out.println("HNA----------");
		for (String[] s : txtinfo.hna()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
		System.out.println("MID----------");
		for (String[] s : txtinfo.mid()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
		System.out.println("TOPOLOGY----------");
		for (String[] s : txtinfo.topology()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
		System.out.println("INTERFACES----------");
		for (String[] s : txtinfo.interfaces()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
		System.out.println("GATEWAYS----------");
		for (String[] s : txtinfo.gateways()) {
			for (String t : s)
				System.out.print(t + ",");
			System.out.println();
		}
	}
}
