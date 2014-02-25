package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import micdoodle8.mods.galacticraft.mars.blocks.BlockMachineMars;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsItemBlockMachine.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemBlockMachine extends ItemBlock implements IHoldableItem
{
	public ItemBlockMachine(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int metadata = 0;

		if (itemstack.getItemDamage() >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			metadata = 2;
		}
		else if (itemstack.getItemDamage() >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			metadata = 1;
		}

		return this.field_150939_a.getUnlocalizedName() + "." + metadata;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName()
	{
		return this.field_150939_a.getUnlocalizedName() + ".0";
	}

	@Override
	public boolean shouldHoldLeftHandUp(EntityPlayer player)
	{
		ItemStack currentStack = player.getCurrentEquippedItem();

		if (currentStack != null && currentStack.getItemDamage() >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean shouldHoldRightHandUp(EntityPlayer player)
	{
		ItemStack currentStack = player.getCurrentEquippedItem();

		if (currentStack != null && currentStack.getItemDamage() >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean shouldCrouch(EntityPlayer player)
	{
		return false;
	}
}
