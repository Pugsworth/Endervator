package net.pugsworth.endervator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.pugsworth.endervator.Block.ModBlocks;
import net.pugsworth.endervator.config.EndervatorConfig;

public class EndervatorMod implements ModInitializer
{
	public static final String MODID = "endervator";
	public static final Logger logger = LogManager.getLogger(MODID);
	public static final EndervatorConfig CONFIG = AutoConfig.register(EndervatorConfig.class, JanksonConfigSerializer::new).getConfig();

	public static final Identifier TELEPORT_PACKET = new Identifier(MODID, "teleport_packet");

	@Override
	public void onInitialize()
	{
		if (!CONFIG.enable)
			return;

		ModBlocks.RegisterBlocks();
	}
}


/**
 * TODO:
 * 
 * particles
 * check space above endervator
 * config
 * fuel
 * particles and "power" only if there is at least 1 valid endervator you can teleport to?
 */