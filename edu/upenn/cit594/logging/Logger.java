package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

   private static Logger instance;
   
   private String filename;
   private FileWriter fw;
   private PrintWriter out;
   
   private Logger() {
	   this.filename = null;
	   this.out = null;
	   this.fw = null;
   }
   
   public static Logger getInstance() { 
	   if(instance == null) {
		   instance = new Logger();
	   }
	   return instance; 
   }
   
   public void init(String filename) throws IOException {
	   
	   if(this.filename == null) {
		   this.filename = filename;
		   this.fw = new FileWriter(this.filename, true);
		   this.out = new PrintWriter(fw);
		   return;
		   
	   }
	   else {
		   if(this.filename.equals(filename)) {
			   return;
		   }
		   else {
			   this.filename = filename;
			   this.fw = new FileWriter(this.filename, true);
			   this.out = new PrintWriter(fw);
			   return;   
		   }
	   }
   }
   
   
   public void log(String msg) {
	   out.println(msg);
	   out.flush();
   }
   
}

