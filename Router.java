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
    
	FileInputStream file = null;
	InputStreamReader input = null;
	BufferedReader fileRead = null;
	String routerName;
    ArrayList<String> neighbors = new ArrayList<String>();
    DistanceVector dv = new DistanceVector();
    ArrayList<HashMap<String, String>> buffer = new ArrayList<HashMap<String, String>>();
    DatagramSocket sock;
    int[] ports = new int[30];
    int port;
    InetAddress ip = InetAddress.getByName("localhost");
    String[] hops;
   
    
    //ports > 9500

    public Router() {
        
    	port = 9500;
    	
    	//?
    	while (isLocalPortInUse())
    	{
    		port += 10; 
    	}
    	
    	
    	
    }
    
    
    public void read_file(String filename){
        //update DistanceVector
    	
    	String temp[] = filename.split(".");
    	RouterName = temp[0].toUpperCase();
    	DistanceVector.setSource(routerName);
        
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
        	String name = inputs[0];
        	String value = inputs[1];
        	
        	neighbors.add(name);
        	tempMap.put(name, value + "," + routerName);
        }
        
        dv.vector = tempMap;
        tempMap.clear();
        
        finally {
            IOUtils.closeQuietly(fileRead);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(file);
        }
    }

    public void start(){
        
    	while (true)
    	{
    		byte[] dataIn = new byte[1024];
    		DatagramPacket pack = new DatagramPacket(data, 1024);
    		sock.receive(pack);
    		int packLen = pack.getLength();
    		String info = pack.getData().toString(); //?
    		buffer.add() //convert info to hashmap and add
    		
    	}
    }
    
    public static void send_update(){
    	try 
    	{
    		for (int i = 0; i < neighbors.size(); i++)
    		{
    			String dataOut = ""; //dv?
    			DatagramPacket pack = new DatagrapmPacket(dataOut.getBytes(), dataOut.getBytes().length);
    			packet.setAddress(ip);
    			packet.setPort(ports[i]);
    			sock.send(pack);
    		}
    	}
    	catch (IOException e) {}
    }
    
    public void recompute_onRcv(){
        //read the file - read_file 
        
        
    }
    
    public void recompute_onSend(){
        
    	
    }
    
    private static boolean isLocalPortInUse(int port) {
        try {
            // ServerSocket try to open a LOCAL port
            new ServerSocket(port).close();
            // local port can be opened, it's available
            return false;
        } catch(IOException e) {
            // local port cannot be opened, it's in use
            return true;
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}