// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import net.minecraft.server.Block;
import java.util.ArrayList;
import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.BaseMod;
import minecraft.server.mod_CompactSolars;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.World;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Material;
import java.util.Random;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;

public class BlockCompactSolar extends BlockContainer implements ITextureProvider
{
    private Random random;
    
    public BlockCompactSolar(final int n) {
        super(n, Material.ORE);
        this.a("CompactSolar");
        this.c(3.0f);
        this.random = new Random();
        this.j();
    }
    
    public TileEntity a_() {
        return null;
    }
    
    public TileEntity getBlockEntity(final int n) {
        return CompactSolarType.makeEntity(n);
    }
    
    public int getBlockTexture(final IBlockAccess blockAccess, final int n, final int n2, final int n3, final int n4) {
        final CompactSolarType compactSolarType = CompactSolarType.values()[blockAccess.getData(n, n2, n3)];
        return (n4 == 1) ? (compactSolarType.getTextureRow() * 16 + 1) : ((n4 == 0) ? (compactSolarType.getTextureRow() * 16 + 2) : (compactSolarType.getTextureRow() * 16));
    }
    
    public int a(final int n, final int n2) {
        final CompactSolarType compactSolarType = CompactSolarType.values()[n2];
        switch (n) {
            case 0: {
                return compactSolarType.getTextureRow() * 16 + 2;
            }
            case 1: {
                return compactSolarType.getTextureRow() * 16 + 1;
            }
            default: {
                return compactSolarType.getTextureRow() * 16;
            }
        }
    }
    
    public String getTextureFile() {
        return "/cpw/mods/compactsolars/sprites/block_textures.png";
    }
    
    public boolean interact(final World world, final int n, final int n2, final int n3, final EntityHuman entityHuman) {
        if (entityHuman.isSneaking()) {
            return false;
        }
        if (world.isStatic) {
            return true;
        }
        final TileEntity tileEntity = world.getTileEntity(n, n2, n3);
        if (tileEntity != null && tileEntity instanceof TileEntityCompactSolar) {
            entityHuman.openGui((BaseMod)mod_CompactSolars.instance, ((TileEntityCompactSolar)tileEntity).getType().ordinal(), world, n, n2, n3);
        }
        return true;
    }
    
    protected int getDropData(final int n) {
        return n;
    }
    
    public void remove(final World world, final int n, final int n2, final int n3) {
        final TileEntityCompactSolar tileEntityCompactSolar = (TileEntityCompactSolar)world.getTileEntity(n, n2, n3);
        if (tileEntityCompactSolar != null) {
            this.dropContent(0, tileEntityCompactSolar, world);
        }
        super.remove(world, n, n2, n3);
    }
    
    public void dropContent(final int n, final TileEntityCompactSolar tileEntityCompactSolar, final World world) {
        for (int i = n; i < tileEntityCompactSolar.getSize(); ++i) {
            final ItemStack item = tileEntityCompactSolar.getItem(i);
            if (item != null) {
                final float n2 = this.random.nextFloat() * 0.8f + 0.1f;
                final float n3 = this.random.nextFloat() * 0.8f + 0.1f;
                final float n4 = this.random.nextFloat() * 0.8f + 0.1f;
                while (item.count > 0) {
                    int count = this.random.nextInt(21) + 10;
                    if (count > item.count) {
                        count = item.count;
                    }
                    final ItemStack itemStack = item;
                    itemStack.count -= count;
                    final EntityItem entityItem = new EntityItem(world, (double)(tileEntityCompactSolar.x + n2), (double)(tileEntityCompactSolar.y + ((n > 0) ? 1 : 0) + n3), (double)(tileEntityCompactSolar.z + n4), new ItemStack(item.id, count, item.getData()));
                    final float n5 = 0.05f;
                    entityItem.motX = (float)this.random.nextGaussian() * n5;
                    entityItem.motY = (float)this.random.nextGaussian() * n5 + 0.2f;
                    entityItem.motZ = (float)this.random.nextGaussian() * n5;
                    if (item.hasTag()) {
                        mod_CompactSolars.proxy.applyExtraDataToDrops(entityItem, (NBTTagCompound)item.getTag().clone());
                    }
                    world.addEntity((Entity)entityItem);
                }
            }
        }
    }
    
    public void addCreativeItems(final ArrayList list) {
        final CompactSolarType[] values = CompactSolarType.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            list.add(new ItemStack((Block)this, 1, values[i].ordinal()));
        }
    }
}
