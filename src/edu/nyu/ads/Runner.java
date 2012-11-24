package edu.nyu.ads;
import java.io.*;
public class Runner {
	
	private String inputFile;
	private TransactionManager tm;
	
	Runner(String input){
		this.inputFile=input;
		tm=new TransactionManager();
	}
	
	
	void run() throws IOException{
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
					 tm.beginRO(s);
				 }
			  
				 else if(text.startsWith("begin")){  
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 tm.begin(s);
				 }
			  
				 else if(text.contains("dump()")){
					 tm.dump();
				 }
				 else if(text.startsWith("dump(x")){
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 tm.dump(s);
				 }
				 
				 else if(text.startsWith("dump(")){
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 int ii=Integer.parseInt(s);
					 tm.dump(ii);
				 }
			  
				 else if(text.startsWith("end")){  
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 tm.end(s);
				 }
			  
				 else if(text.startsWith("fail")){  
					 String x[]=text.split("\\(");
					 String s=x[1].substring(0,x[1].length()-1);
					 tm.fail(Integer.parseInt(s));
				 }
			  
			  else if(text.startsWith("recover")){  
				  String x[]=text.split("\\(");
				  String s=x[1].substring(0,x[1].length()-1);
				  tm.recover(Integer.parseInt(s));
			  }
			  
			  else if(text.startsWith("R")){
				  //R(T1, x4)
				  String x[]=text.split("\\(");
				  String s[]=x[1].split(",");
				  s[1]=s[1].trim();
				  tm.read(s[0],s[1].substring(0,s[1].length()-1));
				 
			  }
				 
			  else if(text.startsWith("W")){
				  //W(T1, x6,v)
				  String x[]=text.split("\\(");
				  String s[]=x[1].split(",");
				  s[1]=s[1].trim();
				  s[2]=s[2].trim();
				  s[2]=s[2].substring(0,s[2].length()-1);
				  
				  tm.write(s[0],s[1],Integer.parseInt(s[2]));
				 
			  }
			  else{
				  System.out.println("Could NOT PARSE " + text);
			  }
			}
		}
		
	
	}
	
	public static void main(String arg[]) throws Exception{
		Runner r=new Runner("input.txt");
		r.run();
	}
}
