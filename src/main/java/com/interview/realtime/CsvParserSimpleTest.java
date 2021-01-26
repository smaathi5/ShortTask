package com.interview.realtime;

import static org.junit.jupiter.api.Assertions.assertTimeout;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class CsvParserSimpleTest extends TestCase {

	/**
	 * Notes for unit testing
	 * Basic CSV : (redundant if subscribed to APNs)
	 * <a> if CSV file exists 
	 * <b> basic checks of reading file and exception handling
	 * <c> check file has data or empty file
	 * 
	 * 
	 * Basic Data Handling
	 * <a> Schema checks about format of XML
	 * <b> Data validation not null especially key values of data for pair, ask, bid, timestamp
	 * <c> Check validity of pair ignoreCase
	 * <d> Check validity of buy/sell data inconsistencies for particular pair, extreme cases 
	 * <e> TimeStamp basic date/time and stale data checks
	 * <f> Data length and basic type checks for Message class 
	 * <g> Need to do extreme checks for queues and thread handling assertTimeout
	 * <h> 
	 */
	

    @Test
    public void testAddtionException() {
    	CsvReader tester = new CsvReader();
    	//"100001","EUR/USD","1.10001","1.20001","01-06-2020 12:01:01:001"
    	//ask = 1.10001 + 0.1%
        assertEquals(""+(1.10001 + (1.10001*0.001)), tester.processAskPrice("1.10001"));
        assertEquals(""+(1.20001 - (1.20001*0.001)), tester.processBidPrice("1.20001"));
        assertEquals("0.00", tester.processAskPrice(null));
        assertEquals("0.00", tester.processBidPrice(null));
        assertFalse(("").equals(tester.processAskPrice("1.10001")));
        assertFalse(("").equals(tester.processBidPrice("1.20001")));
        assertTrue((""+(1.10001 + (1.10001*0.001))).equals(tester.processAskPrice("1.10001")));
        assertTrue((""+(1.20001 - (1.20001*0.001))).equals(tester.processBidPrice("1.20001")));
    	//TODO: Test for Multi threading issues with addition function
    }
    
    @Test
    public void testMessageBean() {
//    	"100001","EUR/USD","1.10001","1.20001","01-06-2020 12:01:01:001"
    	Message originalMessage = new Message();
    	originalMessage.setPair("EURUSD");
    	originalMessage.setCounter("100001");
    	originalMessage.setAskPrice("1.10001");
    	originalMessage.setBidPrice("1.20001");
    	originalMessage.setTime("01-06-2020 12:01:01:001");
    	
    	Message revisedMessage = new Message();
    	revisedMessage.setPair("EURUSD");
    	revisedMessage.setCounter("100001");
    	revisedMessage.setAskPrice("1.10111001");
    	revisedMessage.setBidPrice("1.19880999");
    	revisedMessage.setTime("01-06-2020 12:01:01:001");

    	Message nullMessage = new Message();
    	nullMessage.setPair(null);
    	nullMessage.setCounter(null);
    	nullMessage.setAskPrice(null);
    	nullMessage.setBidPrice(null);
    	nullMessage.setTime(null);

    	Message nullReturnMessage = new Message();
    	nullReturnMessage.setPair(null);
    	nullReturnMessage.setCounter(null);
    	nullReturnMessage.setAskPrice(null);
    	nullReturnMessage.setBidPrice(null);
    	nullReturnMessage.setTime(null);
    	
    	CsvReader tester = new CsvReader();
    	try {
    		//Check if the csv code is working
        	tester.onMessage(originalMessage);
        	assertNotSame(revisedMessage, originalMessage);
        	assertEquals(revisedMessage, originalMessage);
        	
        	//NULL check
        	tester.onMessage(nullMessage);
        	assertNotSame(nullReturnMessage, nullMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	//TODO: Test for valid pair EURUSD, "EUR/JPY", "GBP/USD" check for valid pair 
    	//TODO: Test for time stamp and expiry of quotes
    	//TODO: Test for limits
    	
    }
}
