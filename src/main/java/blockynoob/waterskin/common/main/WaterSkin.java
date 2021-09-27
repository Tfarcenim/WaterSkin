package blockynoob.waterskin.common.main;

import blockynoob.waterskin.client.ClientProxy;
import blockynoob.waterskin.common.networking.CommonNetworkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = WaterSkin.MODID, name = WaterSkin.NAME, version = WaterSkin.VERSION, clientSideOnly = true)
public class WaterSkin {
    public static final String MODID = "waterskin";
    public static final String NAME = "Water Skin";
    public static final String VERSION = "1.0.1";
    public static final String MC_VERSION = "[1.12.2]";

    private static Logger logger;

    @Instance
    public static WaterSkin instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        instance = this;
        ClientProxy.subscribeHandler();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        CommonNetworkManager.init();
        logger.info(WaterSkin.NAME + " is in the Init phase.");
    }
}
