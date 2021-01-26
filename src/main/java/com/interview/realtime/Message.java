package com.interview.realtime;

import com.opencsv.bean.CsvBindByPosition;


public class Message implements Quote {

	@CsvBindByPosition(position = 0)
	private String counter;
	
	@CsvBindByPosition(position = 1)
    private String pair;
	
	@CsvBindByPosition(position = 2)
    private String bidPrice;
	
	@CsvBindByPosition(position = 3)
    private String askPrice;
	
	@CsvBindByPosition(position = 4)
    private String time;

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getPair() {
		return pair;
	}

	public void setPair(String pair) {
		this.pair = pair;
	}

	public String getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((askPrice == null) ? 0 : askPrice.hashCode());
		result = prime * result + ((bidPrice == null) ? 0 : bidPrice.hashCode());
		result = prime * result + ((counter == null) ? 0 : counter.hashCode());
		result = prime * result + ((pair == null) ? 0 : pair.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (askPrice == null) {
			if (other.askPrice != null)
				return false;
		} else if (!askPrice.equals(other.askPrice))
			return false;
		if (bidPrice == null) {
			if (other.bidPrice != null)
				return false;
		} else if (!bidPrice.equals(other.bidPrice))
			return false;
		if (counter == null) {
			if (other.counter != null)
				return false;
		} else if (!counter.equals(other.counter))
			return false;
		if (pair == null) {
			if (other.pair != null)
				return false;
		} else if (!pair.equals(other.pair))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message [counter=" + counter + ", pair=" + pair + ", bidPrice=" + bidPrice + ", askPrice="
				+ askPrice + ", time=" + time + "]";
	}
}
