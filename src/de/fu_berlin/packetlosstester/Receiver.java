package de.fu_berlin.packetlosstester;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class Receiver extends Thread {
	
	
	final static int SOCKET_TIMEOUT = 1000;
	final static int PORT = 5000;
	
	/**
	 * Socket and address it should receive from. This will be changed later to allow dynamic address switching
	 */
	DatagramSocket receiver;
	InetAddress sender_addr;
	
	/**
	 * Variables to keep track of packet meta information
	 */
	AtomicInteger amount_received;
	private int amount_sent;
	int index;
	
	/**
	 * Variables to control the loops, pausingqueue is there to idle the thread, so it doesn't spin around in the
	 * infinite loop
	 */
	AtomicBoolean running;
	boolean giveData;
	BlockingQueue<Integer> pausingqueue;
	
	/**
	 * Structures to pass packet loss information to main thread
	 */
	LossInformation lossinf;
	BlockingQueue<LossInformation> lossqueue;
	
	/**
	 * Arrays to keep track of duplicate packets as well as detect missing packets
	 */
	int[] dupl_tracker;
	boolean[] recvPackets_tracker;
	
	

	
	
	public Receiver(AtomicBoolean running, BlockingQueue<LossInformation> lossqueue,BlockingQueue<Integer> pausingqueue) throws SocketException, UnknownHostException{
		 this.receiver = new DatagramSocket(PORT);
		 this.amount_sent = 0;
		 this.running = running;
		 this.lossqueue = lossqueue;
		 this.pausingqueue = pausingqueue;
		 
		 //Still hardcoded, this will change eventually  
		 this.sender_addr = InetAddress.getByName("192.168.1.255");
		 this.giveData = true;
		 this.amount_received = new AtomicInteger();
		 this.amount_received.set(0);
	}
	
	
	
	public void run(){
		//Packet and byte array that will hold the incoming packets
		DatagramPacket packet;
		byte[] payload = new byte[MainActivity.getPAYLOAD_LENGTH()];
		
		//Set timeout for socket
		try {
			receiver.setSoTimeout(SOCKET_TIMEOUT);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		while(true){
			this.sender_addr = MainActivity.getLink();
			
			//To safe resources, I am using a blocking queue to signal to block the thread. I cannot let the thread run out because then 
			//the socket will block me from using the port on another thread for a while
			try {
				pausingqueue.take();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while (running.get()) {
				
				giveData = true;
				
				packet = new DatagramPacket(payload, payload.length);
				
				try {
					receiver.receive(packet);
					
					//If this is the first packet, extract from payload how many are sent in total and init variables accordingly
					if(amount_sent == 0){
						amount_sent = loadFromByteArray(payload, 1);
		
						dupl_tracker = new int[amount_sent];
						recvPackets_tracker = new boolean[amount_sent];
					}
					//If packet is from the address I should listen on..
					// TODO Enable dynamic switching
					if(packet.getAddress().equals(sender_addr)){
						
						//What Nr. does this packet have?
						index = loadFromByteArray(payload,5);
						
						//Remember this packet
						dupl_tracker[index]++;
						recvPackets_tracker[index] = true;
						
						//Only count it if you haven't received it before
						if(dupl_tracker[index]>1){
						} else {
							amount_received.addAndGet(1);
						}
						
					}
					
					
					
				} catch (SocketTimeoutException t) {
					continue;
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				
			}
			
			//When running is turned of by the main thread, pass on the data gathered
			if(giveData){
				
				//Put data in LossInformation object and put that into the shared queue for the main thread
				lossinf = new LossInformation();
				if(recvPackets_tracker !=null){
					lossinf.setMissingPackets(recvPackets_tracker);					
				} else {
					boolean[] dummy = new boolean[1];
					dummy[0] = true;
					lossinf.setMissingPackets(dummy);
				}
				
				if(dupl_tracker != null){
				
					lossinf.setPacketsReceived(dupl_tracker);
				} else {
					int[] dummy = new int[1];
					lossinf.setPacketsReceived(dummy);
				}
				
				lossinf.setAmount_sent(amount_sent);
				lossinf.setAmount_received(amount_received.get());
				lossqueue.offer(lossinf);
				
				//For safety set giveData to false and reset the counters
				giveData = false;
				amount_received.set(0);
				amount_sent = 0;
			}
			
		}

		
		
	}
	protected static int loadFromByteArray(byte[] array, int start) {
		byte[] buffer = new byte[4];
		for (int i = 0; i < 4; i++)
			buffer[i] = array[start + i];
		return byteArrayToInt(buffer);
	}
	protected static int byteArrayToInt(byte[] b) {
		return ByteBuffer.wrap(b).getInt();
	}

	public AtomicInteger getAmount_received() {
		return amount_received;
	}
}
