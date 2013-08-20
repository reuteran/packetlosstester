package net.commotionwireless.olsrinfo.datatypes;

import java.util.Collection;

/**
 * The complete startup configuration of <tt>olsrd</tt>
 * 
 * Written as part of the Commotion Wireless project
 * 
 * @author Hans-Christoph Steiner <hans@eds.org>
 * @see <a
 *      href="https://code.commotionwireless.net/projects/commotion/wiki/OLSR_Configuration_and_Management">OLSR
 *      Configuration and Management</a>
 */
public class Config {
	public int olsrPort;
	public int debugLevel;
	public boolean noFork;
	public boolean hostEmulation;
	public int ipVersion;
	public boolean allowNoInterfaces;
	public int typeOfService;
	public int rtProto;
	public int rtTable;
	public int rtTableDefault;
	public int rtTableTunnel;
	public int rtTablePriority;
	public int rtTableTunnelPriority;
	public int rtTableDefauiltOlsrPriority;
	public int rtTableDefaultPriority;
	public int willingness;
	public boolean willingnessAuto;
	public long brokenLinkCost;
	public long brokenRouteCost;
	public String fibMetrics;
	public String defaultIpv6Multicast;
	public String defaultIpv4Broadcast;
	public String defaultInterfaceMode;
	public float defaultHelloEmissionInterval;
	public float defaultHelloValidityTime;
	public float defaultTcEmissionInterval;
	public float defaultTcValidityTime;
	public float defaultMidEmissionInterval;
	public float defaultMidValidityTime;
	public float defaultHnaEmissionInterval;
	public float defaultHnaValidityTime;
	public boolean defaultAutoDetectChanges;
	public Collection<LinkQualityMultiplier> defaultLinkQualityMultipliers;
	public Collection<HNA> hna;
	public int ipcConnections;
	public int totalIpcConnectionsAllowed;
	public Collection<AddressNetmask> ipcAllowedAddresses;
	public int pollRate;
	public int nicChangePollInterval;
	public boolean clearScreen;
	public int tcRedundancy;
	public int mprCoverage;
	public int linkQualityLevel;
	public boolean linkQualityFisheye;
	public float linkQualityAging;
	public String linkQualityAlgorithm;
	public int minTcValidTime;
	public boolean setIpForward;
	public String lockFile;
	public boolean useNiit;
	public boolean smartGateway;
	public String mainIpAddress;
	public String unicastSourceIpAddress;
	public boolean useSourceIpRoutes;
	public int maxPrefixLength;
	public int ipSize;
	public boolean deleteInternetGatewaysAtStartup;
	public int willingnessUpdateInterval;
	public float maxSendMessageJitter;
	public int exitValue;
	public int maxTcValidTime;
	public int niit4to6InterfaceIndex;
	public int niit6to4InterfaceIndex;
	public boolean hasIpv4Gateway;
	public boolean hasIpv6Gateway;
	public int ioctlSocket;
	public int routeNetlinkSocket;
	public int routeMonitorSocket;
	public float linkQualityNatThreshold;
	public String olsrdVersion;
	public String olsrdBuildDate;
	public String olsrdBuildHost;
	public String os;
	public int startTime;
}
