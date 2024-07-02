package xyz.nikitacartes.glowingtorchflower.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchflowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {

    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractBlock$Settings;create()Lnet/minecraft/block/AbstractBlock$Settings;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower")))
    private static AbstractBlock.Settings modifyTorchflower(AbstractBlock.Settings properties) {
        return properties.luminance(blockState -> 12);
    }

    @ModifyExpressionValue(method = "createFlowerPotBlock(Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractBlock$Settings;create()Lnet/minecraft/block/AbstractBlock$Settings;",
                    ordinal = 0))
    private static AbstractBlock.Settings modifyPottedTorchflower(AbstractBlock.Settings properties, Block block) {
        if (block == Blocks.TORCHFLOWER)
            return properties.luminance(blockState -> 14);
        return properties;
    }

    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractBlock$Settings;create()Lnet/minecraft/block/AbstractBlock$Settings;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower_crop")))
    private static AbstractBlock.Settings modifyTorchflowerCrop(AbstractBlock.Settings properties) {

        return properties.luminance(blockState -> switch (blockState.get(TorchflowerBlock.AGE)) {
            case 0 -> 3;
            case 1 -> 7;
            default -> 12;
        });
    }

}