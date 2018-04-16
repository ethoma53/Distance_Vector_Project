/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.net.*;

/**
 *
 * @author sg chowdhury
 */
public class Router {
    
	String fileName = "";
	FileInputStream file = null;
	InputStreamReader input = null;
	BufferedReader fileRead = null;
	String routerName;
    ArrayList<String> neighbors = new ArrayList<String>();
    DistanceVector dv = new DistanceVector();
    ArrayList<HashMap<String, String>> buffer = new ArrayList<HashMap<String, String>>();
    DatagramSocket sock;
    int port;
    InetAddress ip = InetAddress.getByName("localhost");
    static HashMap<String,Integer> portTable = new HashMap<>();
    int count = 0;
    Scheduler scheduler;
   

    public Router() {
        
    	//port number = args[1]
    	//filename = args[0]
    	
    	//get arguments
    	boolean inputSuccess = false;
    	while (!inputSuccess)
    	{
    		try 
    		{
    			fileName = args[0]; //path of dat file for router
    			port = args[1]; //router port
    			inputSuccess = true;
    		}
    		catch (Exception e) 
    		{
    			System.out.println("Invalid arguments. <FileName> <portNumber>");
    		}
    	}
    	
    	//get router name and save in its distance vector
    	String temp[] = filename.split(".");
    	routerName = temp[0].toUpperCase();
    	dv.setSource(routerName);
    	
    	sock = new DatagramSocket(port);
    	read_file(fileName, true);
    	readPorts();
    	
    	scheduler = new Scheduler(15, routerName);
    }
    
    
    public void read_file(String filename, boolean resetDV){
        //update DistanceVector
        
        try {
        	file = new FileInputStream(filename);
        	input = new InputStreamReader(file, "UTF-8");
        	fileRead = new BufferedReader(input);
        }
        catch (IOException e) {}
        
        int numNeighbors = Integer.parseInt(fileRead.readLine());
        HashMap<String, String> tempMap = new HashMap<String, String>();

        while ((line = fileRead.readLine()) != null)
        {
        	String[] inputs = line.split(" ");
        	String name = inputs[0].toUpperCase();
        	String value = inputs[1];
        	
        	neighbors.add(name);
        	tempMap.put(name, value + "," + routerName);
        }
        
        if (resetDV)
        {
        	dv.vector = tempMap;
        }
        
        tempMap.clear();
        
        finally {
            IOUtils.closeQuietly(fileRead);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(file);
        }
    }
    
    public void send_update(){
    	
    	count++;
    	read_file(fileName, false);
    	
    	DistanceVector dvCopy = DistanceVector.copy(dv);    	
    	
    	try 
    	{
    		for (int i = 0; i < neighbors.size(); i++)
    		{
    			poisonReverse(neighbors.get(i), dvCopy);
    			Byte[] dataOut = DistanceVector.converToBytes(dvCopy);
    			DatagramPacket pack = new DatagrapmPacket(dataOut, dataOut.length, ip, portTable.get(neighbors.get(i)));
    			sock.send(pack);
    		}
    	}
    	catch (IOException e) {}
    	
    	display();
    }
    
    
    public void receive_update()
    {
    	//receive a packet and create dv
		byte[] dataIn = new byte[1024];
		DatagramPacket pack = new DatagramPacket(dataIn, 1024);
		sock.receive(pack);
		DistanceVector incomingDV = new DistanceVector();
		incomingDV = DistanceVector.convertFromBytes(dataIn);  
		DistanceVector.doDV(dv, incomingDV);
    }

    public static void readPorts() throws FileNotFoundException, IOException {

        File file = new File("Ports.txt"); 

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            String[] str_array = st.split(" ");
            portTable.put(str_array[0], Integer.parseInt(str_array[1]));
        }
    }
    
    public void display()
    {
    	 System.out.println("Output Number: " + count);
         Set keysone = d.vector.keySet();
         for (Iterator i = keysone.iterator(); i.hasNext();)
         {
             String key = (String) i.next();
             String value = d.vector.get(key);
             System.out.println("Shortest path " + d.source + "-" + key+
                     ": the next hop is "+DistanceVector.getThrough(value)+" and the cost is "+DistanceVector.getDistance(value));
                  
         }
    }
    
    public void poisonReverse(String SendTo, DistanceVector d){
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
    
    public static void main(String[] args) 
    {
    	while (true)
    	{
    		//receive a packet and create dv
    		receive_update();
 
    	}
    }
    
}
