package dev.hlieb01.createsableinteractive;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.mojang.logging.LogUtils;
import dev.hlieb01.createsableinteractive.client.ClientControls;
import dev.hlieb01.createsableinteractive.config.TrainSpeedConfig;
import dev.hlieb01.createsableinteractive.network.TrainControlNetworking;
import dev.hlieb01.createsableinteractive.registry.ModBlocks;
import dev.hlieb01.createsableinteractive.registry.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(CreateSableInteractive.MOD_ID)
public final class CreateSableInteractive {
    public static final String MOD_ID = "create_sable_interactive";
    public static final Logger LOGGER = LogUtils.getLogger();

    /** The only CreateRegistrate instance in this addon. */
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public CreateSableInteractive(IEventBus modEventBus, ModContainer modContainer) {
        TrainSpeedConfig.load();
        ModCreativeTabs.register(modEventBus);
        REGISTRATE.setCreativeTab(ModCreativeTabs.MAIN_TAB);
        REGISTRATE.registerEventListeners(modEventBus);
        ModBlocks.register();
        TrainControlNetworking.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ClientControls::registerKeyMappings);
            NeoForge.EVENT_BUS.addListener(ClientControls::onClientTick);
        }
    }
}
