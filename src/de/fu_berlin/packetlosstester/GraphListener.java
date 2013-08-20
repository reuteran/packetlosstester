package de.fu_berlin.packetlosstester;

 
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


import com.androidplot.series.XYSeries;
import com.androidplot.xy.SimpleXYSeries;
 
public class GraphListener implements Runnable,XYSeries {
 
    // encapsulates management of the observers watching this datasource for update events:
    class MyObservable extends Observable {
	    @Override
	    public void notifyObservers() {
	        setChanged();
	        super.notifyObservers();
	    }
    }
    
    public static final int REFRESH_RATE = 500;
    public static final int HISTORY_SIZE = 30;
    public static final String TITLE = "Recv. packets per "+ REFRESH_RATE + " ms";
    
    /**
     * Notifies plot updater in Main Activity when data changes
     */
    private MyObservable notifier;

  
    /**
     * The actual series being plotted
     */
    SimpleXYSeries series;
    
    /**
     * This is the data basis for Y values of the series
     */
    ArrayList<Number> points;
    
    /**
     * Controlled by Main Activity on Receiver toggle to stop thread. Same AtomicBoolean as used in Receiver loop
     */
    AtomicBoolean run;
    
    /**
     * The counter of received packets kept by Receiver
     */
    AtomicInteger recv_packets;
    
    int lastAmount;
    int deltaPackets;
    int recv_buffer;

    
    public GraphListener(AtomicBoolean r, AtomicInteger packets){
    	this.run = r;
    	this.notifier = new MyObservable();
    	this.recv_packets = packets;
    	this.points = new ArrayList<Number>();
    	this.series = new SimpleXYSeries(points,SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,TITLE);
    }

 
    //@Override
    public void run() {
    		

    		
	        try {
	            while (run.get()) {
	 
	                Thread.sleep(REFRESH_RATE); 
	                recv_buffer = recv_packets.get();
	                deltaPackets = recv_buffer - lastAmount;
	                lastAmount = recv_buffer;
	               
	           
	                series.addLast(null,deltaPackets);
	                if(series.size()>HISTORY_SIZE){
	                	series.removeFirst();
	                }
	                
	                notifier.notifyObservers();
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
    	
        
    }
    
    public SimpleXYSeries getTestSeries(){
    	return series;
    }
    
    
 
    public int getItemCount(int series) {
        return 2;
    }
 
    public Number getX(int index) {
        if (index >= HISTORY_SIZE) {
            throw new IllegalArgumentException();
        }

        return index;
    }

    
    public Number getY(int index) {
         if (index >= HISTORY_SIZE) {
             throw new IllegalArgumentException();
         }
         return deltaPackets;

    }
 
    public void addObserver(Observer observer) {
        notifier.addObserver(observer);
    }
 
    public void removeObserver(Observer observer) {
        notifier.deleteObserver(observer);
    }


	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return TITLE;
	}


	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.getItemCount(0);
	}
	
	
	


 
}