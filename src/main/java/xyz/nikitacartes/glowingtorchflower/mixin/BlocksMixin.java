package xyz.nikitacartes.glowingtorchflower.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;
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
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower")))
    private static BlockBehaviour.Properties modifyTorchflower(BlockBehaviour.Properties properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }
        return properties.lightLevel(blockState -> config.torchflowerBrightness);
    }

    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Blocks;flowerPotProperties()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potted_torchflower")))
    private static BlockBehaviour.Properties modifyPottedTorchflower(BlockBehaviour.Properties properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }
        return properties.lightLevel(blockState -> config.torchflowerPotBrightness);
    }

    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower_crop")))
    private static BlockBehaviour.Properties modifyTorchflowerCrop(BlockBehaviour.Properties properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }

        return properties.lightLevel(blockState -> switch (blockState.getValue(TorchflowerCropBlock.AGE)) {
            case 0 -> config.torchflowerStage1Brightness;
            case 1 -> config.torchflowerStage2Brightness;
            default -> config.torchflowerBrightness;
        });
    }

}