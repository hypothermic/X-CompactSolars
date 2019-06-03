// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Level;
import cpw.mods.fml.common.FMLCommonHandler;
import java.util.Properties;

public class Version
{
    private static String major;
    private static String minor;
    private static String rev;
    private static String build;
    private static String mcversion;
    private static boolean loaded;
    
    private static void init() {
        final InputStream resourceAsStream = Version.class.getClassLoader().getResourceAsStream("compactsolarsversion.properties");
        final Properties properties = new Properties();
        if (resourceAsStream != null) {
            try {
                properties.load(resourceAsStream);
                Version.major = properties.getProperty("compactsolars.build.major.number");
                Version.minor = properties.getProperty("compactsolars.build.minor.number");
                Version.rev = properties.getProperty("compactsolars.build.revision.number");
                Version.build = properties.getProperty("compactsolars.build.build.number");
                Version.mcversion = properties.getProperty("compactsolars.build.mcversion");
            }
            catch (IOException ex) {
                FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "Could not get CompactSolars version information - corrupted installation detected!", ex);
                throw new RuntimeException(ex);
            }
        }
        Version.loaded = true;
    }
    
    public static final String version() {
        if (!Version.loaded) {
            init();
        }
        return Version.major + "." + Version.minor + "." + Version.rev;
    }
}
