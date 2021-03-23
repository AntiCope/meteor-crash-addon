package WideCat.MeteorCrashAddon;

import WideCat.MeteorCrashAddon.modules.*;
import minegame159.meteorclient.MeteorAddon;
import minegame159.meteorclient.modules.Modules;
import minegame159.meteorclient.modules.Category;
import net.minecraft.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MeteorCrashAddon extends MeteorAddon {
    public static final Logger LOG = LogManager.getLogger();
    public static final Category CATEGORY = new Category("Crash", Items.TNT.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Initializing MeteorCrashAddon");

        Modules.get().add(new BoatCrash1());
        Modules.get().add(new BoatCrash2());
    }

    @Override
    public void onRegisterCategories(){
        Modules.registerCategory(CATEGORY);
    }
}