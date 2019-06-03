// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import minecraft.server.mod_CompactSolars;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.EntityHuman;
import ic2.api.ElectricItem;
import net.minecraft.server.Item;
import ic2.api.IElectricItem;
import ic2.api.EnergyNet;
import ic2.api.NetworkHelper;
import ic2.api.Direction;

import java.util.Collections;
import java.util.List;
import net.minecraft.server.ItemStack;
import java.util.Random;
import ic2.api.IWrenchable;
import ic2.api.INetworkUpdateListener;
import ic2.api.INetworkDataProvider;
import ic2.api.IEnergySource;
import net.minecraft.server.IInventory;
import net.minecraft.server.TileEntity;

public class TileEntityCompactSolar extends TileEntity implements IInventory, IEnergySource, INetworkDataProvider, INetworkUpdateListener, IWrenchable
{
    private static Random random;
    private CompactSolarType type;
    private ItemStack[] inventory;
    private boolean initialized;
    public boolean theSunIsVisible;
    private int tick;
    private boolean canRain;
    private boolean noSunlight;
    //private boolean compatibilityMode;
    private static List fields;
    
    public TileEntityCompactSolar() {
        this(CompactSolarType.LV);
    }
    
    public TileEntityCompactSolar(final CompactSolarType type) {
        this.type = type;
        this.inventory = new ItemStack[1];
        this.tick = TileEntityCompactSolar.random.nextInt(64);
    }
    
    public boolean emitsEnergyTo(final TileEntity tileEntity, final Direction direction) {
        return true;
    }
    
    public void q_() {
        /*if (this.compatibilityMode) {
            this.world.setData(this.x, this.y, this.z, this.type.ordinal());
            this.compatibilityMode = false;
        }*/
        if (!this.initialized && this.world != null) {
            if (mod_CompactSolars.proxy.isRemote()) {
                NetworkHelper.requestInitialData(this);
            } else {
                ic2.common.EnergyNet.getForWorld(this.world).addTileEntity(this);
            }
            this.canRain = (this.world.getWorldChunkManager().getBiome(this.x, this.z).g() > 0);
            this.noSunlight = this.world.worldProvider.e;
            this.initialized = true;
        }
        if (!this.noSunlight) {
            if (this.tick-- == 0) {
                this.updateSunState();
                this.tick = 256;
            }
            int generateEnergy = 0;
            if (this.theSunIsVisible && (mod_CompactSolars.productionRate == 1 || TileEntityCompactSolar.random.nextInt(mod_CompactSolars.productionRate) == 0)) {
                generateEnergy = this.generateEnergy();
            }
            if (generateEnergy > 0 && this.inventory[0] != null && Item.byId[this.inventory[0].id] instanceof IElectricItem) {
                generateEnergy -= ElectricItem.charge(this.inventory[0], generateEnergy, this.type.ordinal() + 1, false, false);
            }
            if (generateEnergy > 0) {
                EnergyNet.getForWorld(this.world).emitEnergyFrom(this, generateEnergy);
            }
        }
    }
    
    private void updateSunState() {
        final boolean b = this.canRain && (this.world.x() || this.world.w());
        this.theSunIsVisible = (this.world.e() && !b && this.world.isChunkLoaded(this.x, this.y + 1, this.z));
    }
    
    private int generateEnergy() {
        return (mod_CompactSolars.productionRate != 1.0f && this.world.random.nextFloat() >= mod_CompactSolars.productionRate) ? 0 : this.type.getOutput();
    }
    
    public boolean isAddedToEnergyNet() {
        return this.initialized;
    }
    
    public void onNetworkUpdate(final String s) {
    }
    
    public List getNetworkedFields() {
        return TileEntityCompactSolar.fields;
    }
    
    public int getMaxEnergyOutput() {
        return this.type.getOutput();
    }
    
    public ItemStack[] getContents() {
        return this.inventory;
    }
    
    public int getSize() {
        return 1;
    }
    
    public ItemStack getItem(final int n) {
        return this.inventory[n];
    }
    
    public ItemStack splitStack(final int n, final int n2) {
        if (this.inventory[n] == null) {
            return null;
        }
        if (this.inventory[n].count <= n2) {
            final ItemStack itemStack = this.inventory[n];
            this.inventory[n] = null;
            this.update();
            return itemStack;
        }
        final ItemStack a = this.inventory[n].a(n2);
        if (this.inventory[n].count == 0) {
            this.inventory[n] = null;
        }
        this.update();
        return a;
    }
    
    public void setItem(final int n, final ItemStack itemStack) {
        this.inventory[n] = itemStack;
        if (itemStack != null && itemStack.count > this.getMaxStackSize()) {
            itemStack.count = this.getMaxStackSize();
        }
        this.update();
    }
    
    public String getName() {
        return this.type.name();
    }
    
    public int getMaxStackSize() {
        return 64;
    }
    
    public boolean a(final EntityHuman entityHuman) {
        return this.world == null || (this.world.getTileEntity(this.x, this.y, this.z) == this && entityHuman.e(this.x + 0.5, this.y + 0.5, this.z + 0.5) <= 64.0);
    }
    
    public void f() {
    }
    
    public void g() {
    }
    
    public boolean wrenchCanSetFacing(final EntityHuman entityHuman, final int n) {
        return false;
    }
    
    public short getFacing() {
        return 0;
    }
    
    public void setFacing(final short n) {
    }
    
    public boolean wrenchCanRemove(final EntityHuman entityHuman) {
        return true;
    }
    
    public float getWrenchDropRate() {
        return 1.0f;
    }
    
    public void b(final NBTTagCompound nbtTagCompound) {
        super.b(nbtTagCompound);
        final NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                final NBTTagCompound nbtTagCompound2 = new NBTTagCompound();
                nbtTagCompound2.setByte("Slot", (byte)i);
                this.inventory[i].save(nbtTagCompound2);
                list.add((NBTBase)nbtTagCompound2);
            }
        }
        nbtTagCompound.set("Items", (NBTBase)list);
    }
    
    public void a(final NBTTagCompound nbtTagCompound) {
        /*if (nbtTagCompound.getString("id") != this.type.name()) {
            this.compatibilityMode = true;
        }*/
        super.a(nbtTagCompound);
        final NBTTagList list = nbtTagCompound.getList("Items");
        this.inventory = new ItemStack[this.getSize()];
        for (int i = 0; i < list.size(); ++i) {
            final NBTTagCompound nbtTagCompound2 = (NBTTagCompound)list.get(i);
            final int n = nbtTagCompound2.getByte("Slot") & 0xFF;
            if (n >= 0 && n < this.inventory.length) {
                this.inventory[n] = ItemStack.a(nbtTagCompound2);
            }
        }
    }
    
    public CompactSolarType getType() {
        return this.type;
    }
    
    public void j() {
        if (this.world != null && this.initialized) {
            EnergyNet.getForWorld(this.world).removeTileEntity(this);
        }
        super.j();
    }
    
    public ItemStack splitWithoutUpdate(final int n) {
        if (this.inventory[n] != null) {
            final ItemStack itemStack = this.inventory[n];
            this.inventory[n] = null;
            return itemStack;
        }
        return null;
    }
    
    public void setMaxStackSize(final int n) {
    }
    
    static {
        TileEntityCompactSolar.random = new Random();
        TileEntityCompactSolar.fields = Collections.emptyList();
    }
}
