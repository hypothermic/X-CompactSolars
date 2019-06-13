// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import net.minecraft.server.ItemStack;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.ICrafting;
import net.minecraft.server.Slot;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.IInventory;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Container;

public class ContainerCompactSolar extends Container {

    private final TileEntityCompactSolar tile;
    private boolean theSunIsVisible;
    private boolean initialized;
    private final EntityHuman myPlayer;
    
    public ContainerCompactSolar(final IInventory inventory, final TileEntityCompactSolar tile, final CompactSolarType compactSolarType) {
        this.tile = tile;
        this.myPlayer = ((PlayerInventory)inventory).player;
        this.layoutContainer(inventory, (IInventory)tile, compactSolarType);
    }
    
    private void layoutContainer(final IInventory inventory, final IInventory inventory2, final CompactSolarType compactSolarType) {
        this.a(new Slot(inventory2, 0, 80, 26));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.a(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.a(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }
    
    public void a() {
        super.a();
        for (final ICrafting crafting : (List<ICrafting>) this.listeners) {
            if (this.theSunIsVisible != this.tile.theSunIsVisible || !this.initialized) {
                crafting.setContainerData((Container)this, 0, (int)(this.tile.theSunIsVisible ? 1 : 0));
            }
        }
        this.initialized = true;
        this.theSunIsVisible = this.tile.theSunIsVisible;
    }
    
    public void updateProgressBar(final int n, final int n2) {
        if (n == 0) {
            this.tile.theSunIsVisible = (n2 == 1);
        }
    }
    
    public boolean b(final EntityHuman entityHuman) {
        return this.tile.a(entityHuman);
    }
    
    public ItemStack a(final int n) {
        ItemStack cloneItemStack = null;
        final Slot slot = (Slot) this.e.get(n);
        if (slot != null && slot.c()) {
            final ItemStack item = slot.getItem();
            cloneItemStack = item.cloneItemStack();
            if (n == 0) {
                if (!this.a(item, 1, 37, true)) {
                    return null;
                }
            }
            else if (n < 28) {
                if (!this.a(item, 28, 37, false)) {
                    return null;
                }
            }
            else if (n < 37) {
                if (!this.a(item, 1, 27, false)) {
                    return null;
                }
            }
            else if (!this.a(item, 1, 37, false)) {
                return null;
            }
            if (item.count == 0) {
                slot.set(null);
            }
            else {
                slot.d();
            }
            if (item.count == cloneItemStack.count) {
                return null;
            }
            slot.c(item);
        }
        return cloneItemStack;
    }
    
    public EntityHuman getPlayer() {
        return this.myPlayer;
    }
    
    public IInventory getInventory() {
        return this.tile;
    }
}
