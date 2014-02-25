package micdoodle8.mods.galacticraft.mars.util;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

/**
 * GCMarsConfigManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ConfigManagerMars
{
	public static boolean loaded;

	static Configuration configuration;

	public ConfigManagerMars(File file)
	{
		if (!ConfigManagerMars.loaded)
		{
			ConfigManagerMars.configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}

	// DIMENSIONS
	public static int dimensionIDMars;

	// BLOCKS
	public static int idBlockMars;
	public static int idBlockBacterialSludge;
	public static int idBlockVine;
	public static int idBlockRock;
	public static int idBlockTreasureChestT2;
	public static int idBlockMachine;
	public static int idBlockCreeperEgg;
	public static int idBlockTintedGlassPane;

	// ITEMS
	public static int idItemMarsBasic;
	public static int idItemSpaceshipTier2;
	public static int idItemKeyT2;
	public static int idItemSchematicMars;

	// ARMOR
	public static int idArmorDeshHelmet;
	public static int idArmorDeshChestplate;
	public static int idArmorDeshLeggings;
	public static int idArmorDeshBoots;

	// TOOLS
	public static int idToolDeshSword;
	public static int idToolDeshPickaxe;
	public static int idToolDeshAxe;
	public static int idToolDeshSpade;
	public static int idToolDeshHoe;

	// ENTITIES
	public static int idEntityCreeperBoss;
	public static int idEntityProjectileTNT;
	public static int idEntitySpaceshipTier2;
	public static int idEntitySludgeling;
	public static int idEntitySlimeling;
	public static int idEntityTerraformBubble;
	public static int idEntityLandingBalloons;
	public static int idEntityCargoRocket;

	// GUI
	public static int idGuiRocketCraftingBenchT2;
	public static int idGuiMachine;
	public static int idGuiCargoRocketCraftingBench;

	// SCHEMATIC
	public static int idSchematicRocketT2;
	public static int idSchematicCargoRocket;

	// GENERAL
	public static boolean generateOtherMods;
	public static boolean launchControllerChunkLoad;

	private void setDefaultValues()
	{
		try
		{
			ConfigManagerMars.configuration.load();

			ConfigManagerMars.dimensionIDMars = ConfigManagerMars.configuration.get("Dimensions", "Mars Dimension ID", -29).getInt(-29);

			ConfigManagerMars.idEntityCreeperBoss = ConfigManagerMars.configuration.get("Entities", "idEntityCreeperBoss", 171).getInt(171);
			ConfigManagerMars.idEntityProjectileTNT = ConfigManagerMars.configuration.get("Entities", "idEntityProjectileTNT", 172).getInt(172);
			ConfigManagerMars.idEntitySpaceshipTier2 = ConfigManagerMars.configuration.get("Entities", "idEntitySpaceshipTier2", 173).getInt(173);
			ConfigManagerMars.idEntitySludgeling = ConfigManagerMars.configuration.get("Entities", "idEntitySludgeling", 174).getInt(174);
			ConfigManagerMars.idEntitySlimeling = ConfigManagerMars.configuration.get("Entities", "idEntitySlimeling", 175).getInt(175);
			ConfigManagerMars.idEntityTerraformBubble = ConfigManagerMars.configuration.get("Entities", "idEntityTerraformBubble", 176).getInt(176);
			ConfigManagerMars.idEntityLandingBalloons = ConfigManagerMars.configuration.get("Entities", "idEntityLandingBalloons", 177).getInt(177);
			ConfigManagerMars.idEntityCargoRocket = ConfigManagerMars.configuration.get("Entities", "idEntityCargoRocket", 178).getInt(178);

			ConfigManagerMars.idGuiRocketCraftingBenchT2 = ConfigManagerMars.configuration.get("GUI", "idGuiRocketCraftingBenchT2", 143).getInt(143);
			ConfigManagerMars.idGuiMachine = ConfigManagerMars.configuration.get("GUI", "idGuiMachine", 146).getInt(146);
			ConfigManagerMars.idGuiCargoRocketCraftingBench = ConfigManagerMars.configuration.get("GUI", "idGuiCargoRocketCraftingBench", 147).getInt(147);

			ConfigManagerMars.idSchematicRocketT2 = ConfigManagerMars.configuration.get("Schematic", "idSchematicRocketT2", 2).getInt(2);
			ConfigManagerMars.idSchematicCargoRocket = ConfigManagerMars.configuration.get("Schematic", "idSchematicCargoRocket", 3).getInt(3);

			ConfigManagerMars.generateOtherMods = ConfigManagerMars.configuration.get(Configuration.CATEGORY_GENERAL, "Generate other mod's features on Mars", false).getBoolean(false);
			ConfigManagerMars.launchControllerChunkLoad = ConfigManagerMars.configuration.get(Configuration.CATEGORY_GENERAL, "Whether launch controller keeps chunks loaded. This will cause issues if disabled.", true).getBoolean(true);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.FATAL, e, "Galacticraft Mars has a problem loading it's configuration");
		}
		finally
		{
			ConfigManagerMars.configuration.save();
			ConfigManagerMars.loaded = true;
		}
	}
}
