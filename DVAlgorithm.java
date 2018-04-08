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
public class DVAlgorithm {

    public static DistanceVector Result = new DistanceVector();

    

    public static DistanceVector doDV(DistanceVector DV, DistanceVector UPDATE) 
    {
        /*
        
        dv.source = A
        dv.vector = B,(8,C) --- meaning distance to B is 8 through C
        
        
        */
        HashMap<String, String> current = DV.vector;
        String currentName = DV.source;
        HashMap<String, String> update = UPDATE.vector;
        String updateName = UPDATE.source;
        
        Double dtoNode = DistanceVector.getDistance(current.get(updateName)) ;
        
        //iterate over current vector
        Set keysone = current.keySet();
        
        //CHECK IF THE NEIGHBORS DISTANCE HAS CHANGED AND UPDATE DISTANCES 
        if(dtoNode!=DistanceVector.getDistance(update.get(currentName)))
        {
            Double newDist = DistanceVector.getDistance(update.get(currentName));
           for(Iterator i = keysone.iterator();i.hasNext();)
           {
                    String keyone = (String) i.next();
           
                    String via = DistanceVector.getThrough(current.get(keyone));
                    Double updatedDist = newDist + DistanceVector.getDistance(update.get(keyone));
                    
                    if(via.equalsIgnoreCase(updateName)){
                        current.put(keyone, DistanceVector.createValue(updatedDist, keyone));
                    }
           }
            
            
        }
        
        Set keys = update.keySet();
        
        for(Iterator i  = keys.iterator();i.hasNext();)
        {
            String key = (String) i.next();
            if(!current.containsKey(key))//if that key is not present in the distance vector then add it
            {
                Double distance = dtoNode  + DistanceVector.getDistance(update.get(key));
                String via = updateName;
                current.put(key,DistanceVector.createValue(distance, via));
                
            }else{
                if(DistanceVector.getDistance(current.get(key)) > (dtoNode  + DistanceVector.getDistance(update.get(key)) ))
                {
                    Double distance = dtoNode  + DistanceVector.getDistance(update.get(key));
                    String via = updateName;
                    current.put(key,DistanceVector.createValue(distance, via));
                 
                }
              
            }
            
            
        }
        Result.source = currentName;
        Result.vector = current;
        
        
        return Result;
    }
    
    public static void main(String[] args)
    {
       
        DistanceVector A = new DistanceVector();
        A.setSource("A");
        HashMap<String,String> dva = new HashMap<>();
        dva.put("D", "7,A");
        dva.put("B", "4,A");
        dva.put("A", "0,A");
        A.vector = dva;
        
        DistanceVector B = new DistanceVector();
        B.setSource("B");
        HashMap<String,String> dvb = new HashMap<>();
        dvb.put("C", "3,B");
        dvb.put("D", "1,B");
        dvb.put("A", "4,B");
        dvb.put("B", "0,B");
        B.vector = dvb;
        
        DistanceVector.display(doDV(A, B));
        
        
        
        
        
        
        
    }
    

}
