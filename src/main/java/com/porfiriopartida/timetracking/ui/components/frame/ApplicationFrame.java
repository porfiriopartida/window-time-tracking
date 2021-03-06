package com.porfiriopartida.timetracking.ui.components.frame;

import com.porfiriopartida.timetracking.app.Command;
import com.porfiriopartida.timetracking.app.Constants;
import com.porfiriopartida.timetracking.app.Screen;
import com.porfiriopartida.timetracking.app.TimeTrackingRunner;
import com.porfiriopartida.timetracking.screen.ITimeTrackerHandler;
import com.porfiriopartida.timetracking.screen.WindowScreen;
import com.porfiriopartida.timetracking.screen.WindowTimeTracker;
import com.porfiriopartida.timetracking.theme.ThemeUtil;
import com.porfiriopartida.timetracking.ui.components.panels.ApplicationRow;
import com.porfiriopartida.timetracking.ui.components.menu.TimeTrackingMenu;
import com.porfiriopartida.timetracking.ui.components.panels.ApplicationsListScreenManager;
import com.porfiriopartida.timetracking.ui.components.panels.IScreenManager;
import com.porfiriopartida.timetracking.ui.events.ITimeTrackMenuListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class ApplicationFrame extends JFrame implements ITimeTrackerHandler, ITimeTrackMenuListener {
    private JPanel contentPane;
    @Autowired
    private WindowTimeTracker windowTimeTracker;
    @Qualifier(Constants.Beans.PREFERENCES)
    @Autowired
    private IScreenManager applicationsPreferencesPanel;
    @Qualifier(Constants.Beans.APPLICATIONS)
    @Autowired
    private IScreenManager applicationsListPanel;

    private GridBagConstraints panelsGbc;
    private Map<WindowScreen, ApplicationRow> appRows;

    public ApplicationFrame() {
        appRows = new HashMap<WindowScreen, ApplicationRow>();
        setupListeners();
        buildGbc();
        setContentPane(contentPane);
    }

    private void buildGbc() {
        panelsGbc = new GridBagConstraints();
        panelsGbc.gridx = 0;
        panelsGbc.gridy = 0;
        panelsGbc.anchor = GridBagConstraints.NORTH;
        panelsGbc.weighty = 1;
    }

    public void setupListeners() {
        this.setJMenuBar(new TimeTrackingMenu(this));
    }

    public void refreshFrame() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    private ApplicationRow addNewApplicationRow(WindowScreen windowScreen, long appProgress) {
        return ((ApplicationsListScreenManager) applicationsListPanel).addNewApplicationRow(windowScreen, appProgress);
    }

    @Override
    public void updateTimes(Map<WindowScreen, Long> appTimeMap, long totalTime) {
        if (totalTime == 0) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            Set<WindowScreen> keySet = appTimeMap.keySet();

            for (WindowScreen key : keySet) {
                ApplicationRow applicationRow = appRows.get(key);
                if (applicationRow == null) {
                    break;
                }
                long appTime = applicationRow.getTimeValue();
                String hms = toTime(appTime);
                int pct = (int) (((double) appTime / totalTime) * 100);

                applicationRow.getAppProgress().setValue(pct);
                applicationRow.getAppLabel().setText(key.getCommand());
                applicationRow.getHmsLabel().setText(hms);
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

    @Override
    public void newApplicationLoaded(WindowScreen windowScreen, long trackingDelayInMs) {
        appRows.put(windowScreen, addNewApplicationRow(windowScreen, trackingDelayInMs));
    }

    @Override
    public void updateAppTimeValue(WindowScreen applicationName, long trackingDelayInMs) {
        ApplicationRow applicationRow = appRows.get(applicationName);
        applicationRow.setTimeValue(trackingDelayInMs);
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
    }


    @Override
    public void executeCommand(Command command) {
        SwingUtilities.invokeLater(() -> {
            switch (command) {
                case SET_THEME_SYSTEM:
                    ThemeUtil.setSystemTheme(this);
                    break;
                case SET_THEME_DARK_NIMBUS:
                    ThemeUtil.setDarkNimbusTheme(this);
                    break;
                case VIEW_ALL_ITEMS:
                    showItems(false);
                    break;
                case VIEW_CONFIGURED_ITEMS:
                    showItems(true);
                    break;
                case FILE_SAVE:
                    fileSave();
                case FILE_OPEN:
                    fileOpen();
                    break;
            }
        });
    }

    private void fileOpen() {

    }

    private void fileSave() {

    }

    private void showItems(boolean configured) {
        SwingUtilities.invokeLater(() -> {
            this.windowTimeTracker.setTrackingCommandFound(configured);
            Set<WindowScreen> keySet = appRows.keySet();

            for (WindowScreen key : keySet) {
                ApplicationRow applicationRow = appRows.get(key);
                if (applicationRow == null) {
                    continue;
                }
                applicationRow.setVisible(configured ? applicationRow.getWindowScreen().isCommandFound() : true);
            }
        });
    }

    @Override
    public void screenSelected(Screen screen) {
        SwingUtilities.invokeLater(() -> {
            getContentPane().removeAll();
            switch (screen) {
                case PREFERENCES:
                    applicationsPreferencesPanel.getContentPane().setSize(contentPane.getSize());
                    applicationsPreferencesPanel.getContentPane().setPreferredSize(contentPane.getSize());
                    contentPane.add(applicationsPreferencesPanel.getContentPane(), BorderLayout.CENTER);
                    break;
                case APPLICATIONS:
                    applicationsListPanel.getContentPane().setSize(contentPane.getSize());
                    applicationsListPanel.getContentPane().setPreferredSize(contentPane.getSize());
                    contentPane.add(applicationsListPanel.getContentPane(), BorderLayout.CENTER);
                    break;
            }
            refreshFrame();
            revalidate();
            repaint();
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /** Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
