package edu.nyu.ads;
import java.util.*;
import java.io.*;
public class TransactionManager {
	PrintWriter out;
	Map<String,List<Site>> varToSite;
	Map<Integer,Site> sites;
	//Map<String,Transaction> transactions;
	Map<String,Transaction> transactions;
	Map<String,Transaction> blockedTransactions;
	LockManager lockManager;
	String outputFile;
	static int timestamp;
                  
	
	TransactionManager(Map<String,List<Site>> varToSite,Map<Integer,Site> sites, LockManager lockManager,String outputFile) throws IOException{
		this.varToSite = new HashMap<String,List<Site>>(varToSite);
		this.sites = new HashMap<Integer,Site>(sites);
		this.outputFile=outputFile;
		this.out = new PrintWriter(new FileWriter(outputFile));
		this.lockManager=lockManager;
		//transactions=new HashMap<String,Transaction>();
		this.transactions=new HashMap<String,Transaction>();
		this.blockedTransactions=new HashMap<String,Transaction>();
	}
	
	void closeFile(){
		out.close();
	}
	
	void block(String variable,Transaction t,boolean read,int value,int timestamp){
		if(blockedTransactions.containsKey(variable)){
			Transaction temp=blockedTransactions.get(variable);
			if(temp.getTimestamp()>t.getTimestamp()){
				
				out.println("Transaction "+t.getName()+" BLOCKED ON "+variable+"at timestamp"+timestamp);
				//remove the transa
				blockedTransactions.put(variable, t);
				t.block(value, read);
				out.println("Wait Die Protocol-->");
				blockedTransactions.put(variable,t);
				abort(temp,timestamp);
			}
			else{
				out.println("Wait Die Protocol-->");
				abort(t,timestamp);
			}
		}
	}
	
	void abort(Transaction t,int timestamp){
		out.println("Transaction "+t.getName()+" aborting at timestamp "+timestamp);
		Map<String, String> m=t.end(TransactionState.Aborted);
		Set<String> variables=m.keySet();
		for(Integer siteNum:sites.keySet()){
			Site site=sites.get(siteNum);
			site.abort(t);
		}
		for(String v:variables){
			if(blockedTransactions.containsKey(v)){
				Transaction temp=blockedTransactions.get(v);
				blockedTransactions.remove(v);
				Map<Boolean,Integer> map=temp.unblock();
				if(map.containsKey(true)){
					//true means it was a read operation that it was blocked on.
					read(t.getName(),v,timestamp);
				}
				else{
					write(t.getName(),v,map.get(false),timestamp);
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param variable Variable name	
	 * @param t Transaction that wants to lock the variable
	 * @param read true if the lock is for "read" and false if the lock is for "write"
	 * @param value the value to be written. This value wont matter if read is true.
	 * @return
	 */
	void lock(String variable,Transaction t,boolean read,int value,int timestamp){
		Status s=lockManager.getLock(t, variable,read);
		if(read){
			if(s.equals(Status.Abort)){
				out.println("Wait Die Protocol--> ");
				abort(t,timestamp);
			}
			if(s.equals(Status.Block)){
				t.block(value, read);
				out.println(t+ "is Blocked, could not get the locks but is older..");
				block(variable,t,read,value,timestamp);
			}
			if(s.equals(Status.GetLock)){
				List<Site> list=varToSite.get(variable);
				for(Site site:list){
					if(site.isUp()){
						Variable v=null;
						try{
							v=site.readVariable(variable);
						}
						catch(IllegalStateException e){
							continue;
						}
						site.lock(variable, t,read);
						t.read(variable);
						out.println(t+ " READS variable "+variable+"."+site.getSiteNumber()+" = "+ v.getValue()+ "at timestamp" + timestamp );
						return;
					}
				}
				
				block(variable,t,read,value,timestamp);
			}
		}
		else{
			if(s.equals(Status.Abort)){
				out.println("Wait Die Protocol-->");
				abort(t,timestamp);
			}
			if(s.equals(Status.Block)){
				t.block(value, read);
				out.println(t+ "is Blocked, could not get the locks but is older..");

				block(variable,t,read,value,timestamp);
			}
			if(s.equals(Status.GetLock)){
				List<Site> list=varToSite.get(variable);
				boolean wrote=false;
				for(Site site:list){
					if(site.isUp()){
						site.lock(variable, t,read);
						site.writeVariable(variable, value);
						t.write(variable, value);
						out.println(t+ " WRITES "+value+" to  variable "+variable+"."+site.getSiteNumber()+ " at timestamp " + timestamp );
						wrote=true;
					}
				}
				if(!wrote){
					block(variable,t,read,value,timestamp);
				}
			}
		}
		
	}
	
	void begin(String s,int timestamp){
		Transaction temp=new ReadWriteTransaction(s, timestamp);
		transactions.put(s,temp);
		out.println("BEGIN READWRITE "+temp);
	}
	
	void beginRO(String s,int timestamp){
		
		Map<String,Variable> variables=new HashMap<String,Variable>();
		for(String var:varToSite.keySet()){
			
			List<Site> siteList=varToSite.get(var);
			
			FOR: for(Site sit:siteList){
				if(sit.isUp()){
					Variable v=null;
					try{
						v=sit.readVariableBackup(var);	
					}
					catch(IllegalStateException e){
					continue;
					}
					variables.put(v.getName(),v);
					break FOR;
				}
				
			}
			
			//check variables map for the entry of this variable
			//if not found, do what prof says...(doubt)
		}
		Transaction temp=new ReadOnlyTransaction(s,timestamp,variables);
		transactions.put(s, temp);
		out.println("BEGIN READONLY "+temp);
	}
	
	boolean end(String s,int timestamp){
		System.out.println("end "+s);
		return false;
	}
	
	boolean read(String transaction, String variable,int timestamp){
		Transaction temp=transactions.get(transaction);
		if(temp.type.equals(TransactionType.ReadOnly)){
			int value=temp.read(variable);
			out.println("Transaction "+transactions.get(transaction)+" reads "+variable+" as "+value+" on timestamp= "+timestamp);
		}
		else{
			lock(variable,transactions.get(transaction),true,0,timestamp);
		}
		return true;
	}
	
	
	boolean write(String transaction, String variable, int value,int timestamp)
	{
		lock(variable,transactions.get(transaction),false,value,timestamp);
		return true;
	}
	
	void dump(int timestamp){
		out.println("COMPLETE DUMP AT TIMESTAMP= "+timestamp);
		for(Integer siteNum:sites.keySet()){
			dump(siteNum,timestamp);
		}
		
	}
	
	
	void dump(int siteNum,int timestamp){
		out.println("DUMPing Site= "+siteNum+" at TimeStamp "+timestamp);
		
			Site site=sites.get(siteNum);
			if(site.isUp()){
				out.println(siteNum+" variables are as follows:");
				out.println(site.dump());
			}
			else{
				out.println(siteNum+" is DOWN.");
			}
			
	}
	
	void dump(String variable,int timestamp){
		out.println("Dumping Variable "+variable+" at timestamp"+timestamp);
		List<Site> siteList=varToSite.get(variable);
			System.out.println();
		for(Site sit:siteList){
			if(sit.isUp()){
				try{
					Variable v=sit.readVariable(variable);	
					out.println("At Site "+sit+": "+v.getValue());
				}
				catch(IllegalStateException e){
					out.println("At Site "+sit+": CANNOT READ, STALE COPY.");
					continue;
				}
			}
			else{
				out.println("Site "+sit+" IS DOWN.");
			}
		}	
		
	}
	
	void fail(int site,int timestamp){
		System.out.println("fail"+site);
	}
	
	void recover(int site,int timestamp){
		System.out.println("recover"+site);
	}
	
	void queryState(){
		
	}

	
}
