// 
// Decompiled by Procyon v0.5.30
// 

package minecraft.server;

import forge.*;
import net.minecraft.server.BaseMod;
import net.minecraft.server.Block;
import net.minecraft.server.ModLoader;
import net.minecraft.server.SidedProxy;
import nl.hypothermic.compactsolars.*;

import java.io.File;

public class mod_CompactSolars extends NetworkMod
{
    @SidedProxy(clientSide = "cpw.mods.compactsolars.client.ClientProxy", serverSide = "nl.hypothermic.compactsolars.server.ServerProxy")
    public static IProxy proxy;
    public static BlockCompactSolar compactSolarBlock;
    public static int productionRate;
    public static mod_CompactSolars instance;
    
    public String getVersion() {
        return Version.version();
    }
    
    public void load() {
        MinecraftForge.versionDetect("CompactSolars", 3, 2, 4);
        if (mod_CompactSolars.instance == null) {
            mod_CompactSolars.instance = this;
        }
        MinecraftForge.setGuiHandler((BaseMod) mod_CompactSolars.instance, (IGuiHandler) mod_CompactSolars.proxy);
        final Configuration configuration = new Configuration(new File(mod_CompactSolars.proxy.getMinecraftDir(), "config/IC2CompactSolars.cfg"));
        try {
            configuration.load();
            final Property orCreateBlockIdProperty = configuration.getOrCreateBlockIdProperty("compactSolar", 183);
            orCreateBlockIdProperty.comment = "The block id for the compact solar arrays.";
            if (mod_CompactSolars.compactSolarBlock == null) {
                mod_CompactSolars.compactSolarBlock = new BlockCompactSolar(orCreateBlockIdProperty.getInt(183));
            }
            final Property orCreateIntProperty = configuration.getOrCreateIntProperty("scaleFactor", "general", 1);
            orCreateIntProperty.comment = "The EU generation scaling factor. The average number of ticks needed to generate one EU packet.1 is every tick, 2 is every other tick etc. Each Solar will still generate a whole packet (8, 64, 512 EU).";
            mod_CompactSolars.productionRate = orCreateIntProperty.getInt(1);
            final Property sunUpdateTime = configuration.getOrCreateIntProperty("sunUpdateTime", "general", 256);
            sunUpdateTime.comment = "Check if the sunlight is valid every X ticks. (standard=64)";
            mod_CompactSolars.productionRate = sunUpdateTime.getInt(256);
        }
        catch (Exception ex) {
            ModLoader.getLogger().severe("CompactSolars was unable to load it's configuration successfully");
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
        finally {
            configuration.save();
        }
        ModLoader.registerBlock((Block) mod_CompactSolars.compactSolarBlock, ItemCompactSolar.class);
        mod_CompactSolars.proxy.registerTranslations();
        mod_CompactSolars.proxy.registerTileEntities();
        mod_CompactSolars.proxy.registerRenderInformation();
    }
    
    public void modsLoaded() {
        CompactSolarType.generateRecipes(mod_CompactSolars.compactSolarBlock);
    }
    
    public boolean clientSideRequired() {
        return true;
    }
    
    public boolean serverSideRequired() {
        return false;
    }
    
    public String getPriorities() {
        return "after:mod_IC2";
    }
    
    static {
        mod_CompactSolars.productionRate = 1;
    }
}
