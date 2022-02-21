/*
 *  Copyright (c) 2021 Wide_Cat and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package widecat.meteorcrashaddon;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import widecat.meteorcrashaddon.modules.*;

import java.lang.invoke.MethodHandles;

public class CrashAddon extends MeteorAddon {
    public static final Logger LOG = LogManager.getLogger();
    public static final Category CATEGORY = new Category("Crash", Items.TNT.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Crash Addon.");

        MeteorClient.EVENT_BUS.registerLambdaFactory("widecat.meteorcrashaddon", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        Modules.get().add(new AACCrash());
        Modules.get().add(new BoatCrash());
        Modules.get().add(new BookCrash());
        Modules.get().add(new ContainerCrash());
        Modules.get().add(new EntityCrash());
        Modules.get().add(new InvalidPositionCrash());
        Modules.get().add(new LoginCrash());
        Modules.get().add(new MessageLagger());
        Modules.get().add(new MovementCrash());
        Modules.get().add(new PacketSpammer());
        Modules.get().add(new SignCrash());
        Modules.get().add(new TryUseCrash());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }
}
