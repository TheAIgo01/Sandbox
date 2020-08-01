package org.sandboxpowered.sandbox.fabric;

import net.minecraft.util.registry.Registry;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.security.AddonSecurityPolicy;

import java.security.Policy;

public class SandboxHooks {
    public static void shutdown() {
//        if (Sandbox.SANDBOX.getSide() == Side.CLIENT) { TODO
//            SandboxClient.INSTANCE.shutdown();
//            if (SandboxServer.INSTANCE != null && SandboxServer.INSTANCE.isIntegrated())
//                SandboxServer.INSTANCE.shutdown();
//        } else {
//            SandboxServer.INSTANCE.shutdown();
//        }
        Registry.REGISTRIES.stream().map(registry -> (SandboxInternal.Registry) registry).forEach(SandboxInternal.Registry::reset);
    }

    public static void setupGlobal() {
        Policy.setPolicy(new AddonSecurityPolicy());
    }

    public static void shutdownGlobal() {
    }
}