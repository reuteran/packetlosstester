package net.commotionwireless.olsrinfo.datatypes;

/**
 * An address/netmask pair for specifying networks.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class AddressNetmask {
	public String ipAddress;
	public int netmask;
}
