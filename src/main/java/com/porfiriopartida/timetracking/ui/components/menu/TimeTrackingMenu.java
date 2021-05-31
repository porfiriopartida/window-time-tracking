package com.porfiriopartida.timetracking.ui.components.menu;

import com.porfiriopartida.timetracking.app.Command;
import com.porfiriopartida.timetracking.app.Screen;
import com.porfiriopartida.timetracking.ui.events.ITimeTrackMenuListener;

import javax.swing.*;

public class TimeTrackingMenu extends JMenuBar {
    private ITimeTrackMenuListener menuListener;
    public TimeTrackingMenu(ITimeTrackMenuListener listener){
        this.menuListener = listener;
        setupFileMenu();
        setupEditMenu();
        setupViewMenu();
    }

    private JMenu buildThemesMenuItem(){
        //Build
        JMenu viewThemesMenu = new JMenu("Themes");
        JMenuItem viewThemeSystemItem = viewThemesMenu.add("System");
        viewThemeSystemItem.setActionCommand(String.valueOf(Command.SET_THEME_SYSTEM));

        JMenuItem viewThemeDarkNimbusItem = viewThemesMenu.add("Dark Nimbus");
        viewThemeDarkNimbusItem.setActionCommand(String.valueOf(Command.SET_THEME_DARK_NIMBUS));

        // Set action events
        addGenericCommandMultiMenuItemActionListener(viewThemeSystemItem, viewThemeDarkNimbusItem);

        return viewThemesMenu;
    }
    private void setupViewMenu() {
        JMenu viewMenu = new JMenu("View");
        JMenuItem viewApplicationsItem = viewMenu.add("Applications");
        viewApplicationsItem.setActionCommand(String.valueOf(Screen.APPLICATIONS));

        viewMenu.addSeparator();

        JMenuItem viewAllItems = viewMenu.add("All");
        viewAllItems.setActionCommand(String.valueOf(Command.VIEW_ALL_ITEMS));
        JMenuItem viewConfiguredItems = viewMenu.add("Configured");
        viewConfiguredItems.setActionCommand(String.valueOf(Command.VIEW_CONFIGURED_ITEMS));

        viewMenu.addSeparator();

        viewMenu.add(buildThemesMenuItem());

        addGenericScreenMultiMenuItemActionListener(viewApplicationsItem);
        addGenericCommandMultiMenuItemActionListener(viewAllItems, viewConfiguredItems);

//        viewMenu.addSeparator();

        add(viewMenu);
    }


    private void addGenericScreenMultiMenuItemActionListener(JMenuItem... menuItems) {
        for(JMenuItem menuItem : menuItems){
            Screen screen = Screen.valueOf(menuItem.getActionCommand());
            menuItem.addActionListener(e -> genericScreenMenuItemPressed(screen));
        }
    }
    private void addGenericCommandMultiMenuItemActionListener(JMenuItem... menuItems) {
        for(JMenuItem menuItem : menuItems){
            Command command = Command.valueOf(menuItem.getActionCommand());
            menuItem.addActionListener(e -> genericCommandMenuItemPressed(command));
        }
    }
    private void genericScreenMenuItemPressed(Screen screen){
        menuListener.screenSelected(screen);
    }
    private void genericCommandMenuItemPressed(Command command){
        menuListener.executeCommand(command);
    }

    private void setupEditMenu() {
        JMenu file = new JMenu("Edit");
        JMenuItem preferencesItem = file.add("Preferences");
        preferencesItem.setActionCommand(Screen.PREFERENCES.toString());
        addGenericScreenMultiMenuItemActionListener(preferencesItem);
        add(file);
    }

    private void setupFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem openItem = file.add("Open");
        openItem.addActionListener(e -> fileOpenPressed());
        JMenuItem saveItem = file.add("Save");
        saveItem.addActionListener(e -> fileSavePressed());
        file.addSeparator();
        JMenuItem quitItem = file.add("Exit");
        quitItem.addActionListener(e -> fileExitPressed());
        add(file);
    }

    private void fileExitPressed() {
        System.exit(0);
    }

    private void fileOpenPressed() {
        System.out.println("Open Pressed");
    }

    private void fileSavePressed() {
        System.out.println("Save Pressed");
    }
}
