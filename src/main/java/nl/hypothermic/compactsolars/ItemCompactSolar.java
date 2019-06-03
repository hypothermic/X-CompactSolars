// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemBlock;

public class ItemCompactSolar extends ItemBlock
{
    public ItemCompactSolar(final int n) {
        super(n);
        this.setMaxDurability(0);
        this.a(true);
    }
    
    public int filterData(final int n) {
        return (n < CompactSolarType.values().length) ? n : 0;
    }
    
    public String a(final ItemStack itemStack) {
        return CompactSolarType.values()[itemStack.getData()].name();
    }
}
