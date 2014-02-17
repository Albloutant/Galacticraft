package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.element.InfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenSealer;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

/**
 * GCCoreGuiAirSealer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GuiAirSealer extends GuiAdvancedContainer
{
	private static final ResourceLocation sealerTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/oxygen_large.png");

	private final TileEntityOxygenSealer sealer;
	private GuiButton buttonDisable;

	private InfoRegion oxygenInfoRegion = new InfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height);
	private InfoRegion electricInfoRegion = new InfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height);

	public GuiAirSealer(InventoryPlayer par1InventoryPlayer, TileEntityOxygenSealer par2TileEntityAirDistributor)
	{
		super(new ContainerOxygenSealer(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.sealer = par2TileEntityAirDistributor;
		this.ySize = 200;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.sealer.xCoord, this.sealer.yCoord, this.sealer.zCoord, 0 }));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add("Sealer battery slot, place battery here");
		batterySlotDesc.add("if not using a connected power source");
		this.infoRegions.add(new InfoRegion((this.width - this.xSize) / 2 + 31, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
		List<String> oxygenDesc = new ArrayList<String>();
		oxygenDesc.add("Oxygen Storage");
		oxygenDesc.add(EnumColor.YELLOW + "Oxygen: " + ((int) Math.floor(this.sealer.storedOxygen) + " / " + (int) Math.floor(this.sealer.maxOxygen)));
		this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
		this.oxygenInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
		this.oxygenInfoRegion.yPosition = (this.height - this.ySize) / 2 + 23;
		this.oxygenInfoRegion.parentWidth = this.width;
		this.oxygenInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.oxygenInfoRegion);
		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add("Electrical Storage");
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.sealer.getEnergyStored()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStored())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 36;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 38, this.height / 2 - 30 + 21, 76, 20, StatCollector.translateToLocal("gui.button.enableseal.name")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(this.sealer.getInventoryName(), 8, 10, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.message.in.name") + ":", 87, 25, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.message.in.name") + ":", 87, 37, 4210752);
		String status = StatCollector.translateToLocal("gui.message.status.name") + ": " + this.getStatus();
		this.buttonDisable.enabled = this.sealer.disableCooldown == 0;
		this.buttonDisable.displayString = this.sealer.disabled ? StatCollector.translateToLocal("gui.button.enableseal.name") : StatCollector.translateToLocal("gui.button.disableseal.name");
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 50, 4210752);
		status = "Oxygen Use: " + this.sealer.oxygenPerTick * 20 + "/s";
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 60, 4210752);
		status = ElectricityDisplay.getDisplay(this.sealer.ueWattsPerTick * 20, ElectricUnit.WATT);
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 70, 4210752);
		status = ElectricityDisplay.getDisplay(this.sealer.getVoltage(), ElectricUnit.VOLTAGE);
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 80, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90 + 3, 4210752);
	}

	private String getStatus()
	{
		Block blockAbove = this.sealer.getWorldObj().getBlock(this.sealer.xCoord, this.sealer.yCoord + 1, this.sealer.zCoord);

		if (blockAbove != Blocks.air && blockAbove != GCBlocks.breatheableAir)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.sealerblocked.name");
		}

		if (this.sealer.disabled)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.disabled.name");
		}

		if (this.sealer.getEnergyStored() == 0)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.missingpower.name");
		}

		if (this.sealer.storedOxygen < 1)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.missingoxygen.name");
		}

		if (this.sealer.calculatingSealed)
		{
			return EnumColor.ORANGE + "Checking seal...";
		}

		if (!this.sealer.sealed)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.unsealed.name");
		}
		else
		{
			return EnumColor.DARK_GREEN + StatCollector.translateToLocal("gui.status.sealed.name");
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiAirSealer.sealerTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

		if (this.sealer != null)
		{
			List<String> oxygenDesc = new ArrayList<String>();
			oxygenDesc.add("Oxygen Storage");
			oxygenDesc.add(EnumColor.YELLOW + "Oxygen: " + ((int) Math.floor(this.sealer.storedOxygen) + " / " + (int) Math.floor(this.sealer.maxOxygen)));
			this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

			List<String> electricityDesc = new ArrayList<String>();
			electricityDesc.add("Electrical Storage");
			electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.sealer.getEnergyStored()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStored())));
			this.electricInfoRegion.tooltipStrings = electricityDesc;

			int scale = this.sealer.getCappedScaledOxygenLevel(54);
			this.drawTexturedModalRect(var5 + 113, var6 + 24, 197, 7, Math.min(scale, 54), 7);
			scale = this.sealer.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(var5 + 113, var6 + 37, 197, 0, Math.min(scale, 54), 7);

			if (this.sealer.getEnergyStored() > 0)
			{
				this.drawTexturedModalRect(var5 + 99, var6 + 36, 176, 0, 11, 10);
			}

			if (this.sealer.storedOxygen > 0)
			{
				this.drawTexturedModalRect(var5 + 100, var6 + 23, 187, 0, 10, 10);
			}
		}
	}
}
