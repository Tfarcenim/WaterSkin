package tfar.waterskin.common.main;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tfar.waterskin.client.ClientProxy;
import tfar.waterskin.common.networking.CommonNetworkManager;

@Mod(modid = WaterSkin.MODID, name = WaterSkin.NAME, version = WaterSkin.VERSION, clientSideOnly = true)
public class WaterSkin {
    public static final String MODID = "waterskin";
    public static final String NAME = "Water Skin";
    public static final String VERSION = "1.0.1";
    public static final String MC_VERSION = "[1.12.2]";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientProxy.subscribeHandler();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        CommonNetworkManager.init();
    }
}
