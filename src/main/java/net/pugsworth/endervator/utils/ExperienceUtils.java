package net.pugsworth.endervator.utils;

import net.minecraft.entity.player.PlayerEntity;

public class ExperienceUtils
{
    public static int getExperienceInt(PlayerEntity player)
    {
        return (int) getExperienceDouble(player);
    }

    public static double getExperienceDouble(PlayerEntity player)
    {
        int currentExperience = (int) Math.round(player.experienceProgress * player.getNextLevelExperience());
        int level = player.experienceLevel;
        double levelsqr = Math.pow(level, 2);

        double totalExperience = currentExperience;

        // Values taken from the wiki
        // Don't know why this kind of method doesn't already exist, but whatever.
        if (level <= 16)
        {
            totalExperience += levelsqr + 6 * level;
        }
        else if (level > 16 && level <= 31)
        {
            totalExperience += 2.5 * levelsqr - 40.5 * level + 360;
        }
        else
        {
            totalExperience += 4.5 * levelsqr - 162.5 * level + 2220;
        }

        return totalExperience;
    }
}
