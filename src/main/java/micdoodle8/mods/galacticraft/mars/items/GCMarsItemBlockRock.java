package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockSlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsItemBlockRock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsItemBlockRock extends ItemBlock
{
	public GCMarsItemBlockRock(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		String name = GCMarsBlockSlimelingEgg.names[itemstack.getItemDamage() % 3];

		return this.field_150939_a.getUnlocalizedName() + "." + name;
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftMars.galacticraftMarsTab;
	}

	@Override
	public String getUnlocalizedName()
	{
		return this.field_150939_a.getUnlocalizedName() + ".0";
	}
}
