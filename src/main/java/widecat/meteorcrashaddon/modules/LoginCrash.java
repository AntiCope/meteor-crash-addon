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
https://github.com/0x151/Cornos/blob/master/src/main/java/me/zeroX150/cornos/features/module/impl/exploit/crash/LoginCrash.java
 */

import meteordevelopment.meteorclient.systems.modules.Module;
import widecat.meteorcrashaddon.CrashAddon;

public class LoginCrash extends Module {
    public LoginCrash() {
        super(CrashAddon.CATEGORY, "login-crash", "Tries to crash the server on login using null packets. (By 0x150)");
    }
}
