package com.example.separatebobbers.mixin;

import com.example.separatebobbers.BobberTracker;
import com.example.separatebobbers.RodIds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.Level;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {

    @Inject(method = "use", at = @At("HEAD"))
    private void separatebobbers$before(Level level, Player player, InteractionHand hand,
                                        CallbackInfoReturnable<InteractionResult> cir) {
        if (!(level instanceof ServerLevel)) return;
        String id = RodIds.getOrCreate(player.getItemInHand(hand));
        if (id != null) BobberTracker.begin(player, id);
    }

    @Inject(method = "use", at = @At("RETURN"))
    private void separatebobbers$after(Level level, Player player, InteractionHand hand,
                                       CallbackInfoReturnable<InteractionResult> cir) {
        if (!(level instanceof ServerLevel)) return;
        BobberTracker.end(player);
    }
}
