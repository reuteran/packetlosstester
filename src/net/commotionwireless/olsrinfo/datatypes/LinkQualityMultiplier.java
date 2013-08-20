package net.commotionwireless.olsrinfo.datatypes;

/**
 * A multiplier for a given route to affect its link quality metric.
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a
 *      href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR
 *      Configuration and Management</a>
 */
public class LinkQualityMultiplier {
	public String route;
	public float multiplier;
}
