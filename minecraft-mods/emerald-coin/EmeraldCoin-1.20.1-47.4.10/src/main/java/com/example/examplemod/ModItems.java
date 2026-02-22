package com.example.examplemod;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "examplemod");

    public static final RegistryObject<Item> EMERALD_COIN = ITEMS.register("emerald_coin",
            () -> new Item(new Item.Properties()));
}