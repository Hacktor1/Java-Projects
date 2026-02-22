package com.example.examplemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("examplemod")
public class ExampleMod {
    public ExampleMod() {
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}