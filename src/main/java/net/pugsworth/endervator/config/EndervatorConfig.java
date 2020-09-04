package net.pugsworth.endervator.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.pugsworth.endervator.EndervatorMod;

@Config(name=EndervatorMod.MODID)
public class EndervatorConfig implements ConfigData
{
    @Comment("Enable the mod's functionality.")
    public boolean enable = true;
    @Comment("Maximum distance to look for the next endervator.")
    public int maximumDistance = 16;
    @Comment("How much light to give off.")
    public int lightLevel = 6;
    @Comment("Namespace ID of the sound file to play.")
    public String soundName = "minecraft:entity.shulker.teleport";

    @ConfigEntry.Gui.CollapsibleObject
    public Client client = new Client();

    @ConfigEntry.Gui.CollapsibleObject
    public Damage damage = new Damage();

    @ConfigEntry.Gui.CollapsibleObject
    public Fuel fuel = new Fuel();

    public static class Client
    {
        @Comment("Enable all particles.")
        public boolean useParticles = true;
        @Comment("Show the stream of partcles in the direction of teleportion.")
        public boolean useParticleStream = true;
    }

    public static class Damage
    {
        @Comment("Does damage like an enderpearl.")
        public boolean doesDamage = true;
        @Comment("How much damager per block teleported.")
        public double damagePerBlock = 0.1f;
    }

    public static class Fuel
    {
        @Comment("Use Experience for fuel")
        public boolean useXP = true;
        @Comment("How much experience per block to use. If insufficient Experience, teleport fails.")
        public double xpAmount = 0.5f;
        @Comment("Consume this much experience no matter the distance")
        public boolean xpAmountAbsolute = false;
        @Comment("An internal inventory to use an item for fuel.")
        public boolean useItem = false;
        @Comment("What Item to use for fuel?")
        public Item itemID = Items.ENDER_PEARL;
        @Comment("Use damage for fuel.")
        public boolean useDamage = false;
        @Comment("How much damage to incur per block.")
        public float damagePerBlock = 0.1f;
    }
}
