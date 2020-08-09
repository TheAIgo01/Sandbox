package org.sandboxpowered.sandbox.fabric.mixin.event.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.BiFunction;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow
    public abstract boolean isDamageable();

    @ModifyVariable(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "LOAD", ordinal = 0))
    public int place(int localDamage, int damage, Random rdm, @Nullable ServerPlayerEntity playerEntity) {
        if (isDamageable())
            return localDamage;
        if (ItemEvents.DAMAGE.hasSubscribers()) {
            PlayerEntity player = WrappingUtil.convert(playerEntity);
            org.sandboxpowered.api.item.ItemStack stack = WrappingUtil.convert((ItemStack) (Object) this);
            return ItemEvents.DAMAGE.post((event, value) -> event.onEvent(player, stack, value), damage);
        }
        return localDamage;
    }

    @Inject(at = @At("RETURN"), method = "getMiningSpeedMultiplier", cancellable = true)
    public void getMiningSpeedMultiplier(BlockState vs, CallbackInfoReturnable<Float> info) {
        if (ItemEvents.MINING_SPEED.hasSubscribers()) {
            org.sandboxpowered.api.item.ItemStack stack = WrappingUtil.convert((ItemStack) (Object) this);
            org.sandboxpowered.api.state.BlockState state = WrappingUtil.convert(vs);
            ItemEvents.MINING_SPEED.post((miningSpeedEvent, miningSpeed) -> miningSpeedEvent.onEvent(stack, state, miningSpeed), info.getReturnValue());
        }
    }
}