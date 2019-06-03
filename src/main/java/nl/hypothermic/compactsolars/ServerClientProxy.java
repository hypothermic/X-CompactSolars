// 
// Decompiled by Procyon v0.5.30
// 

package nl.hypothermic.compactsolars;

import net.minecraft.server.ModLoader;

public enum ServerClientProxy
{
    CLIENT("cpw.mods.compactsolars.client.ClientProxy"), 
    SERVER("cpw.mods.compactsolars.server.ServerProxy");
    
    private String className;
    
    private ServerClientProxy(final String className) {
        this.className = className;
    }
    
    private IProxy buildProxy() {
        try {
            return (IProxy)Class.forName(this.className).newInstance();
        }
        catch (Exception ex) {
            ModLoader.getLogger().severe("A fatal error has occured initializing CompactSolars");
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
    
    public static IProxy getProxy() {
        try {
            ModLoader.class.getMethod("getMinecraftInstance", (Class<?>[])new Class[0]);
        }
        catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
        catch (NoSuchMethodException ex2) {
            return ServerClientProxy.SERVER.buildProxy();
        }
        return ServerClientProxy.CLIENT.buildProxy();
    }
}
