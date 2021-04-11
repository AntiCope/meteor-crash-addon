package WideCat.MeteorCrashAddon.mixin;

import com.mojang.authlib.GameProfile;
import minegame159.meteorclient.systems.modules.Modules;
import WideCat.MeteorCrashAddon.modules.LoginCrash;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoginHelloC2SPacket.class)
public abstract class GameProfileMixin {

    @Shadow private GameProfile profile;

    @Shadow public abstract GameProfile getProfile();

    @Inject(method = "write", cancellable = true, at = @At("HEAD"))
    public void gid(PacketByteBuf buf, CallbackInfo ci) {
        if (Modules.get().isActive(LoginCrash.class)) {
            buf.writeString(null);
            ci.cancel();
        }
    }
}
