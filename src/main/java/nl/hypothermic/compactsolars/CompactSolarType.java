// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import net.minecraft.server.ModLoader;
import net.minecraft.server.Block;
import net.minecraft.server.ItemStack;
import ic2.api.Items;

public enum CompactSolarType {

    LV(8, "Low Voltage Solar Array", "lvTransformer", TileEntityCompactSolar.class),
    MV(64, "Medium Voltage Solar Array", "mvTransformer", TileEntityCompactSolarMV.class),
    HV(512, "High Voltage Solar Array", "hvTransformer", TileEntityCompactSolarHV.class);
    
    private final int output;
    public final Class clazz;
    public final String friendlyName, transformerName;
    
    private CompactSolarType(final int output, final String friendlyName, final String transformerName, final Class clazz) {
        this.output = output;
        this.friendlyName = friendlyName;
        this.transformerName = transformerName;
        this.clazz = clazz;
    }
    
    public static void generateRecipes(final BlockCompactSolar blockCompactSolar) {
        ItemStack item = Items.getItem("solarPanel");
        for (final CompactSolarType compactSolarType : values()) {
            final ItemStack itemStack = new ItemStack((Block)blockCompactSolar, 1, compactSolarType.ordinal());
            addRecipe(itemStack, "SSS", "SXS", "SSS", 'S', item, 'X', Items.getItem(compactSolarType.transformerName));
            item = itemStack;
        }
    }
    
    private static void addRecipe(final ItemStack itemStack, final Object... array) {
        ModLoader.addRecipe(itemStack, array);
    }
    
    public int getOutput() {
        return this.output;
    }
    
    public static TileEntityCompactSolar makeEntity(final int n) {
        try {
            return (TileEntityCompactSolar) values()[n].clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public int getTextureRow() {
        return this.ordinal();
    }
    
    public String[] tileEntityNames() {
        return new String[] { "CompactSolarType." + this.name(), this.name(), this.name() + " Solar Array" };
    }
}
