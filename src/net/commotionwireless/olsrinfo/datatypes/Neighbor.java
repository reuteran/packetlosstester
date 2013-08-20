package net.commotionwireless.olsrinfo.datatypes;

import java.util.Collection;

/**
 * A class representing a first-hop neighbor on the mesh.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class Neighbor {
	public String ipv4Address;
	public boolean symmetric;
	public boolean multiPointRelay;
	public boolean multiPointRelaySelector;
	public int willingness;
	public int twoHopNeighborCount;
	public Collection<String> twoHopNeighbors;
}
