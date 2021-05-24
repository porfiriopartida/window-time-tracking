package com.porfiriopartida.timetracking.screen;

import com.porfiriopartida.exception.ConfigurationValidationException;
import com.porfiriopartida.screen.application.ScreenApplication;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WindowTimeTracker extends ScreenApplication implements Runnable {
    private Map<String, Long> appTimeMap;
    private long trackingDelayInMs;
    private long saveDelay = 2000;
    private String application;
    public WindowTimeTracker(){
        appTimeMap = new HashMap<String, Long>();
        trackingDelayInMs = 200;
    }

    public void start() throws IOException, ConfigurationValidationException {
        super.start();
        new Thread(this).start();
    }

    @Override
    public void update(Observable observable, Object arg) {
        String windowName = arg.toString();
        System.out.println(String.format("New window name: %s", windowName));
        application = getCommand(windowName);
    }

    public void printMap(){
        Set<String> keys = appTimeMap.keySet();
        StringBuilder mapString = new StringBuilder();
        for (String key : keys){
            mapString.append(String.format("%s: %s", key, toTime(appTimeMap.get(key))));
            mapString.append("\n");
        }

        System.out.println(mapString);
    }

    private String toTime(Long millis) {
        //https://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        return hms;
    }

    @Override
    public void run() {
        long saveDelay = 0;
        while(true){
            if(StringUtils.isBlank(application)){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                Thread.sleep(trackingDelayInMs);
                saveDelay -= trackingDelayInMs;

                trackTime();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(saveDelay <= 0){
                saveDelay = this.saveDelay;
                printMap();
            }
        }
    }

    private void trackTime() {
        if(StringUtils.isBlank(application)){
            return;
        }
        if(!appTimeMap.containsKey(application)){
            appTimeMap.put(application, trackingDelayInMs);
            return;
        }

        long val = appTimeMap.get(application);
        long newVal = val + trackingDelayInMs;
        appTimeMap.put(application, newVal);
    }
}