package tfar.waterskin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.TANCapabilities;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.thirst.ThirstHandler;
import toughasnails.util.RenderUtils;

public class HydrationHUD {

    //overlays are drawn in priority

    //low = draw AFTER thirst bar
    //high = draw BEFORE thirst bar

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPostRenderOverlayLast(RenderGameOverlayEvent.Pre event) {

        if (event.getType() == ElementType.AIR && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST)) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution resolution = event.getResolution();
            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            EntityPlayerSP player = mc.player;

            ThirstHandler thirstStats = (ThirstHandler) player.getCapability(TANCapabilities.THIRST, null);
            float thirstExhaustion = thirstStats.getExhaustion();

            mc.getTextureManager()
                    .bindTexture(new ResourceLocation("waterskin", "textures/gui/overlay/thirst_overlay.png"));

            drawExhaustion(width, height, thirstExhaustion);
            mc.getTextureManager().bindTexture(Gui.ICONS);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPostRenderOverlayLast2(RenderGameOverlayEvent.Pre event) {

        if (event.getType() == ElementType.AIR && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST)) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution resolution = event.getResolution();
            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            EntityPlayerSP player = mc.player;

            ThirstHandler thirstStats = (ThirstHandler) player.getCapability(TANCapabilities.THIRST, null);
            float thirstHydrationLevel = thirstStats.getHydration();
            float thirstExhaustion = thirstStats.getExhaustion();

            thirstHydrationLevel = Math.max(0, thirstHydrationLevel - 0.25f * thirstExhaustion);
            mc.getTextureManager()
                    .bindTexture(new ResourceLocation("waterskin", "textures/gui/overlay/thirst_overlay.png"));

            drawHydration(width, height, thirstHydrationLevel);
            mc.getTextureManager().bindTexture(Gui.ICONS);
        }
    }



    protected static final int BAR_WIDTH = 81;
    protected static final int BAR_HEIGHT = 9;
    protected static final float MAX_EXHAUSTION = 4;
    protected static final int LEFT_OFFSET = 91;
    protected static final int ATLAS_HEIGHT = 18;

    private static void drawExhaustion(int width, int height, float exhaustion) {
        Minecraft mc = Minecraft.getMinecraft();
        float maxExhaustion = MAX_EXHAUSTION;
        int barWidth = BAR_WIDTH;
        int left = (width / 2) + LEFT_OFFSET;
        int top = height - GuiIngameForge.right_height;
        float emptyRatio = (maxExhaustion - exhaustion) / maxExhaustion;
        float filledRatio = exhaustion / maxExhaustion;
        int emptyTex = (int) (barWidth * emptyRatio);
        int filledTex = (int) (barWidth * filledRatio);

        int x = left - filledTex;
        int y = top;

        mc.ingameGUI.drawTexturedModalRect(x, y, emptyTex, ATLAS_HEIGHT, filledTex, BAR_HEIGHT);
    }

    protected static final int DROPLET_WIDTH = 9;
    protected static final int DROPLET_HEIGHT = 9;

    private static void drawHydration(int width, int height, float thirstHydrationLevel) {
        Minecraft mc = Minecraft.getMinecraft();
        int dropletWidth = DROPLET_WIDTH;
        int dropletHeight = DROPLET_HEIGHT;
        int left = (width / 2) + LEFT_OFFSET;
        int top = height - GuiIngameForge.right_height + 10;//need the offset because TAN shifts the bar up once it's done drawing
        int x = left - dropletWidth;
        int y = top;
        for (int i = 0; 2 * i < thirstHydrationLevel; i++) {
            float rem = thirstHydrationLevel - (2 * i);
            if (rem < 2) {
                RenderUtils.drawTexturedModalRect(x - (dropletWidth - 1) * i, y, dropletWidth * (int) (rem * 2 - 1), 0,
                        dropletWidth, dropletHeight);
            } else {
                RenderUtils.drawTexturedModalRect(x - (dropletWidth - 1) * i, y, dropletWidth * 3, 0, dropletWidth, dropletHeight);
            }
        }
    }
}