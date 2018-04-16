/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author sg chowdhury
 */
public class DistanceVector {
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
    
    private static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        } 
    }
       
     private static DistanceVector convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInput in = new ObjectInputStream(bis)) {
            return (DistanceVector)in.readObject();
        } 
    }
    
    
    
    
}
