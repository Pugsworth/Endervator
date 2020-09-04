package net.pugsworth.endervator.config;

import java.util.function.Function;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.pugsworth.endervator.EndervatorMod;

@Environment(EnvType.CLIENT)
public class EndervatorConfigMenu implements ModMenuApi
{

    @Override
    public String getModId() {
        return EndervatorMod.MODID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(EndervatorConfig.class, parent).get();
    }
    
}
