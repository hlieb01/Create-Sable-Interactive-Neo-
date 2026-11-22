package dev.hlieb01.createsableinteractive.registry;

import dev.hlieb01.createsableinteractive.CreateSableInteractive;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateSableInteractive.MOD_ID);

    public static final ResourceKey<CreativeModeTab> MAIN_TAB_KEY = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB,
        ResourceLocation.fromNamespaceAndPath(CreateSableInteractive.MOD_ID, "main")
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = REGISTER.register("main",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.create_sable_interactive.main"))
            .icon(() -> new ItemStack(ModBlocks.WARNING_BARRIER_10.get()))
            .displayItems((parameters, output) -> {})
            .build());

    private ModCreativeTabs() {}

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
