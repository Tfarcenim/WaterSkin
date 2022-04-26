package tfar.waterskin.common.networking;

import tfar.waterskin.common.main.WaterSkin;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CommonNetworkManager {

	private static SimpleNetworkWrapper gaddonsChannel;

	private static int registryIndex = 0;

	public static void init() {
		gaddonsChannel = NetworkRegistry.INSTANCE.newSimpleChannel(WaterSkin.MODID);
		registerMessages();
	}

	public static <REQ extends IMessage, REP extends IMessage> void registerMessage(
			Class<? extends IMessageHandler<REQ, REP>> handlerClass, Class<REQ> messageClass, Side side) {
		gaddonsChannel.registerMessage(handlerClass, messageClass, registryIndex++, side);
	}

	private static void registerMessages() {
		registerMessage(ThirstPacketHandler.class, ThirstPacket.class, Side.CLIENT);
	}

	public static void sendToClient(IMessage msg, EntityPlayerMP player) {
		gaddonsChannel.sendTo(msg, player);
	}

}
