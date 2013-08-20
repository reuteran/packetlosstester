package de.fu_berlin.packetlosstester;


public class LossInformation {
	int amount_sent;
	int amount_received;
	int[] packets_received;
	boolean[] missing;
	
	public int getAmount_sent() {
		return amount_sent;
	}
	public void setAmount_sent(int amount_sent) {
		this.amount_sent = amount_sent;
	}
	public int getAmount_received() {
		return amount_received;
	}
	public void setAmount_received(int amount_received) {
		this.amount_received = amount_received;
	}
	public int[] getPacketsReceived(){
		return this.packets_received;
	}
	public void setPacketsReceived(int[] pack){
		this.packets_received = pack;
	}
	
	public boolean[] getMissingPackets(){
		return this.missing;
	}
	public void setMissingPackets(boolean[] miss){
		this.missing = miss;
	}
	

}
