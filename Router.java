/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.io.*;
import java.net.*;

/**
 *
 * @author sg chowdhury
 */
public class Router {
    
	static String fileName = "";
	static FileInputStream file = null;
	static InputStreamReader input = null;
	static BufferedReader fileRead = null;
	static String routerName;
    static ArrayList<String> neighbors = new ArrayList<String>();
    static DistanceVector dv = new DistanceVector();
    static ArrayList<HashMap<String, String>> buffer = new ArrayList<HashMap<String, String>>();
    static DatagramSocket sock;
    static int port;
    static InetAddress ip;
    static HashMap<String,Integer> portTable = new HashMap<>();
    static int count = 0;
    static Scheduler scheduler;
       
    public static void read_file(String filename, boolean resetDV) throws IOException{
        //update DistanceVector
        
        try {
        	file = new FileInputStream(filename);
        	input = new InputStreamReader(file, "UTF-8");
        	fileRead = new BufferedReader(input);
        }
        catch (IOException e) {}
        
        try {
			int numNeighbors = Integer.parseInt(fileRead.readLine());
		} catch (NumberFormatException | IOException e) {}
		
        HashMap<String, String> tempMap = new HashMap<String, String>();
        String line;

        try {
			while ((line = fileRead.readLine()) != null)
			{
				String[] inputs = line.split(" ");
				String name = inputs[0].toUpperCase();
				String value = inputs[1];
				
				neighbors.add(name);
				tempMap.put(name, value + "," + routerName);
			}
		} 
        catch (IOException e) {}
        
        if (resetDV)
        {
        	dv.vector = tempMap;
        }
        
        tempMap.clear();
        
        fileRead.close();
        input.close();
        file.close();
       
    }
    
    public static void send_update(){
    	
    	count++;
    	try {
			read_file(fileName, false);
		} 
    	catch (IOException e1) {}
    	
    	DistanceVector dvCopy = DistanceVector.copy(dv);    	
    	
    	try 
    	{
    		for (int i = 0; i < neighbors.size(); i++)
    		{
    			poisonReverse(neighbors.get(i), dvCopy);
    			byte[] dataOut = DistanceVector.convertToBytes(dvCopy);
    			DatagramPacket pack = new DatagramPacket(dataOut, dataOut.length, ip, portTable.get(neighbors.get(i)));
    			sock.send(pack);
    		}
    	}
    	catch (IOException e) {}
    	
    	display();
    }
    
    
    public static void receive_update() throws ClassNotFoundException, IOException
    {
    	//receive a packet and create dv
		byte[] dataIn = new byte[1024];
		DatagramPacket pack = new DatagramPacket(dataIn, 1024);
		try {
			sock.receive(pack);
		} 
		catch (IOException e) {}
		DistanceVector incomingDV = new DistanceVector();
		incomingDV = DistanceVector.convertFromBytes(dataIn);  
		DVAlgorithm.doDV(dv, incomingDV);
    }

    public static void readPorts() throws FileNotFoundException, IOException {

        File file = new File("Ports.txt"); 

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            String[] str_array = st.split(" ");
            portTable.put(str_array[0], Integer.parseInt(str_array[1]));
        }
        
        br.close();
    }
    
    public static void display()
    {
    	 System.out.println("Output Number: " + count);
         Set keysone = dv.vector.keySet();
         for (Iterator i = keysone.iterator(); i.hasNext();)
         {
             String key = (String) i.next();
             String value = dv.vector.get(key);
             System.out.println("Shortest path " + dv.source + "-" + key+
                     ": the next hop is "+DistanceVector.getThrough(value)+" and the cost is "+DistanceVector.getDistance(value));
                  
         }
    }
    
    public static void poisonReverse(String SendTo, DistanceVector d){
        Set keysone = d.vector.keySet();
        for (Iterator i = keysone.iterator(); i.hasNext();)
        {
            String key = (String) i.next();
            String value = d.vector.get(key);
            
            if(SendTo.equals(DistanceVector.getThrough(value))){
                
                d.vector.put(key,DistanceVector.createValue(Double.MAX_VALUE, value));
                
                
            }
           
        }
   
    }
    
    public static void main(String[] args) throws ClassNotFoundException, IOException 
    {
    	//port number = args[1]
    	//filename = args[0]
    	//router name = args[2]
    	
    	//get arguments
    	boolean inputSuccess = false;
    	
    	try {
    		InetAddress.getByName("localhost");
    	}
    	catch (UnknownHostException e) {}
    	
    	while (!inputSuccess)
    	{
    		try 
    		{
    			fileName = args[0]; //path of dat file for router
    			port = Integer.parseInt(args[1]); //router port
    			routerName = args[2];
    			inputSuccess = true;
    		}
    		catch (Exception e) 
    		{
    			System.out.println("Invalid arguments. <FileName> <portNumber> <routerName>");
    		}
    	}
    	
    	dv.setSource(routerName);
    	
    	try
    	{
    		sock = new DatagramSocket(port);
    	}
    	catch (IOException e) {}
    	
    	try {
			read_file(fileName, true);
		} 
    	catch (IOException e1) {}
    	
    	try {
			readPorts();
		} catch (IOException e) {} 
    	
    	scheduler = new Scheduler(15, routerName);
    	
    	while (true)
    	{
    		//receive a packet and create dv
    		receive_update();
    	}
    }
    
}
