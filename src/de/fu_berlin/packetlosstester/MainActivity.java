package de.fu_berlin.packetlosstester;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.atomic.AtomicBoolean;

import net.commotionwireless.olsrinfo.TxtInfo;

import com.androidplot.Plot;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	final static int PAYLOAD_LENGTH = 100;
	final static int SENDING_INTERVAL_MS = 5;
	static InetAddress SENDING_LINK;
	
	TxtInfo infoparser;
	
	Receiver recv;
	Sender sender;
	
	/**
	 * Controls GraphListener and Receiver loops
	 */
	AtomicBoolean running;
	
	/**
	 * Gets input of how many packets should be sent
	 */
	EditText packetAmount;
	
	/**
	 * Datastructure to pass information from Receiver to Mainthread
	 */
	LossInformation lossinfo;
	
	/**
	 * Queue used to pass LossInformation Objects between Receiver and Mainthread
	 */
	BlockingQueue<LossInformation> lossqueue;
	
	/**
	 * Used to make the receiver Thread idle. I cannot let the Thread run out, because a new thread would have to
	 * make a new socket, which is not possible since the address would be still in use by the old expired socket.
	 * EADDRESS already in use error
	 */
	BlockingQueue<Integer> pausingqueue;
	
	TopologyParser links_getter;
	MyListener selectionListener;
	
	/**
	 * Is called by GraphListener to update the plot
	 * @author ponken
	 *
	 */
	private class MyPlotUpdater implements Observer {
        @SuppressWarnings("rawtypes")
		Plot plot;
        @SuppressWarnings("rawtypes")
		public MyPlotUpdater(Plot plot) {
            this.plot = plot;
        }
        @Override
        public void update(Observable o, Object arg) {
            plot.redraw();
        }

    }
 
    private XYPlot dynamicPlot;
    private MyPlotUpdater plotUpdater;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/**
		 * Android boilerplate
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		/**
		 * Init variables
		 */
		running = new AtomicBoolean();
		running.set(false);
		packetAmount = (EditText) findViewById(R.id.packetAmount);
		lossqueue = new ArrayBlockingQueue<LossInformation>(5);
		pausingqueue = new ArrayBlockingQueue<Integer>(1);
        dynamicPlot = (XYPlot) findViewById(R.id.dynamicPlot);
        plotUpdater = new MyPlotUpdater(dynamicPlot);
        infoparser = new TxtInfo();
        
        // only display whole numbers in domain labels
        dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        
        
        //In case user forgets to set link, use broadcast
        try {
			MainActivity.setLink(InetAddress.getByName("192.168.1.255"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
       
		
       
		

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void showDialog(View v) throws InterruptedException, ExecutionException{
		
		
		
		
		links_getter = new TopologyParser();
		String[] links = links_getter.execute(infoparser).get();
		
//		Bundle argument = new Bundle();
//		argument.putStringArray("links", links);
		
//		ListenerDialog links_dialog = new ListenerDialog();
		
//		links_dialog.setArguments(argument);		
//		links_dialog.show(getFragmentManager(), "links_dialog");
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose neighbour to listen to");
		//      String[] links = this.getArguments().getStringArray("links");
		
		selectionListener = new MyListener(links);
		builder.setSingleChoiceItems(links,0,selectionListener);
		AlertDialog dia = builder.create();
		
		dia.show();
		
		
		
		
		
	}
	
	public void sendPackets(View v) throws SocketException{
		String amount = packetAmount.getText().toString();
		TextView status = (TextView) findViewById(R.id.statusDisplay);
		
		/**
		 * Sends out the amount of packets in a new sender thread
		 */
		sender = new Sender(amount);
		sender.start();
		
		/**
		 * Notifies GUI when Sender is done sending
		 */
		SendNotifier not = new SendNotifier();
		not.execute(Integer.valueOf(amount),SENDING_INTERVAL_MS);
		
		
		status.setText("Sending..");
	}
	
	@SuppressWarnings("deprecation")
	public void onReceiverToggle(View v) throws SocketException, InterruptedException, UnknownHostException{
		
		/**
		 * TextViews for status (sending or receiving) and missed packets
		 */
		TextView status = (TextView) findViewById(R.id.statusDisplay);
		TextView missed = (TextView) findViewById(R.id.missedDisplay);
		//Make TextView scrollable, in case of many missed packets
		missed.setMovementMethod(new ScrollingMovementMethod());
		
		lossinfo = new LossInformation();

		ToggleButton toggle = (ToggleButton) findViewById(R.id.receiverButton);
		boolean on = toggle.isChecked();

        //New Formatter for our plot
        LineAndPointFormatter f1 = new LineAndPointFormatter(Color.rgb(0, 0, 200), null, Color.rgb(0, 0, 80));
        f1.getFillPaint().setAlpha(220);
        
        /*
         *Setup plot 
         */
        dynamicPlot.setGridPadding(5, 0, 5, 0);
        dynamicPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
 
        // thin out domain/range tick labels so they dont overlap each other:
        dynamicPlot.setTicksPerDomainLabel(5);
        dynamicPlot.setTicksPerRangeLabel(1);
 
        // freeze the range boundaries:
        dynamicPlot.setRangeBoundaries(0, 200, BoundaryMode.FIXED);
        
        
		
		if(on){
			
			/*
			 * If this if the first toggle, start receiver
			 */
			if(recv == null){
				recv = new Receiver(running,lossqueue,pausingqueue);
				recv.start();
			}
			
			/*
			 * Unblock various receiver loops
			 */
			running.set(true);
			pausingqueue.add(1);
			
			
			/*
			 * Make new GraphListener and make the updater observe it, then add series from GraphListener to plot
			 */
	        GraphListener data = new GraphListener(running,recv.getAmount_received());
	        data.addObserver(plotUpdater);
	        dynamicPlot.addSeries(data.getTestSeries(), 
	        		new LineAndPointFormatter(Color.rgb(0, 100, 0), Color.rgb(0,200,0), Color.rgb(0, 80, 0)));
	        dynamicPlot.setDomainStepValue(1);
	        
	        
	        // kick off the data generating thread:
	        new Thread(data).start();
	 

			
	        status.setText("Receiving..");
			 
		}
		if(!on){
			if(recv != null){

				dynamicPlot.clear();
				
				missed.setText("Looking for missed packets..");
				
				/*
				 * Stop receiver loop
				 */
				running.set(false);
				
				/*
				 * Get packet loss information from receiver
				 */
				lossinfo = lossqueue.take();
				boolean[] missed_packets = lossinfo.getMissingPackets();

			
				/*
				 * Prepare text messages for GUI about packet loss and what packets were missed
				 */
				CharSequence status_text = "Received "+lossinfo.amount_received+" out" +
						" of "+lossinfo.amount_sent+" packets!";

				String missing_text = "Missed packets: ";
				for(int i = 0;i<missed_packets.length;i++){
					if(!missed_packets[i]){
						missing_text = missing_text + i + ", ";
					}
				}

				//Display information on GUI
				status.setText(status_text);
				missed.setText(missing_text);
				
				//Small toast as visual notifier
				Toast toast = Toast.makeText(this, status_text, Toast.LENGTH_SHORT);
				toast.show();
				

				
			}
		}
		
	}
	
	private class SendNotifier extends AsyncTask<Integer, Void, String>{

		@Override
		protected String doInBackground(Integer... params) {
			
			int sleeptime = params[0] * params[1] + 50;
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {  
			
			TextView status = (TextView) findViewById(R.id.statusDisplay);
			status.setText("Done sending!");
			Toast toast = Toast.makeText(getApplicationContext(), "Done sending!", Toast.LENGTH_SHORT);
			toast.show();
		}


		
	}
	
	private class TopologyParser extends AsyncTask<TxtInfo, Void, String[]>{

		@Override
		protected String[] doInBackground(TxtInfo... params) {
			TxtInfo parser = params[0];
			String[][] links_messy = parser.links();
			String[] links = new String[links_messy.length];
			
			for(int i = 0; i<links_messy.length;i++){
				links[i] = links_messy[i][1];
			}
			return links;
			
		}
		
	}
	
	private class MyListener implements DialogInterface.OnClickListener{
		
		String[] links;
		public MyListener(String[] links){
			this.links = links;
		}
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			arg0.dismiss();
			try {
				setLink(InetAddress.getByName(links[arg1]));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

	public static int getPAYLOAD_LENGTH() {
		return PAYLOAD_LENGTH;
	}

	public static int getSendingIntervalMs() {
		return SENDING_INTERVAL_MS;
	}
	
	public static InetAddress getLink(){
		return SENDING_LINK;
	}
	
	public static void setLink(InetAddress newlink){
		SENDING_LINK = newlink;
	}


}
