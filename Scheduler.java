/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author sg chowdhury
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
            
            Router.send_update();
            
        }
        
    }

}
