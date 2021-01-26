package com.interview.realtime;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.opencsv.bean.CsvToBeanBuilder;

public class CsvReader implements AutoCloseable {

    private ArrayBlockingQueue<Quote> queue;
    private Logger logger;
    private long lastQueueWarningTime = 0;
    private Boolean ready;
    private List<Thread> threadsRunning = new ArrayList<Thread>();

    private static final Integer MAX_QUEUE_SIZE = 10000;
    private static final Integer HEARTBEAT = 3000;
    private static final double COMMISSION = 0.001; //0.1%
    private static final Integer TIME_OUT = 1000;

    public CsvReader() {
        this.logger = Logger.getLogger(CsvReader.class.getName());
        this.queue = new ArrayBlockingQueue<Quote>(MAX_QUEUE_SIZE);
        
     // Setup heartbeat
        final CsvReader reader = this;
        Thread heartbeat = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(HEARTBEAT);

                        //Listen to messages here
                        //Read CSV file from desk just for demo, in real world it would come via messages anyhow
                        String fileName = "C:\\Malathi\\eclipse-workspace\\interview\\Data.csv";

                        try {
                            List<Message> prices = new CsvToBeanBuilder<Message>(new FileReader(fileName))
                                    .withType(Message.class)
                                    .build()
                                    .parse();

                            prices.forEach(price -> {
                				try {
                					reader.onMessage(price);
                				} catch (Exception e) {
                					e.printStackTrace();
                				}
                			});
                            
                            if (prices != null) {
//                              publish back to message bus
//                          	just for demo publish to console
                            	for (Message message : prices) {
									System.out.println(message);
								}
                             }
                          } catch (Exception e) {
                			e.printStackTrace();
                		}
                    }
                }
                catch (InterruptedException e) { }
            }
        };

        heartbeat.start();
        this.threadsRunning.add(heartbeat);
    }
    
    public void connect() throws Exception {
        this.connectAsync();
        while (true) {}
    }
    
    public void connectAsync() throws Exception {
        this.logger.info("Connecting...");

        this.ready = false;
        //Connect to message bus
        
    }

    public void disconnect() {
        this.ready = false;

        this.logger.info("Disconnected");
        //disconnect to message bus
    }

    public void onMessage(Object price) throws Exception {
        if (price != null) {
        	Message quote = (Message) price;
        	
        	quote.setAskPrice(processAskPrice(quote.getAskPrice()));

        	quote.setBidPrice(processBidPrice(quote.getBidPrice()));
        	
            boolean accepted = queue.offer((Quote) price, TIME_OUT, TimeUnit.MICROSECONDS);
            if (!accepted) {
                onQueueFull();
            }
        }
    }

    /**
     * With an incoming price, process each with a margin (add commission) function, 
     * assume it is simply  -0.1% on bid, +0.1% on ask (subtract from bid, add to ask). 
     * @param price
     * @return String {TODO: Change Strings to Double, check openCSV or get the backup file reader code.}
     */
    public String processAskPrice(String price) {
    	if (price == null) return "0.00";
    	Double askPrice = Double.parseDouble(price);
    	return (""+ (askPrice + askPrice*COMMISSION));
    }
    
    public String processBidPrice(String price) {
    	if (price == null) return "0.00";
    	Double bidPrice = Double.parseDouble(price);
    	return (""+ (bidPrice - bidPrice*COMMISSION));
    }
    
    @Override
	public void close() throws Exception {
        for (Thread t : this.threadsRunning) {
            t.interrupt();
        }
	}

	public Quote getNextQuote() throws InterruptedException {
        return this.queue.take();
	}
	
    public void registerQuoteHandler(final QuoteHandler run) {
        QuoteHandlerThread thread = new QuoteHandlerThread() {
            @Override
            public void onQuote(Quote quote) {
                run.onQuote(quote);
            }
        };
        thread.setClient(this);
        thread.start();
        this.threadsRunning.add(thread);
    }
    
    public int remainingQueueCapacity() {
        return this.queue.remainingCapacity();
    }

    public void onQueueFull() {
        long time = Calendar.getInstance().getTimeInMillis();
        if (time - this.lastQueueWarningTime > 1000) {
            this.logger.warning("Quote queue is full! remove some quotes");
            this.lastQueueWarningTime = time;
        }
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public static void main(String[] args) {
        new CsvReader();
	}
}
