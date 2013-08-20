package net.commotionwireless.olsrinfo.datatypes;

/**
 * A class representing an <tt>olsrd</tt> plugin that was loaded at start time.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class Plugin {
	public String plugin;
	public String host;
	public String net;
	public String ping;
	public String hna;
	public String accept;
	public int port;
	public String uuidfile;
	public String keyfile;
	public int checkinterval;
	public int pinginterval;
}
