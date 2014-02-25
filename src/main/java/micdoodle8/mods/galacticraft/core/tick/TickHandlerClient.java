package micdoodle8.mods.galacticraft.core.tick;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOrbit;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOverworld;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlayCountdown;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlayDockingRocket;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlayLander;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlayOxygenTankIndicator;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlayOxygenWarning;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySpaceship;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.items.ItemSensorGlasses;
import micdoodle8.mods.galacticraft.core.network.PacketRotateRocket;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.ThreadRequirementMissing;
import micdoodle8.mods.galacticraft.core.util.ThreadVersionCheck;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import tconstruct.client.tabs.TabRegistry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

/**
 * GCCoreTickHandlerClient.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TickHandlerClient
{
	public static int airRemaining;
	public static int airRemaining2;
	public static boolean checkedVersion = true;
	private static boolean lastInvKeyPressed;
	private static long tickCount;
	public static boolean addTabsNextTick = false;
	public static Set<Vector3> valuableBlocks = new HashSet<Vector3>();
	private static Set<BlockMetaList> detectableBlocks = new HashSet<BlockMetaList>();
	public static boolean lastSpacebarDown;

	public static Set<PlayerGearData> playerItemData = Sets.newHashSet();

	private static ThreadRequirementMissing missingRequirementThread;

	static
	{
		// for (final String s : GCCoreConfigManager.detectableIDs)
		// {
		// final String[] split = s.split(":");
		// Block block = Block.getBlockById(Integer.parseInt(split[0]));
		// List<Integer> metaList = Lists.newArrayList();
		// metaList.add(Integer.parseInt(split[1]));
		//
		// for (BlockMetaList blockMetaList : detectableBlocks)
		// {
		// if (blockMetaList.getBlock() == block)
		// {
		// metaList.addAll(blockMetaList.getMetaList());
		// break;
		// }
		// }
		//
		// if (!metaList.contains(0))
		// {
		// metaList.add(0);
		// }
		//
		// detectableBlocks.add(new BlockMetaList(block, metaList));
		// }
	}

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event)
	{
		// if (player != null)
		// {
		// ClientProxyCore.playerPosX = player.prevPosX + (player.posX -
		// player.prevPosX) * partialTickTime;
		// ClientProxyCore.playerPosY = player.prevPosY + (player.posY -
		// player.prevPosY) * partialTickTime;
		// ClientProxyCore.playerPosZ = player.prevPosZ + (player.posZ -
		// player.prevPosZ) * partialTickTime;
		// ClientProxyCore.playerRotationYaw = player.prevRotationYaw +
		// (player.rotationYaw - player.prevRotationYaw) * partialTickTime;
		// ClientProxyCore.playerRotationPitch = player.prevRotationPitch +
		// (player.rotationPitch - player.prevRotationPitch) * partialTickTime;
		// }

		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.thePlayer;
		World world = minecraft.theWorld;
		GCEntityClientPlayerMP playerBaseClient = (GCEntityClientPlayerMP) player;

		if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityTier1Rocket)
		{
			float f = (((EntityTier1Rocket) player.ridingEntity).timeSinceLaunch - 250F) / 175F;

			if (f < 0)
			{
				f = 0F;
			}

			if (f > 1)
			{
				f = 1F;
			}

			final ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
			scaledresolution.getScaledWidth();
			scaledresolution.getScaledHeight();
			minecraft.entityRenderer.setupOverlayRendering();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
		{
			OverlaySpaceship.renderSpaceshipOverlay(((EntitySpaceshipBase) player.ridingEntity).getSpaceshipGui());
		}

		if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityLander && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
		{
			OverlayLander.renderLanderOverlay();
		}

		if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityAutoRocket && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
		{
			OverlayDockingRocket.renderDockingOverlay();
		}

		if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI && ((EntitySpaceshipBase) minecraft.thePlayer.ridingEntity).launchPhase != EnumLaunchPhase.LAUNCHED.ordinal())
		{
			OverlayCountdown.renderCountdownOverlay();
		}

		if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen))
		{
			int var6 = (TickHandlerClient.airRemaining - 90) * -1;

			if (TickHandlerClient.airRemaining <= 0)
			{
				var6 = 90;
			}

			int var7 = (TickHandlerClient.airRemaining2 - 90) * -1;

			if (TickHandlerClient.airRemaining2 <= 0)
			{
				var7 = 90;
			}

			OverlayOxygenTankIndicator.renderOxygenTankIndicator(var6, var7, !ConfigManagerCore.oxygenIndicatorLeft, !ConfigManagerCore.oxygenIndicatorBottom);
		}

		if (playerBaseClient != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && !playerBaseClient.oxygenSetupValid && minecraft.currentScreen == null && !playerBaseClient.capabilities.isCreativeMode)
		{
			OverlayOxygenWarning.renderOxygenWarningOverlay();
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.thePlayer;
		World world = minecraft.theWorld;

		if (event.phase == TickEvent.Phase.START)
		{
			if (TickHandlerClient.tickCount >= Long.MAX_VALUE)
			{
				TickHandlerClient.tickCount = 0;
			}

			TickHandlerClient.tickCount++;

			if (TickHandlerClient.tickCount % 20 == 0)
			{
				if (player != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() instanceof ItemSensorGlasses)
				{
					TickHandlerClient.valuableBlocks.clear();

					for (int i = -4; i < 5; i++)
					{
						for (int j = -4; j < 5; j++)
						{
							for (int k = -4; k < 5; k++)
							{
								int x = MathHelper.floor_double(player.posX + i);
								int y = MathHelper.floor_double(player.posY + j);
								int z = MathHelper.floor_double(player.posZ + k);

								Block block = player.worldObj.getBlock(x, y, z);

								if (block != Blocks.air)
								{
									int metadata = world.getBlockMetadata(x, y, z);
									boolean isDetectable = false;

									for (BlockMetaList blockMetaList : TickHandlerClient.detectableBlocks)
									{
										if (blockMetaList.getBlock() == block && blockMetaList.getMetaList().contains(metadata))
										{
											isDetectable = true;
											break;
										}
									}

									if (isDetectable)
									{
										if (!this.alreadyContainsBlock(x, y, z))
										{
											TickHandlerClient.valuableBlocks.add(new Vector3(x, y, z));
										}
									}
									else if (block instanceof IDetectableResource && ((IDetectableResource) block).isValueable(metadata))
									{
										if (!this.alreadyContainsBlock(x, y, z))
										{
											TickHandlerClient.valuableBlocks.add(new Vector3(x, y, z));
										}

										List<Integer> metaList = Lists.newArrayList();
										metaList.add(metadata);

										for (BlockMetaList blockMetaList : TickHandlerClient.detectableBlocks)
										{
											if (blockMetaList.getBlock() == block)
											{
												metaList.addAll(blockMetaList.getMetaList());
												break;
											}
										}

										TickHandlerClient.detectableBlocks.add(new BlockMetaList(block, metaList));
									}
								}
							}
						}
					}
				}
			}

			if (minecraft.currentScreen != null && minecraft.currentScreen instanceof GuiMainMenu)
			{
				GalacticraftCore.playersServer.clear();
				GalacticraftCore.playersClient.clear();
				TickHandlerClient.playerItemData.clear();

				if (TickHandlerClient.missingRequirementThread == null)
				{
					TickHandlerClient.missingRequirementThread = new ThreadRequirementMissing(FMLCommonHandler.instance().getEffectiveSide());
					TickHandlerClient.missingRequirementThread.start();
				}
			}

			if (world != null && TickHandlerClient.checkedVersion)
			{
				ThreadVersionCheck.instance.start();
				TickHandlerClient.checkedVersion = false;
			}

			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase)
			{
				GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(player.ridingEntity));
				// final Object[] toSend = { player.ridingEntity.rotationPitch
				// };
				// PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
				// EnumPacketServer.UPDATE_SHIP_PITCH, toSend));
				// final Object[] toSend2 = { player.ridingEntity.rotationYaw };
				// PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
				// EnumPacketServer.UPDATE_SHIP_YAW, toSend2));
			}

			if (world != null && world.provider instanceof WorldProviderSurface)
			{
				if (world.provider.getSkyRenderer() == null && player.ridingEntity != null && player.ridingEntity.posY >= 200)
				{
					world.provider.setSkyRenderer(new SkyProviderOverworld());
				}
				else if (world.provider.getSkyRenderer() != null && world.provider.getSkyRenderer() instanceof SkyProviderOverworld && (player.ridingEntity == null || player.ridingEntity.posY < 200))
				{
					world.provider.setSkyRenderer(null);
				}
			}

			if (world != null && world.provider instanceof WorldProviderSpaceStation)
			{
				if (world.provider.getSkyRenderer() == null)
				{
					world.provider.setSkyRenderer(new SkyProviderOrbit(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/planets/overworld.png"), true, true));
				}

				if (world.provider.getCloudRenderer() == null)
				{
					world.provider.setCloudRenderer(new CloudRenderer());
				}
			}

			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase)
			{
				final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;
				boolean hasChanged = false;

				if (minecraft.gameSettings.keyBindLeft.isPressed())
				{
					ship.turnYaw(-1.0F);
					hasChanged = true;
					// final Object[] toSend = { ship.rotationYaw };
					// PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
					// EnumPacketServer.UPDATE_SHIP_YAW, toSend));
				}

				if (minecraft.gameSettings.keyBindRight.isPressed())
				{
					ship.turnYaw(1.0F);
					hasChanged = true;
					// final Object[] toSend = { ship.rotationYaw };
					// PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
					// EnumPacketServer.UPDATE_SHIP_YAW, toSend));
				}

				if (minecraft.gameSettings.keyBindForward.isPressed())
				{
					if (ship.getLaunched())
					{
						ship.turnPitch(-0.7F);
						hasChanged = true;
						// final Object[] toSend = { ship.rotationPitch };
						// PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
						// EnumPacketServer.UPDATE_SHIP_PITCH, toSend));
					}
				}

				if (minecraft.gameSettings.keyBindBack.isPressed())
				{
					if (ship.getLaunched())
					{
						ship.turnPitch(0.7F);
						hasChanged = true;
						// final Object[] toSend = { ship.rotationPitch };
						// PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
						// EnumPacketServer.UPDATE_SHIP_PITCH, toSend));
					}
				}

				if (hasChanged)
				{
					GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(ship));
				}
			}

			if (world != null)
			{
				for (int i = 0; i < world.loadedEntityList.size(); i++)
				{
					final Entity e = (Entity) world.loadedEntityList.get(i);

					if (e != null)
					{
						if (e instanceof EntityTier1Rocket)
						{
							final EntityTier1Rocket eship = (EntityTier1Rocket) e;

							if (eship.rocketSoundUpdater == null)
							{
								// TODO Fix Rocket sound updater
								// eship.rocketSoundUpdater = new
								// GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager,
								// eship,
								// FMLClientHandler.instance().getClient().thePlayer);
							}
						}
					}
				}
			}

			if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiChoosePlanet)
			{
				player.motionY = 0;
			}

			if (world != null && world.provider instanceof IGalacticraftWorldProvider)
			{
				world.setRainStrength(0.0F);
			}
		}

		if (event.phase == TickEvent.Phase.END)
		{
			boolean invKeyPressed = Keyboard.isKeyDown(minecraft.gameSettings.keyBindInventory.getKeyCode());

			if (!TickHandlerClient.lastInvKeyPressed && invKeyPressed && minecraft.currentScreen != null && minecraft.currentScreen.getClass() == GuiInventory.class)
			{
				TickHandlerClient.addTabsToInventory((GuiContainer) minecraft.currentScreen);
			}

			TickHandlerClient.lastInvKeyPressed = invKeyPressed;

			if (!Keyboard.isKeyDown(minecraft.gameSettings.keyBindJump.getKeyCode()))
			{
				TickHandlerClient.lastSpacebarDown = false;
			}

			if (player != null && player.ridingEntity != null && Keyboard.isKeyDown(minecraft.gameSettings.keyBindJump.getKeyCode()) && !TickHandlerClient.lastSpacebarDown)
			{
				GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_IGNITE_ROCKET, new Object[] { }));
				TickHandlerClient.lastSpacebarDown = true;
			}
		}
	}

	@SubscribeEvent
	public void entityJoined(EntityJoinWorldEvent event)
	{
	}

	private boolean alreadyContainsBlock(int x1, int y1, int z1)
	{
		return TickHandlerClient.valuableBlocks.contains(new Vector3(x1, y1, z1));
	}

	public static void zoom(float value)
	{
		try
		{
			Class<?> clazz = FMLClientHandler.instance().getClient().entityRenderer.getClass();
			java.lang.reflect.Field f = clazz.getDeclaredField("thirdPersonDistance");
			f.setAccessible(true);
			f.set(FMLClientHandler.instance().getClient().entityRenderer, value); // TODO
																					// Fix
																					// zoom
																					// for
																					// non-dev
																					// env
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void addTabsToInventory(GuiContainer gui)
	{
		boolean tConstructLoaded = Loader.isModLoaded("TConstruct");

		if (!tConstructLoaded)
		{
			if (!TickHandlerClient.addTabsNextTick)
			{
				TickHandlerClient.addTabsNextTick = true;
				return;
			}

			TabRegistry.addTabsToInventory(gui);
		}
	}
}
