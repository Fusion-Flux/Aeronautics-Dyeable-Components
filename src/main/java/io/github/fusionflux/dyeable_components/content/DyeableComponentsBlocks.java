package io.github.fusionflux.dyeable_components.content;

import dev.eriksonn.aeronautics.content.particle.LevititeSparkleParticleData;
import dev.ryanhcode.offroad.content.components.TireLike;
import dev.ryanhcode.offroad.content.items.tire.TireItem;
import dev.ryanhcode.offroad.index.OffroadDataComponents;
import io.github.fusionflux.dyeable_components.DyeableComponents;
import dev.eriksonn.aeronautics.content.components.Levitating;
import dev.eriksonn.aeronautics.index.AeroBlocks;
import dev.eriksonn.aeronautics.index.AeroDataComponents;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class DyeableComponentsBlocks {
    public static final ResourceLocation DYED_LEVITITE_TAB_SECTION = DyeableComponents.id("levitite");
    public static final ResourceLocation DYED_WHEEL_TAB_SECTION = DyeableComponents.id("wheels");

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DyeableComponents.ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DyeableComponents.ID);

    public static final Map<DyeColor, DeferredBlock<Block>> DYED_LEVITITE_BLOCKS = DyeableComponents.colorMap(
            color -> BLOCKS.register(color.getSerializedName() + "_levitite", () -> {
                BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(AeroBlocks.LEVITITE.get());
                return new Block(properties);
            })
    );

    public static final Map<DyeColor, DeferredItem<Item>> DYED_LEVITITE_ITEMS = DyeableComponents.colorMap(color -> {
        LevititeSparkleParticleData particleData = new LevititeSparkleParticleData(color.getTextureDiffuseColor());
        Levitating levitating = new Levitating(0.93f, Optional.of(particleData));

        DeferredItem<Item> holder = ITEMS.register(color.getSerializedName() + "_levitite", () -> new BlockItem(
                DYED_LEVITITE_BLOCKS.get(color).get(),
                new Item.Properties().component(AeroDataComponents.LEVITATING, levitating)
        ));

        SimulatedRegistrate.TAB_ITEMS.add(holder::value);
        SimulatedRegistrate.ITEM_TO_SECTION.put(holder.getId(), DYED_LEVITITE_TAB_SECTION);
        return holder;
    });

    public static final Map<DyeColor, DeferredItem<Item>> DYED_SMALL_TIRE_ITEMS = DyeableComponents.colorMap(createDyedTireMap("small_tire", 0.75f));
    public static final Map<DyeColor, DeferredItem<Item>> DYED_TIRE_ITEMS = DyeableComponents.colorMap(createDyedTireMap("tire", 0.96875f));
    public static final Map<DyeColor, DeferredItem<Item>> DYED_LARGE_TIRE_ITEMS = DyeableComponents.colorMap(createDyedTireMap("large_tire", 1.25f));
    public static final Map<DyeColor, DeferredItem<Item>> DYED_MONSTROUS_TIRE_ITEMS = DyeableComponents.colorMap(createDyedTireMap("monstrous_tire", 2.0f));

    private static @NotNull Function<DyeColor, DeferredItem<Item>> createDyedTireMap(String name, float radius) {
        return color -> {
            DeferredItem<Item> holder = ITEMS.register(color.getSerializedName() + "_" + name, () ->
                    new TireItem(new Item.Properties().component(OffroadDataComponents.TIRE, new TireLike(radius)))
            );

            SimulatedRegistrate.TAB_ITEMS.add(holder::value);
            SimulatedRegistrate.ITEM_TO_SECTION.put(holder.getId(), DYED_WHEEL_TAB_SECTION);
            return holder;
        };
    }
}
