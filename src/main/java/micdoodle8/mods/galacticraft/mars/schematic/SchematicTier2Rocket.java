package micdoodle8.mods.galacticraft.mars.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.mars.client.gui.GuiSchematicTier2Rocket;
import micdoodle8.mods.galacticraft.mars.inventory.ContainerSchematicTier2Rocket;
import micdoodle8.mods.galacticraft.mars.util.ConfigManagerMars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsSchematicRocketT2.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class SchematicTier2Rocket implements ISchematicPage
{
	@Override
	public int getPageID()
	{
		return ConfigManagerMars.idSchematicRocketT2;
	}

	@Override
	public int getGuiID()
	{
		return ConfigManagerMars.idGuiRocketCraftingBenchT2;
	}

	@Override
	public ItemStack getRequiredItem()
	{
		return new ItemStack(GCItems.schematic, 1, 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
	{
		return new GuiSchematicTier2Rocket(player.inventory, x, y, z);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, int x, int y, int z)
	{
		return new ContainerSchematicTier2Rocket(player.inventory, x, y, z);
	}

	@Override
	public int compareTo(ISchematicPage o)
	{
		if (this.getPageID() > o.getPageID())
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
}
