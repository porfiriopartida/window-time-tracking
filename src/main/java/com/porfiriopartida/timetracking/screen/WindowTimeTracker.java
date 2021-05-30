package com.porfiriopartida.timetracking.screen;

import com.porfiriopartida.exception.ConfigurationValidationException;
import com.porfiriopartida.screen.application.ScreenApplication;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class WindowTimeTracker extends ScreenApplication implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(WindowTimeTracker.class);
    private Map<String, Long> appTimeMap;
    private long trackingDelayInMs;
    private long saveDelay = 2000;
    private String application;
    public ITimeTrackerHandler timeTrackerHandler;

    public WindowTimeTracker(){
        appTimeMap = new HashMap<String, Long>();
        trackingDelayInMs = 200;
    }

    public void setTimeTrackerHandler(ITimeTrackerHandler timeTrackerHandler) {
        this.timeTrackerHandler = timeTrackerHandler;
    }

    public void start() throws IOException, ConfigurationValidationException {
        super.start();
        new Thread(this).start();
    }

    @Override
    public void update(Observable observable, Object arg) {
        String windowName = arg.toString();
        LOGGER.debug(String.format("New window name: %s", windowName));
        application = getCommand(windowName);
    }
    private long totalTime = 0;
    public void notifyMap(){
        if (timeTrackerHandler != null) {
            timeTrackerHandler.updateTimes(appTimeMap, totalTime);
        }
        if(LOGGER.isDebugEnabled()){
            Set<String> keys = appTimeMap.keySet();
            StringBuilder mapString = new StringBuilder();
            for (String key : keys){
                mapString.append(String.format("%s: %s", key, toTime(appTimeMap.get(key))));
                mapString.append("\n");
            }
            LOGGER.debug(mapString.toString());
        }
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
            if(StringUtils.isEmpty(application)){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LOGGER.error("error sleeping", e);
                }
                continue;
            }
            try {
                Thread.sleep(trackingDelayInMs);
                saveDelay -= trackingDelayInMs;

                trackTime();

            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error("error tracking time", e);
            }

            if(saveDelay <= 0){
                saveDelay = this.saveDelay;
                notifyMap();
            }
        }
    }

    private void trackTime() {
        if(StringUtils.isEmpty(application)){
            return;
        }
        if(!appTimeMap.containsKey(application)){
            appTimeMap.put(application, trackingDelayInMs);

            if (timeTrackerHandler != null) {
                timeTrackerHandler.newApplicationLoaded(application, trackingDelayInMs);
            }
            return;
        }

        long val = appTimeMap.get(application);
        long newVal = val + trackingDelayInMs;
        totalTime += trackingDelayInMs;
        appTimeMap.put(application, newVal);


        if (timeTrackerHandler != null) {
            timeTrackerHandler.updateAppTimeValue(application, newVal);
        }
    }

    public void displayErrorMessage(String errorMessage) {
        LOGGER.error(errorMessage);
        timeTrackerHandler.displayErrorMessage(errorMessage);
    }
}