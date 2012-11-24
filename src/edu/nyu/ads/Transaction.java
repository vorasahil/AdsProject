/**
 * 
 */
package edu.nyu.ads;

import java.util.*;
/**
 * @author Rameez
 *
 */
public abstract class Transaction {
	String name;
	int timestamp;
	TransactionType type;
	TransactionState state;
	
	Transaction(String name,int timestamp,TransactionType type){
		this.name=name;
		this.timestamp=timestamp;
		this.type=type;
		this.state=TransactionState.Active;
	}
	
	abstract void block();
	abstract int  read (String variable);
	abstract void write(String variable, int value);
	abstract Map<String,String> end();

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + timestamp;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (state != other.state)
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
