/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3ccn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author shirupa chowdhury
 * @author lizzy thomas
 */
public class DistanceVector implements Serializable {
    String source;
    HashMap<String, String> vector;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public HashMap<String, String> getVector() {
        return vector;
    }

    public void setVector(HashMap<String, String> vector) {
        this.vector = vector;
    }
    public static String createValue(Double Distance,String Through)
    {
        return Distance+","+Through;
    }
    public static String getThrough(String tuple)
    {
       String[] part = tuple.split(",", 2); 
       return part[1];
        
        
    }
    public static Double getDistance(String tuple)
    {
        String[] part = tuple.split(",", 2); 
        return Double.parseDouble(part[0]);
            
    }
    
    public static DistanceVector copy(DistanceVector d){
        DistanceVector dcopy = new DistanceVector();
        dcopy.source = d.source;
        dcopy.vector = new HashMap<String,String>();
        
        Set keysone = d.vector.keySet();
           for(Iterator i = keysone.iterator();i.hasNext();)
           {
               String key = (String) i.next();
               String value= d.vector.get(key);
               
               dcopy.vector.put(key, value);
           }
        
        return dcopy;
    }
    
    public static void display(DistanceVector d){
        System.out.println("------ "+ d.source+" ---------");
        Set keysone = d.vector.keySet();
        for(Iterator i = keysone.iterator();i.hasNext();)
        {
            String key = (String) i.next();
            String value= d.vector.get(key);
            
            System.out.println("-----------"+key+"---->"+value+"------------");
        }
        
        
        
    }
    
    public static byte[] convertToBytes(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
         
    }
       
     public static DistanceVector convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
       ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return (DistanceVector)o.readObject();
         
    }
    
    
    
    
}
