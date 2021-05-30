package com.porfiriopartida.timetracking.theme;

import com.porfiriopartida.timetracking.ui.components.frame.ApplicationFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

public class ThemeUtil {
    public static Logger LOGGER = LoggerFactory.getLogger(ThemeUtil.class);
    private ThemeUtil(){
        throw new UnsupportedOperationException("Invalid operation.");
    }

    public static void setDarkNimbusTheme(Component that){
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control", new Color(128, 128, 128));
            UIManager.put("info", new Color(128, 128, 128));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
            UIManager.put("text", new Color(230, 230, 230));
            if(that != null){
                SwingUtilities.updateComponentTreeUI(that);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    public static void setSystemTheme(Component that) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if(that != null){
                SwingUtilities.updateComponentTreeUI(that);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
