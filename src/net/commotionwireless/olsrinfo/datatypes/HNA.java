package net.commotionwireless.olsrinfo.datatypes;

/**
 * A Host and Network Association (HNA) between the current mesh network
 * and another network that is accessible via a given node in the mesh.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class HNA {
	public String destination;
	public int genmask;
	public String gateway;
	public int validityTime;
}
