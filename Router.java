/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3ccn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.IOUtils;

/**
 *
 * @author shirupa chowdhury
 * @author lizzy thomas
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
    static DatagramSocket Sendsock;
    static DatagramSocket Rcvsock;
    static int port;

    static InetAddress ip;
    static HashMap<String, Integer> portTable = new HashMap<>();
    static int count = 0;
    static Scheduler scheduler;
    static boolean vectorMade = false;
    static byte[] dataOut;

    public static void read_file(String filename, boolean resetDV) throws IOException {
        //update DistanceVector
        neighbors.clear();

        try {
            file = new FileInputStream(filename);
            input = new InputStreamReader(file, "UTF-8");
            fileRead = new BufferedReader(input);
        } catch (Exception e) {
        }

        try {
            int numNeighbors = Integer.parseInt(fileRead.readLine());
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
        HashMap<String, String> tempMap = new HashMap<String, String>();
        String line;

        while ((line = fileRead.readLine()) != null) {
            String[] inputs = line.split(" ");
            String name = inputs[0].toUpperCase();
            String value = inputs[1];

            neighbors.add(name);
            tempMap.put(name, value + "," + routerName);
        }

        if (resetDV) {
            dv.vector = (HashMap<String, String>) tempMap.clone();
            vectorMade = true;
        }

        tempMap.clear();
        fileRead.close();
        input.close();
        file.close();

    }

    public static void send_update() {

        count++;
        try {
            read_file(fileName, false);
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!vectorMade) {

            try {
                read_file(fileName, true);
            } catch (IOException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        

        try {
            for (int i = 0; i < neighbors.size(); i++) {
                DistanceVector dvCopy = DistanceVector.copy(dv);
                
                if (!neighbors.get(i).equals(routerName)) {
                    poisonReverse(neighbors.get(i), dvCopy);
                    dataOut = DistanceVector.convertToBytes(dvCopy);
                    int portno = portTable.get(neighbors.get(i)) + 1;
                    DatagramPacket pack = new DatagramPacket(dataOut, dataOut.length, ip, portno);

                    Sendsock.send(pack);
                    System.out.println(" Sending to port " + portno);
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }

        display();
        scheduler = new Scheduler(15, routerName);
    }

    public static void receive_update() {
        //receive a packet and create dv
        byte[] dataIn = new byte[1024];
        DatagramPacket pack = new DatagramPacket(dataIn, 1024);
        try {
            System.out.println("Waiting still!!");
            Rcvsock.receive(pack);
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
        DistanceVector incomingDV = new DistanceVector();
        try {
            incomingDV = DistanceVector.convertFromBytes(dataIn);
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Received update from " + incomingDV.source);
        dv = DVAlgorithm.doDV(dv, incomingDV);
    }

    public static void readPorts() throws FileNotFoundException, IOException {

        File file = new File("D:\\Ports.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            String[] str_array = st.split(" ");
            portTable.put(str_array[0], Integer.parseInt(str_array[1]));
        }
    }

    public static void display() {
        System.out.println("Output Number: " + count);
        Set keysone = dv.vector.keySet();
        for (Iterator i = keysone.iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = dv.vector.get(key);
            System.out.println("Shortest path " + dv.source + "-" + key
                    + ": the next hop is " + DistanceVector.getThrough(value) + " and the cost is " + DistanceVector.getDistance(value));

        }
    }

    public static void poisonReverse(String SendTo, DistanceVector d) {
        Set keysone = d.vector.keySet();
        for (Iterator i = keysone.iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = d.vector.get(key);

            if (SendTo.equals(DistanceVector.getThrough(value))) {

                d.vector.put(key, DistanceVector.createValue(Double.MAX_VALUE, value));

            }

        }

    }

    public static void main(String[] args) {
        boolean inputSuccess = false;
        try {
            ip = InetAddress.getByName("localhost");
            //port number = args[1]
            //filename = args[0]

            //get arguments
        } catch (UnknownHostException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!inputSuccess) {
            try {
                fileName = args[0]; //path of dat file for router
                port = Integer.parseInt(args[1]); //router port
                routerName = args[2];
                inputSuccess = true;
            } catch (Exception e) {
                System.out.println("Invalid arguments. <FileName> <portNumber>");
            }
        }

        //get router name and save in its distance vector
        // String temp[] = fileName.split(".");
        //routerName = temp[0].toUpperCase();
        dv.setSource(routerName);

        try {
            Sendsock = new DatagramSocket(port);
            Rcvsock = new DatagramSocket(port + 1);
        } catch (SocketException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            read_file(fileName, true);
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            readPorts();
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }

        scheduler = new Scheduler(15, routerName);

        while (true) {
            //receive a packet and create dv
            System.out.println("Waiting for an update!! ");
            receive_update();

        }
    }

}
