package widecat.meteorcrashaddon;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import widecat.meteorcrashaddon.commands.CrashItemCommand;
import widecat.meteorcrashaddon.modules.*;

public class CrashAddon extends MeteorAddon {
    public static final Logger LOG = LoggerFactory.getLogger("CrashAddon");
    public static final Category CATEGORY = new Category("Crash", Items.TNT.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Crash Addon.");

        Modules.get().add(new AACCrash());
        Modules.get().add(new BookCrash());
        Modules.get().add(new ContainerCrash());
        Modules.get().add(new CraftingCrash());
        Modules.get().add(new CreativeCrash());
        Modules.get().add(new EntityCrash());
        Modules.get().add(new ErrorCrash());
        Modules.get().add(new InteractCrash());
        Modules.get().add(new LecternCrash());
        Modules.get().add(new MessageLagger());
        Modules.get().add(new MovementCrash());
        Modules.get().add(new PacketSpammer());
        Modules.get().add(new SequenceCrash());

        Commands.add(new CrashItemCommand());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getWebsite() {
        return "https://github.com/AntiCope/meteor-crash-addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("AntiCope", "meteor-crash-addon");
    }

    @Override
    public String getCommit() {
        String commit = FabricLoader
            .getInstance()
            .getModContainer("meteor-crash-addon")
            .get().getMetadata()
            .getCustomValue("github:sha")
            .getAsString();
        return commit.isEmpty() ? null : commit.trim();

    }

    public String getPackage() {
        return "widecat.meteorcrashaddon";
    }
}
