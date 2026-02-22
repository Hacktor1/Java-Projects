package com.example.examplemod;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.CreativeModeTabs;

@Mod.EventBusSubscriber(modid = "examplemod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabEvents {

    @SubscribeEvent
    public static void addItemsToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.EMERALD_COIN);
        }
    }
}