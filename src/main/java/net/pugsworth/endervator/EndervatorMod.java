package net.pugsworth.endervator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pugsworth.endervator.Block.EndervatorBlock;

public class EndervatorMod implements ModInitializer
{

	public static final Block ENDERVATOR_BLOCK = new EndervatorBlock(FabricBlockSettings
		.of(Material.METAL)
		.breakByTool(FabricToolTags.PICKAXES)
		.lightLevel(6)
		.strength(5, 6)
		.ticksRandomly()
		.build()
	);

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.BLOCK, new Identifier("endervator", "endervator_block"), ENDERVATOR_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("endervator", "endervator_block"), new BlockItem(ENDERVATOR_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
	}
}


/**
 * TODO:
 * 
 * particles
 * check space above endervator
 */