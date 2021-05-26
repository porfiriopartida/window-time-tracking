package com.porfiriopartida.timetracking.screen;

import java.util.Map;

public interface ITimeTrackerHandler {
    void updateTimes(Map<String, Long> appTimeMap, long totalTime);

    void newApplicationLoaded(String application, long trackingDelayInMs);

    void updateAppTimeValue(String application, long newVal);

    void displayErrorMessage(String errorMessage);
}
