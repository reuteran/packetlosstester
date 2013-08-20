package net.commotionwireless.olsrinfo.datatypes;

/**
 * A node in the whole mesh topology view.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class Node {
	public String destinationIP;
	public String lastHopIP;
	public float linkQuality;
	public float neighborLinkQuality;
	public int tcEdgeCost;
	public int validityTime;
}
