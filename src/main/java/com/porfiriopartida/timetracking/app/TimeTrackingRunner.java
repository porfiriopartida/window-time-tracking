package com.porfiriopartida.timetracking.app;

import com.formdev.flatlaf.FlatLightLaf;
import com.porfiriopartida.exception.ConfigurationValidationException;
import com.porfiriopartida.timetracking.screen.WindowTimeTracker;
import com.porfiriopartida.timetracking.ui.form.ApplicationFrame;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TimeTrackingRunner implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTrackingRunner.class);
    private WindowTimeTracker windowTimeTracker;
    private ApplicationFrame frame;

    @Override
    public void run(String... args) {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        setupWindowTimeTrackerAgent();
        setupUIFrame(args);
    }
    private void setupUIFrame(String... args) {
        SwingUtilities.invokeLater(() -> {
            frame = new ApplicationFrame();
            frame.setTimeTrackingRunner(this);
            //[326, 24]
            frame.setSize(380, 400);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("Application Usages");
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setMenuBar(buildMenu());

            windowTimeTracker.setTimeTrackerHandler(frame);

            start(args);
        });
    }

    private MenuBar buildMenu() {
        return null;
    }

    private void setupWindowTimeTrackerAgent() {
//        if(externalConfigFile == null){
//            externalConfigFile = ScreenApplication.class.getClassLoader().getResource( "configuration.csv" ).getPath();
//        }

        windowTimeTracker = new WindowTimeTracker();

    }
    private void start(String... args){
        String externalConfigFile = null;
        for (int i = 0; i < args.length; i++) {
            if("--file".equals(args[i])){
                externalConfigFile = args[i + 1];
            }
        }

        if (StringUtils.isBlank(externalConfigFile) || !new File(externalConfigFile).exists()) {
            String errorMessage = "Invalid file given. Usage: --file path/to/config/file.txt";
            windowTimeTracker.displayErrorMessage(errorMessage);
            frame.dispose();
            return;
        }
        windowTimeTracker.setConfigFile(externalConfigFile);

        try {
            windowTimeTracker.start();
            frame.setVisible(true);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ConfigurationValidationException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public WindowTimeTracker getWindowTimeTracker(){
        return windowTimeTracker;
    }
}