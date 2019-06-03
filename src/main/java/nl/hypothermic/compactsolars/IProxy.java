// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.EntityItem;
import java.io.File;
import forge.IGuiHandler;

public interface IProxy extends IGuiHandler
{
    File getMinecraftDir();
    
    void registerTranslations();
    
    void registerTileEntities();
    
    boolean isRemote();
    
    void registerRenderInformation();
    
    void applyExtraDataToDrops(final EntityItem p0, final NBTTagCompound p1);
}
