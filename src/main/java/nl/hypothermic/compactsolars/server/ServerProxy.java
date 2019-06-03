// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars.server;

import net.minecraft.server.IInventory;
import nl.hypothermic.compactsolars.ContainerCompactSolar;
import nl.hypothermic.compactsolars.TileEntityCompactSolar;
import net.minecraft.server.World;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.EntityItem;
import java.lang.reflect.Field;
import net.minecraft.server.ModLoader;
import nl.hypothermic.compactsolars.CompactSolarType;
import java.util.Map;
import net.minecraft.server.TileEntity;
import java.io.File;
import nl.hypothermic.compactsolars.IProxy;

public class ServerProxy implements IProxy
{
    @Override
    public File getMinecraftDir() {
        return new File(".");
    }
    
    @Override
    public void registerTranslations() {
    }
    
    @Override
    public void registerTileEntities() {
        try {
            final Field declaredField = TileEntity.class.getDeclaredField("a");
            declaredField.setAccessible(true);
            final Map map = (Map)declaredField.get(null);
            for (final CompactSolarType compactSolarType : CompactSolarType.values()) {
                final String[] tileEntityNames = compactSolarType.tileEntityNames();
                ModLoader.registerTileEntity(compactSolarType.clazz, tileEntityNames[0]);
                for (int j = 1; j < tileEntityNames.length; ++j) {
                    map.put(tileEntityNames[j], compactSolarType.clazz);
                }
            }
        }
        catch (Exception ex) {
            ModLoader.getLogger().severe("A fatal error occured initializing CompactSolars!");
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public boolean isRemote() {
        return false;
    }
    
    @Override
    public void registerRenderInformation() {
    }
    
    @Override
    public void applyExtraDataToDrops(final EntityItem entityItem, final NBTTagCompound tag) {
        entityItem.itemStack.setTag(tag);
    }
    
    public Object getGuiElement(final int n, final EntityHuman entityHuman, final World world, final int n2, final int n3, final int n4) {
        final TileEntity tileEntity = world.getTileEntity(n2, n3, n4);
        if (tileEntity != null && tileEntity instanceof TileEntityCompactSolar) {
            final TileEntityCompactSolar tileEntityCompactSolar = (TileEntityCompactSolar)tileEntity;
            return new ContainerCompactSolar((IInventory)entityHuman.inventory, tileEntityCompactSolar, tileEntityCompactSolar.getType());
        }
        return null;
    }
}
