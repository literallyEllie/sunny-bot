package me.ellie.sunnybot.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ellie on 06/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CommandCooldown {

    private int cooldown;
    private Timer timer;

    public CommandCooldown(int cooldown){
        this.cooldown = cooldown;
        int period = 1000;
        int delay = 1000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(decrement() <= 0) {
                    this.cancel();
                }
            }
        }, delay, period);
    }

    public boolean finished(){
        return cooldown <= 0;
    }

    private int decrement(){
        return --cooldown;
    }

    public void cancel(){
        if(timer != null){
            timer.cancel();
        }
    }

    public int getRemaining() {
        return cooldown;
    }
}
