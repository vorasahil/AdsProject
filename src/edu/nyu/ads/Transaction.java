/**
 * 
 */
package edu.nyu.ads;

import java.util.*;
/**
 *
 *
 */
public abstract class Transaction {
	String name;
	int timestamp;
	TransactionType type;
	TransactionState state;
	boolean read;
	int value;
	boolean justUnblocked=false;
	
	
	Transaction(String name,int timestamp,TransactionType type){
		this.name=name;
		this.timestamp=timestamp;
		this.type=type;
		this.state=TransactionState.Active;
	}

	public boolean blockedOnRead(){
		return this.read;
	}
	
	public TransactionType getType() {
		return type;
	}


	public void setType(TransactionType type) {
		this.type = type;
	}


	public TransactionState getState() {
		return state;
	}


	public void setState(TransactionState state) {
		this.state = state;
	}


	public boolean isRead() {
		return read;
	}


	public void setRead(boolean read) {
		this.read = read;
	}


	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}


	public void setName(String name) {
		this.name = name;
	}


	void block(int value, boolean read) {
		// TODO Auto-generated method stub
		this.state=TransactionState.Blocked;
		this.value=value;
		this.read=read;
		this.justUnblocked=false;
	}

	Map<Boolean,Integer>unblock(){
		this.state = TransactionState.Active;
		Map<Boolean,Integer> map=new HashMap<Boolean,Integer>();
		map.put(read,value);
		this.justUnblocked=true;
		return map;
	}
	
	public boolean jusUnblocked(){
		return this.justUnblocked;
	}
	
	abstract Integer  read (String variable);
	abstract void write(String variable, int value);
	abstract Map<String,String> end(TransactionState s);

	public int getTimestamp() {
		return timestamp;
	}

	public String getName(){
		return this.name;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transaction [name=" + name + ", type=" + type + ", state="
				+ state + "]";
	}
	
}
