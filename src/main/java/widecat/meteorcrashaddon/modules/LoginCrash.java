package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import widecat.meteorcrashaddon.CrashAddon;

public class LoginCrash extends Module {
    public LoginCrash() {
        super(CrashAddon.CATEGORY, "login-crash", "Tries to crash the server on login using null packets.");
    }
}
