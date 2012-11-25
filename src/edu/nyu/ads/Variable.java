package edu.nyu.ads;

public class Variable {
	private String name;
	private int value;
	private boolean isReplicated;
	
	public Variable(Variable v){
		this.name=v.name;
		this.value=v.value;
		this.isReplicated=v.isReplicated;
	}
	
	public Variable(String name,int value){
		if(name==null){
			throw new NullPointerException("Cannot have a null name");
		}
		this.name=name;
		this.value=value;
		isReplicated=false;
	}
	
	public String getName(){
		return this.name;
	}
	
	boolean isVariableReplicated(){
		return this.isReplicated;
	}
	
	void setIsReplicated(boolean replicated){
		this.isReplicated=replicated;
	}
	
	void setValue(int value){
		return;
	}
	int getValue(){
		return this.value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Variable other = (Variable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
