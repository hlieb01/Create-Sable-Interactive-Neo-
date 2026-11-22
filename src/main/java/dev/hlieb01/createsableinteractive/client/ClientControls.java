package dev.hlieb01.createsableinteractive.client;

import dev.hlieb01.createsableinteractive.network.TrainControlPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public final class ClientControls {
    private static final KeyMapping ACCELERATE = new KeyMapping(
        "key.create_sable_interactive.accelerate", GLFW.GLFW_KEY_UP, "key.categories.create_sable_interactive");
    private static final KeyMapping BRAKE = new KeyMapping(
        "key.create_sable_interactive.brake", GLFW.GLFW_KEY_DOWN, "key.categories.create_sable_interactive");
    private static int tickCounter;

    private ClientControls() {}

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ACCELERATE);
        event.register(BRAKE);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getConnection() == null) return;
        int input = 0;
        if (ACCELERATE.isDown()) input |= TrainControlPayload.ACCELERATE;
        if (BRAKE.isDown()) input |= TrainControlPayload.BRAKE;
        if (input == 0 || ++tickCounter % 2 != 0) return;
        PacketDistributor.sendToServer(new TrainControlPayload(input));
    }
}
