/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author sg chowdhury
 */
public class Router {
    String RouterName;
    ArrayList<String> neighbors;
    HashMap<String,HashMap<String,Double>> DistanceVector;
    ArrayList<HashMap<String,HashMap<String,Double>>> buffer;

    public Router() {
        
    }
    
    
    
    public void read_file(String filename){
        //update DistanceVector
        
    }

    public void start(){
        
    }
    
    public static void send_update(){
        
    }
    
    public void recompute_onRcv(){
        //read the file - read_file 
        
        
    }
    
    public void recompute_onSend(){
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
