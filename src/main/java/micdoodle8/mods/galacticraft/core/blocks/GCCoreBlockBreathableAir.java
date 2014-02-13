package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * GCCoreBlockBreathableAir.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockBreathableAir extends BlockAir
{
	public GCCoreBlockBreathableAir(String assetName)
	{
		super();
		this.setResistance(1000.0F);
		this.setHardness(0.0F);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
    public boolean isAir(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
	{
		return null;
	}

	@Override
	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
	{
		return true;
	}

	@Override
	public boolean canCollideCheck(int var1, boolean var2)
	{
		return false;
	}

	@Override
	public int getRenderBlockPass()
	{
		return 1;
	}

	@Override
	public int getMobilityFlag()
	{
		return 1;
	}

	@Override
	public Item getItemDropped(int var1, Random var2, int var3)
	{
		return Item.getItemFromBlock(Blocks.air);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		if (par1IBlockAccess.getBlock(par2, par3, par4) == this)
		{
			return false;
		}
		else
		{
			Block block = par1IBlockAccess.getBlock(par2, par3, par4);
			boolean var6 = block.isOpaqueCube();

			boolean var7 = block == Blocks.air;

			if ((var6 || var7) && par5 == 3 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 4 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 5 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 2 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 0 && !var6)
			{
				return true;
			}
			else if ((var6 || var7) && par5 == 1 && !var6)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block blockBroken)
	{
		if (blockBroken != Blocks.air && blockBroken != GCCoreBlocks.breatheableAir)
		{
			OxygenPressureProtocol.onEdgeBlockUpdated(world, new Vector3(x, y, z));
		}
	}
}
