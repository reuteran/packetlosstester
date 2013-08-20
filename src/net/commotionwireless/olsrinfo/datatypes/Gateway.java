package net.commotionwireless.olsrinfo.datatypes;

/**
 * A gateway that <tt>olsrd</tt> is aware of.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class Gateway {
	public String ipv4Status;
	public String ipv6Status;
	public String ipType;
	public boolean ipv4;
	public boolean ipv4Nat;
	public boolean ipv6;
	public String ipAddress;
	public int tcPathCost;
	public int hopCount;
	public int uplinkSpeed;
	public int downlinkSpeed;
	public String externalPrefix;
}
