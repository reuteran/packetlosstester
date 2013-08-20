package net.commotionwireless.olsrinfo.datatypes;

import java.util.Collection;

/**
 * A network interface used in MID (Multiple Interface Declaration) setups.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR Configuration and Management</a>
 */
public class MID {
	public String ipAddress;
	public Collection<MIDAlias> aliases;
}
