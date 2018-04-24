/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package project3ccn;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author shirupa chowdhury
 * @author lizzy thomas
 */
public class Scheduler {
    Timer timer;
    String name;

    public Scheduler(int seconds,String name) {
        timer = new Timer();
        this.name = name;
        timer.schedule(new SenderTimerTask(), seconds*1000);
	}

   
    class SenderTimerTask extends TimerTask {
        public void run() {
            System.out.println("Run");
            Router.send_update();
            
        }
        
    }

}
