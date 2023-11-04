package me.drex.badpacketfix.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Based on: Paper (Add-slot-sanity-checks-in-container-clicks.patch)
 * <p>
 * Patch Author: Nassim Jahnke <nassim@njahnke.dev>
 * <br>
 * License: GPL-3.0
 */
@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @Inject(
        method = "doClick",
        slice = @Slice(
            id = "swap",
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/inventory/ClickType;SWAP:Lnet/minecraft/world/inventory/ClickType;"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;"
            )
        ),
        at = @At(
            value = "INVOKE",
            slice = "swap",
            target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void addSanityChecksSwap(int slotIndex, int button, ClickType clickType, Player player, CallbackInfo ci) {
        if (slotIndex < 0 || button < 0) {
            ci.cancel();
        }
    }


    @Inject(
        method = "doClick",
        slice = @Slice(
            id = "quickcraft",
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;quickcraftStatus:I"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;canItemQuickReplace(Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/world/item/ItemStack;Z)Z"
            )
        ),
        at = @At(
            value = "INVOKE",
            slice = "quickcraft",
            target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;"
        ),
        cancellable = true
    )
    private void addSanityCheckQuickCraft(int slotIndex, int button, ClickType clickType, Player player, CallbackInfo ci) {
        if (slotIndex < 0) {
            ci.cancel();
        }
    }


}
