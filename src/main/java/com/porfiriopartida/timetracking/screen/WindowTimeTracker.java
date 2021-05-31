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
    private Map<WindowScreen, Long> appTimeMap;
    private long trackingDelayInMs;
    private long saveDelay = 500;
    private long totalTime = 0;
    private WindowScreen application;
    public ITimeTrackerHandler timeTrackerHandler;
    private boolean isTrackingCommandFound;

    public WindowTimeTracker(){
        appTimeMap = new HashMap<WindowScreen, Long>();
        trackingDelayInMs = 100;
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
        String command = getCommand(windowName);
        boolean isCommandFound = !StringUtils.isBlank(command);
        WindowScreen ws = new WindowScreen();
        ws.setCommand(isCommandFound ? command:windowName);
        ws.setFullPath(windowName);
        ws.setCommandFound(isCommandFound);
        application = ws;
    }

    public void notifyMap(){
        if (timeTrackerHandler != null) {
            timeTrackerHandler.updateTimes(appTimeMap, totalTime);
        }
        if(LOGGER.isDebugEnabled()){
            Set<WindowScreen> keys = appTimeMap.keySet();
            StringBuilder mapString = new StringBuilder();
            for (WindowScreen key : keys){
                mapString.append(String.format("%s: %s", key.getCommand(), toTime(appTimeMap.get(key))));
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
            if(application == null || StringUtils.isEmpty(application.getCommand())){
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
        //Should we track or not even if not used?
        if(StringUtils.isEmpty(application.getCommand())){ // || isTrackingCommandFound && !application.isCommandFound()
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
        if(!isTrackingCommandFound || application.isCommandFound()) {
            totalTime += trackingDelayInMs;
        }
        appTimeMap.put(application, newVal);


        if (timeTrackerHandler != null) {
            timeTrackerHandler.updateAppTimeValue(application, newVal);
        }
    }

    public void displayErrorMessage(String errorMessage) {
        LOGGER.error(errorMessage);
        timeTrackerHandler.displayErrorMessage(errorMessage);
    }

    public boolean isTrackingCommandFound() {
        return isTrackingCommandFound;
    }

    public void setTrackingCommandFound(boolean trackingCommandFound) {
        if(this.isTrackingCommandFound != trackingCommandFound){
            reloadTotalTime(trackingCommandFound);
        }
        isTrackingCommandFound = trackingCommandFound;
    }

    private void reloadTotalTime(boolean trackingCommandFound) {
        Set<WindowScreen> keys = appTimeMap.keySet();
        StringBuilder mapString = new StringBuilder();
        totalTime = 0;
        for (WindowScreen key : keys){
            if(!trackingCommandFound || key.isCommandFound()){
                totalTime += appTimeMap.get(key);
            }
        }
    }
}