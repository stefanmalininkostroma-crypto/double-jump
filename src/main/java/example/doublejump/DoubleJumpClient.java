
package example.doublejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class DoubleJumpClient implements ClientModInitializer {
    private boolean jumpWasDown = false;
    private boolean secondJumpUsed = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;

        if (player == null || client.world == null) {
            jumpWasDown = false;
            secondJumpUsed = false;
            return;
        }

        if (player.isOnGround()) {
            secondJumpUsed = false;
        }

        boolean jumpDown = client.options.jumpKey.isPressed();
        boolean justPressed = jumpDown && !jumpWasDown;

        if (justPressed && !player.isOnGround()
                && !secondJumpUsed && !player.isTouchingWater()
                && !player.isInLava() && !player.hasVehicle()) {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, 0.42, velocity.z);
            secondJumpUsed = true;
        }

        jumpWasDown = jumpDown;
    }
}
