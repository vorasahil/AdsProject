package edu.nyu.ads;
import java.io.*;
import java.util.List;
import java.util.Map;
public class Runner {
	
	private String inputFile;
	private TransactionManager tm;
	
	Runner(String input,Map<String,List<Site>> varToSite,Map<Integer,Site> sites, LockManager lockManager,String outputFile) throws IOException{
		this.inputFile=input;
		tm=new TransactionManager(varToSite,sites,lockManager,outputFile);
	}
	
	
	void run() throws IOException{
		try{
		int timestamp=0;
		tm.timestamp=timestamp;
		BufferedReader in = new BufferedReader(new FileReader(inputFile)); 
		String txt="";
		while (in.ready()) { 
				
			  txt = in.readLine(); 
			  txt=txt.trim();
			  String cmd[]=txt.split(";");
		
			  for(int i=0;i<cmd.length;i++){
				 String text=cmd[i].trim();
			  
				 if(text.startsWith("beginRO")){  
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 System.out.println(s);
					 tm.beginRO(s,timestamp);
				 }
			  
				 else if(text.startsWith("begin")){  
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 tm.begin(s,timestamp);
				 }
			  
				 else if(text.contains("dump()")){
					 tm.dump(timestamp);
				 }
				 else if(text.startsWith("dump(x")){
					 if(!text.contains(".")){					 
						 String x[]=text.split("\\(");
						 String s=x[1].substring(0,x[1].length()-1);
						 tm.dump(s,timestamp);
					 }
					 else{
						 String x[]=text.split("\\(");
						 String s[]=x[1].split("\\.");
						 int site=Integer.parseInt(s[1].substring(0,s[1].length()-1));
						 tm.dump(s[0],site,timestamp);
					 }
				 }
				 
				 else if(text.startsWith("dump(")){
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 int ii=Integer.parseInt(s);
					 tm.dump(ii,timestamp);
				 }
			  
				 else if(text.startsWith("end")){  
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 tm.end(s,timestamp);
				 }
			  
				 else if(text.startsWith("fail")){  
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 tm.fail(Integer.parseInt(s),timestamp);
				 }
			  
			  else if(text.startsWith("recover")){  
				  String x[]=text.split("\\(");
				  String s=x[1].substring(0,x[1].length()-1);
				  tm.recover(Integer.parseInt(s),timestamp);
			  }
			  
			  else if(text.startsWith("R")){
				  //R(T1, x4)
				  String x[]=text.split("\\(");
				  String s[]=x[1].split(",");
				  s[1]=s[1].trim();
				  tm.read(s[0],s[1].substring(0,s[1].length()-1),timestamp);
				 
			  }
				 
			  else if(text.startsWith("W")){
				  //W(T1, x6,v)
				  String x[]=text.split("\\(");
				  String s[]=x[1].split(",");
				  s[1]=s[1].trim();
				  s[2]=s[2].trim();
				  s[2]=s[2].substring(0,s[2].length()-1);
				  
				  tm.write(s[0],s[1],Integer.parseInt(s[2]),timestamp);
				 
			  }
			  else{
				  System.out.println("Could NOT PARSE " + text);
			  }
			}
			  
			timestamp++;
			tm.timestamp=timestamp;
		}
		}
		finally{
		tm.closeFile();
		}
	}

}
