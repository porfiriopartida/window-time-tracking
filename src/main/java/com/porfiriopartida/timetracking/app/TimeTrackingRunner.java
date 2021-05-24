package com.porfiriopartida.timetracking.app;

import com.porfiriopartida.exception.ConfigurationValidationException;
import com.porfiriopartida.screen.application.ScreenApplication;
import com.porfiriopartida.timetracking.screen.WindowTimeTracker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.IOException;

public class TimeTrackingRunner implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(TimeTrackingRunner.class);
    @Override
    public void run(String... args) {
        String externalConfigFile = null;
        for (int i = 0; i < args.length; i++) {
            if("--file".equals(args[i])){
                externalConfigFile = args[i + 1];
            }
        }
        if(externalConfigFile == null){
            externalConfigFile = ScreenApplication.class.getClassLoader().getResource( "configuration.csv" ).getPath();
        }
        WindowTimeTracker windowTimeTracker = new WindowTimeTracker();

        if (!StringUtils.isBlank(externalConfigFile) && new File(externalConfigFile).exists()) {
            windowTimeTracker.setConfigFile(externalConfigFile);
        } else {
            System.err.println("Invalid file given. Usage: --file path/to/config/file.txt");
            System.exit(1);
        }

        try {
            windowTimeTracker.start();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ConfigurationValidationException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
