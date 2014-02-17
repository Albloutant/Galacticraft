package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreOverlayOxygenWarning.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class OverlayOxygenWarning extends Overlay
{
	private static Minecraft minecraft = FMLClientHandler.instance().getClient();

	private static long screenTicks;

	/**
	 * Render the GUI when player is in inventory
	 */
	public static void renderOxygenWarningOverlay()
	{
		OverlayOxygenWarning.screenTicks++;
		final ScaledResolution scaledresolution = new ScaledResolution(OverlayOxygenWarning.minecraft.gameSettings, OverlayOxygenWarning.minecraft.displayWidth, OverlayOxygenWarning.minecraft.displayHeight);
		final int width = scaledresolution.getScaledWidth();
		final int height = scaledresolution.getScaledHeight();
		OverlayOxygenWarning.minecraft.entityRenderer.setupOverlayRendering();
		// final GCCoreFontRendererLarge fr = new
		// GCCoreFontRendererLarge(GCCoreOverlayOxygenWarning.minecraft.gameSettings,
		// new ResourceLocation("textures/font/ascii.png"),
		// GCCoreOverlayOxygenWarning.minecraft.renderEngine, false);

		GL11.glPushMatrix();

		GL11.glScalef(2.0F, 2.0F, 0.0F);

		OverlayOxygenWarning.minecraft.fontRenderer.drawString(StatCollector.translateToLocal("gui.warning"), width / 4 - OverlayOxygenWarning.minecraft.fontRenderer.getStringWidth(StatCollector.translateToLocal("gui.warning")) / 2, height / 8 - 20, CoreUtil.to32BitColor(255, 255, 0, 0));
		final int alpha = (int) (255 * Math.sin(OverlayOxygenWarning.screenTicks / 20.0F));
		OverlayOxygenWarning.minecraft.fontRenderer.drawString(StatCollector.translateToLocal("gui.oxygen.warning"), width / 4 - OverlayOxygenWarning.minecraft.fontRenderer.getStringWidth(StatCollector.translateToLocal("gui.oxygen.warning")) / 2, height / 8, CoreUtil.to32BitColor(alpha, alpha, alpha, alpha));

		GL11.glPopMatrix();
	}
}
