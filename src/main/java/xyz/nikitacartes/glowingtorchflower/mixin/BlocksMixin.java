package xyz.nikitacartes.glowingtorchflower.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchflowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import xyz.nikitacartes.glowingtorchflower.config.MainConfigV1;

@Mixin(Blocks.class)
public class BlocksMixin {

    @Unique
    private static MainConfigV1 config;

    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractBlock$Settings;create()Lnet/minecraft/block/AbstractBlock$Settings;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower")))
    private static AbstractBlock.Settings modifyTorchflower(AbstractBlock.Settings properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }
        return properties.luminance(blockState -> config.torchflowerBrightness);
    }

    @ModifyExpressionValue(method = "createFlowerPotBlock(Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractBlock$Settings;create()Lnet/minecraft/block/AbstractBlock$Settings;",
                    ordinal = 0))
    private static AbstractBlock.Settings modifyPottedTorchflower(AbstractBlock.Settings properties, Block block) {
        if (block == Blocks.TORCHFLOWER) {
            if (config == null) {
                config = MainConfigV1.load();
            }
            return properties.luminance(blockState -> config.torchflowerPotBrightness);
        }
        return properties;
    }

    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractBlock$Settings;create()Lnet/minecraft/block/AbstractBlock$Settings;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower_crop")))
    private static AbstractBlock.Settings modifyTorchflowerCrop(AbstractBlock.Settings properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }

        return properties.luminance(blockState -> switch (blockState.get(TorchflowerBlock.AGE)) {
            case 0 -> config.torchflowerStage1Brightness;
            case 1 -> config.torchflowerStage2Brightness;
            default -> config.torchflowerBrightness;
        });
    }

}