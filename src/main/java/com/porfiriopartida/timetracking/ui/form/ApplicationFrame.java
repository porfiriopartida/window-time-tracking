package com.porfiriopartida.timetracking.ui.form;

import com.porfiriopartida.timetracking.app.TimeTrackingRunner;
import com.porfiriopartida.timetracking.screen.ITimeTrackerHandler;
import com.porfiriopartida.timetracking.ui.components.ApplicationRow;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ApplicationFrame extends JFrame implements ITimeTrackerHandler {
    private JPanel applicationsPanel;
    private JLabel frameTitleLabel;
    private JButton refreshButton;
    private JPanel applicationsListPanel;
    private GridBagConstraints gbc;
    private TimeTrackingRunner timeTrackingRunner;
    private String appFormat = "%s %s";

    public ApplicationFrame(){
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;
        appRows = new HashMap<String, ApplicationRow>();

        setContentPane(applicationsPanel);
        setupListeners();
    }
    public void setupListeners(){
        setupRefreshButtonListener();
    }
    public void refreshFrame(){
        SwingUtilities.updateComponentTreeUI(this);
    }
    private void setupRefreshButtonListener() {
        refreshButton.setVisible(false);
//        refreshButton.addActionListener(e -> {
//            updateAppTimeValue("CHROME", 40);
//        });
    }

    private ApplicationRow addNewApplicationRow(String appName, long appProgress) {
        ApplicationRow newRow = buildNewRow(appName, appProgress);
        ++gbc.gridy;
        applicationsListPanel.add(newRow, gbc);
        refreshFrame();
        return newRow;
    }

    private ApplicationRow buildNewRow(String appName, long appProgress) {
        ApplicationRow applicationRow = new ApplicationRow();
        String hms = toTime(0l);
        applicationRow.getAppLabel().setText(String.format(appFormat, appName, hms));
        applicationRow.setAppName(appName);
        applicationRow.getAppProgress().setValue(0);
        applicationRow.setTimeValue(appProgress);
        return applicationRow;
    }

    public void setTimeTrackingRunner(TimeTrackingRunner timeTrackingRunner) {
        this.timeTrackingRunner = timeTrackingRunner;
    }

    @Override
    public void updateTimes(Map<String, Long> appTimeMap, long totalTime) {
        if(totalTime == 0){
            return;
        }

        SwingUtilities.invokeLater(() -> {

            Set<String> keySet = appTimeMap.keySet();

            for(String key : keySet){
                ApplicationRow applicationRow = appRows.get(key);
                long appTime = applicationRow.getTimeValue();
                int pct = (int) (((double)appTime / totalTime)*100);
                applicationRow.getAppProgress().setValue(pct);
                String hms = toTime(appTime);
                applicationRow.getAppLabel().setText(String.format(appFormat, key, hms));
            }
        });
    }

    private String toTime(Long millis) {
        //https://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        return hms;
    }

    private Map<String, ApplicationRow> appRows;

    @Override
    public void newApplicationLoaded(String applicationName, long trackingDelayInMs) {
        appRows.put(applicationName, addNewApplicationRow(applicationName, trackingDelayInMs));
    }

    @Override
    public void updateAppTimeValue(String applicationName, long trackingDelayInMs) {
        ApplicationRow applicationRow = appRows.get(applicationName);
        applicationRow.setTimeValue(trackingDelayInMs);
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
