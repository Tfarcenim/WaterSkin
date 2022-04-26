package tfar.waterskin.client;

import tfar.waterskin.common.main.WaterSkin;
import tfar.waterskin.common.networking.CommonNetworkManager;
import tfar.waterskin.common.networking.ThirstPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

import tfar.waterskin.common.main.ThirstUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.ItemDrink;

public class ClientProxy {
	private static final HydrationHUD hud = new HydrationHUD();

	public static void subscribeHandler() {
		MinecraftForge.EVENT_BUS.register(hud);
		MinecraftForge.EVENT_BUS.register(new ClientProxy());
	}

	@SubscribeEvent
	public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
		if (!event.player.world.isRemote) {
			CommonNetworkManager.sendToClient(new ThirstPacket(event.player), (EntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public void afterTooltip(RenderTooltipEvent.PostText event) {
		ItemStack stack = event.getStack();

		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager()
				.bindTexture(new ResourceLocation(WaterSkin.MODID, "textures/gui/overlay/thirst_overlay.png"));

		GuiScreen gui = mc.currentScreen;
		Triple<Integer,Float,Float> data = ThirstUtils.getDrinkData(stack);

		if (data != null) {

			int thirst = data.getLeft();
			float hydration = data.getMiddle();
			float poisonChance = data.getRight();
			
			ScaledResolution res = new ScaledResolution(mc);
			float calculatedHydration = hydration;
			if (stack.getItem() instanceof ItemDrink) {
				calculatedHydration = Math.min(2 * hydration * thirst, 20);
			}
			float hydrationRemaining = 2f * ((int) calculatedHydration % 2);
			int lengthThirst = (thirst + 1 >> 1) << 3;
			int lengthHydration = 2 + ((int) ((calculatedHydration + 1.99) / 2.0)) * 6;
			int length = Math.max(lengthThirst, lengthHydration);
			GL11.glColor4f(1, 1, 1, 1);
			int baseX = event.getX();
			int baseY = event.getY() + event.getHeight();
			if ((baseY + 29) >= res.getScaledHeight())
				baseY = event.getY() - 33;

			gui.drawTexturedModalRect(baseX - 2, baseY + 3, 0, 42, length + 4, 26);

			gui.drawTexturedModalRect(baseX + length + 2, baseY + 3, 88, 42, 3, 26);
			for (int i = 0; i * 2 < thirst; i++) {
				if (thirst - i * 2 == 1) {
					gui.drawTexturedModalRect(baseX + 2 + i * 8, baseY + 7, 46, 0, 7, 10);
					break;
				}
				gui.drawTexturedModalRect(baseX + 2 + i * 8, baseY + 7, 37, 0, 7, 10);

			}

			for (int i = 0; i * 2 < calculatedHydration; i++) {
				if (calculatedHydration - i * 2 < 2) {
					gui.drawTexturedModalRect(baseX + 2 + i * 6, baseY + 17, ((int) (hydrationRemaining - 1)) * 7, 27, 6, 7);
					break;
				}
				gui.drawTexturedModalRect(baseX + 2 + i * 6, baseY + 17, 21, 27, 6, 7);
			}
		}
	}
}
