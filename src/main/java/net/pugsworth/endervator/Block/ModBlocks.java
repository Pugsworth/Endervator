package net.pugsworth.endervator.Block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pugsworth.endervator.EndervatorMod;

public class ModBlocks
{
    public static final Block ENDERVATOR_BLOCK = new EndervatorBlock(FabricBlockSettings
		.of(Material.METAL)
		.breakByTool(FabricToolTags.PICKAXES)
		.lightLevel(EndervatorMod.CONFIG.lightLevel)
		.strength(5, 6)
		.ticksRandomly()
		.build()
	);
    
    public static void RegisterBlocks()
    {
        registerBlock("endervator_block", ENDERVATOR_BLOCK, ItemGroup.MISC);
    }

    public static void registerBlock(String id, Block block, ItemGroup group)
	{
		Registry.register(Registry.BLOCK, new Identifier(EndervatorMod.MODID, id), block);
		Registry.register(Registry.ITEM, new Identifier(EndervatorMod.MODID, id), new BlockItem(block, new Item.Settings().group(group)));
	}
}
