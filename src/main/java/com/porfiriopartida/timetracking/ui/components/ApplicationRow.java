package com.porfiriopartida.timetracking.ui.components;

import javax.swing.*;

public class ApplicationRow extends JPanel {
    private JPanel applicationRowPanel;
    private JProgressBar appProgress;
    private JLabel appLabel;
    public long timeValue;


    public JProgressBar getAppProgress() {
        return appProgress;
    }

    public void setAppProgress(JProgressBar appProgress) {
        this.appProgress = appProgress;
    }
    public long getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(long timeValue) {
        this.timeValue = timeValue;
    }

    public ApplicationRow(){
        appProgress.setMaximum(100);
        add(applicationRowPanel);
    }

    public void setAppName(String appName){
        appLabel.setText(appName);
    }

    public JPanel getApplicationRowPanel() {
        return applicationRowPanel;
    }

    public void setApplicationRowPanel(JPanel applicationRowPanel) {
        this.applicationRowPanel = applicationRowPanel;
    }

    public void setAppLabel(JLabel appLabel) {
        this.appLabel = appLabel;
    }
    public JLabel getAppLabel() {
        return this.appLabel;
    }
}
