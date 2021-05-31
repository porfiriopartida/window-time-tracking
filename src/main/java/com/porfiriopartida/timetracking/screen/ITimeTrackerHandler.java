package com.porfiriopartida.timetracking.screen;

import java.util.Map;

public interface ITimeTrackerHandler {
    void updateTimes(Map<WindowScreen, Long> appTimeMap, long totalTime);

    void newApplicationLoaded(WindowScreen application, long trackingDelayInMs);

    void updateAppTimeValue(WindowScreen application, long newVal);

    void displayErrorMessage(String errorMessage);
}
