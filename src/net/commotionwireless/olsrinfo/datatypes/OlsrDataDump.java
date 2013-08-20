package net.commotionwireless.olsrinfo.datatypes;

import java.util.Collection;

/**
 * A class representing the complete dump of information from <tt>olsrd</tt>'s
 * jsoninfo plugin.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class OlsrDataDump {
	public Config config;
	public Collection<Gateway> gateways;
	public Collection<HNA> hna;
	public Collection<Interface> interfaces;
	public Collection<Link> links;
	public Collection<MID> mid;
	public Collection<Neighbor> neighbors;
	public Collection<Node> topology;
	public Collection<Plugin> plugins;
	public Collection<Route> routes;
	public int systemTime = 0;
	public int timeSinceStartup = 0;
	public String uuid = "";

	String raw = "";

	public void setRaw(String s) {
		raw = s;
	}
	
	public String toString(){
		return raw;
	}
}
