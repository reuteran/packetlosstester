package de.fu_berlin.packetlosstester;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;




public class Sender extends Thread {

	final int interval = MainActivity.SENDING_INTERVAL_MS;
	
	/**
	 * Network structures to send packets
	 */
	private DatagramSocket datagramSocket;
	private DatagramPacket dgpkg;
	private byte[] payload;
	
	/**
	 * Information about packets to be sent
	 */
	private String amount_to_send_str;
	private int amount_to_send_int;
	private int amount_length;
	private int index;
	
	public Sender(String amount) throws SocketException {
		datagramSocket = new DatagramSocket();
		payload = new byte[MainActivity.getPAYLOAD_LENGTH()];
		amount_to_send_str = amount;
		amount_to_send_int = Integer.valueOf(amount_to_send_str);
		amount_length = amount_to_send_str.length();
		payload[0] = (byte) amount_length;
		saveToByteArray(amount_to_send_int, payload, 1);
		index = 0;
		
	}
	

	public void run(){
		
		try {
			dgpkg = new DatagramPacket(payload, 9, InetAddress.getByName("192.168.1.255"), 5000);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i = 0;i<amount_to_send_int;i++){
			
			saveToByteArray(index,payload,5);
			index++;
			try {
				Thread.sleep(interval);
				datagramSocket.send(dgpkg);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	protected static byte[] intToByteArray(int i) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(i);
		return buffer.array();
	}
	
	protected static void saveToByteArray(int value, byte[] array, int start) {
		byte[] buffer = intToByteArray(value);
		for (int i = 0; i < 4; i++)
			array[start + i] = buffer[i];
	}
	
	

}

