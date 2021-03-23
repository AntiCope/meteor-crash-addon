package WideCat.MeteorCrashAddon.modules;

import WideCat.MeteorCrashAddon.MeteorCrashAddon;
import minegame159.meteorclient.modules.Module;

public class LoginCrash extends Module {

    public LoginCrash() {
        super(MeteorCrashAddon.CATEGORY, "login-crash", "Tries to crash the server on login using null packets");
    }
    
}
