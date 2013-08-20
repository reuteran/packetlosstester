package net.commotionwireless.olsrinfo.datatypes;

/**
 * A mesh link between two nodes in the mesh.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class Link {
	public String localIP;
	public String remoteIP;
	public int validityTime;
	public float linkQuality;
	public float neighborLinkQuality;
	public int linkCost;
}
