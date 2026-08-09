package xyz.nikitacartes.glowingtorchflower.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;
//? if >=1.20 <1.21.2 {
/*import net.minecraft.world.level.block.Block;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
//? if >=26.2 {
import org.objectweb.asm.Opcodes;
//?}
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import xyz.nikitacartes.glowingtorchflower.config.MainConfigV1;

@Mixin(Blocks.class)
public class BlocksMixin {

    @Unique
    private static MainConfigV1 config;

    // 26.2 replaced the registration name constants with BlockItemIds / BlockIds fields,
    // so the slice anchors on a field access instead of a string constant.
    // 1.19.4 still takes a Material in BlockBehaviour.Properties.of.
    //? if >=26.2 {
    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
                    target = "Lnet/minecraft/references/BlockItemIds;TORCHFLOWER:Lnet/minecraft/references/BlockItemId;")))
    //?} elif >=1.20 {
    /*@ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower")))
    *///?} else {
    /*@ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of(Lnet/minecraft/world/level/material/Material;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower")))
    *///?}
    private static BlockBehaviour.Properties modifyTorchflower(BlockBehaviour.Properties properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }
        return properties.lightLevel(blockState -> config.torchflowerBrightness);
    }

    // 1.20 to 1.21.1 build the flower pot in a shared Blocks.flowerPot helper, so the
    // injection filters on the potted block. From 1.21.2 on the pot properties come from
    // Blocks.flowerPotProperties() inside <clinit> and a slice picks the torchflower one.
    //? if >=26.2 {
    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Blocks;flowerPotProperties()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
                    target = "Lnet/minecraft/references/BlockIds;POTTED_TORCHFLOWER:Lnet/minecraft/resources/ResourceKey;")))
    private static BlockBehaviour.Properties modifyPottedTorchflower(BlockBehaviour.Properties properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }
        return properties.lightLevel(blockState -> config.torchflowerPotBrightness);
    }
    //?} elif >=1.21.2 {
    /*@ModifyExpressionValue(method = "<clinit>",
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
    *///?} elif >=1.20 {
    /*@ModifyExpressionValue(method = "flowerPot",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0))
    private static BlockBehaviour.Properties modifyPottedTorchflower(BlockBehaviour.Properties properties, Block block) {
        if (block != Blocks.TORCHFLOWER) {
            return properties;
        }
        if (config == null) {
            config = MainConfigV1.load();
        }
        return properties.lightLevel(blockState -> config.torchflowerPotBrightness);
    }
    *///?} else {
    /*@ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of(Lnet/minecraft/world/level/material/Material;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potted_torchflower")))
    private static BlockBehaviour.Properties modifyPottedTorchflower(BlockBehaviour.Properties properties) {
        if (config == null) {
            config = MainConfigV1.load();
        }
        return properties.lightLevel(blockState -> config.torchflowerPotBrightness);
    }
    *///?}

    //? if >=26.2 {
    @ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
                    target = "Lnet/minecraft/references/BlockItemIds;TORCHFLOWER_CROP:Lnet/minecraft/references/BlockItemId;")))
    //?} elif >=1.20 {
    /*@ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower_crop")))
    *///?} else {
    /*@ModifyExpressionValue(method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of(Lnet/minecraft/world/level/material/Material;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower_crop")))
    *///?}
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
