package com.porfiriopartida.timetracking.app;

import org.springframework.boot.CommandLineRunner;

import java.io.File;

public class TimeTrackingRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        for (int i = 0; i < args.length; i++) {
            if("--file".equals(args[i])){
                externalConfigFile = args[i + 1];
            }
        }
        WindowTimeTracker windowTimeTracker = new WindowTimeTracker();

        if (!StringUtils.isBlank(externalConfigFile) && new File(externalConfigFile).exists()) {
            windowTimeTracker.setConfigFile(externalConfigFile);
        } else {
            System.err.println("Invalid file given. Usage: --file path/to/config/file.txt");
            System.exit(1);
        }

        windowTimeTracker.start();
    }
}
