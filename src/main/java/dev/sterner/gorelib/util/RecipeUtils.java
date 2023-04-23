package dev.sterner.gorelib.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RecipeUtils {
    public static Stream<JsonElement> arrayStream(JsonArray array) {
        return IntStream.range(0, array.size()).mapToObj(array::get);
    }

    /**
     * Deserialize a DefaultedList of ItemStacks from a json array
     */
    public static DefaultedList<ItemStack> deserializeStacks(JsonArray array) {
        if (array.isJsonArray()) {
            return arrayStream(array.getAsJsonArray()).map(entry -> deserializeStack(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
        } else {
            return DefaultedList.copyOf(deserializeStack(array.getAsJsonObject()));
        }
    }

    /**
     * Deserialize an ItemStack from a json object
     */
    public static @NotNull ItemStack deserializeStack(JsonObject object) {
        final Identifier id = new Identifier(JsonHelper.getString(object, "item"));
        final Item item = Registry.ITEM.get(id);
        if (Items.AIR == item) {
            throw new IllegalStateException("Invalid item: " + item);
        }
        int count = 1;
        if (object.has("count")) {
            count = JsonHelper.getInt(object, "count");
        }
        final ItemStack stack = new ItemStack(item, count);
        if (object.has("nbt")) {
            final NbtCompound tag = (NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, object.get("nbt"));
            stack.setNbt(tag);
        }
        return stack;
    }

    /**
     * Deserialize a List of entityTypes from a json array
     */
    public static List<EntityType<?>> deserializeEntityTypes(JsonArray array) {
        if (array.isJsonArray()) {
            return arrayStream(array.getAsJsonArray()).map(entry -> deserializeEntityType(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
        } else {
            return DefaultedList.copyOf(deserializeEntityType(array.getAsJsonObject()));
        }
    }

    /**
     * Deserialize an EntityType from a json object
     */
    public static @NotNull EntityType<?> deserializeEntityType(JsonObject object) {
        final Identifier id = new Identifier(JsonHelper.getString(object, "entity"));
        return Registry.ENTITY_TYPE.get(id);
    }

    /**
     * Deserialize a List of StatusEffectInstances from a json array
     */
    public static List<StatusEffectInstance> deserializeStatusEffects(JsonArray array) {
        if (array.isJsonArray()) {
            return arrayStream(array.getAsJsonArray()).map(entry -> deserializeStatusEffect(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
        } else {
            return DefaultedList.copyOf(deserializeStatusEffect(array.getAsJsonObject()));
        }
    }

    /**
     * Deserialize a StatusEffectInstance from a json object
     */
    public static @Nullable StatusEffectInstance deserializeStatusEffect(JsonObject object) {
        final Identifier id = new Identifier(JsonHelper.getString(object, "id"));
        final int duration = JsonHelper.getInt(object, "duration");
        final int amplifier = JsonHelper.getInt(object, "amplifier");
        StatusEffect statusEffect = Registry.STATUS_EFFECT.get(id);
        if (statusEffect != null) {
            return new StatusEffectInstance(statusEffect, duration, amplifier);
        }
        return null;
    }

    /**
     * Deserialize a DefaultedList of Ingredients from a json array
     */
    public static DefaultedList<Ingredient> deserializeIngredients(JsonArray json) {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        for (int i = 0; i < json.size(); i++) {
            Ingredient ingredient = Ingredient.fromJson(json.get(i));
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    public static boolean containsAllIngredients(List<Ingredient> ingredients, List<ItemStack> items) {
        List<Integer> checkedIndexes = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < items.size(); i++) {
                if (!checkedIndexes.contains(i)) {
                    if (ingredient.test(items.get(i))) {
                        checkedIndexes.add(i);
                        break;
                    }
                }
            }
        }
        return checkedIndexes.size() == ingredients.size();
    }

    public static class DefaultedListCollector<T> implements Collector<T, DefaultedList<T>, DefaultedList<T>> {

        private static final Set<Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

        public static <T> DefaultedListCollector<T> toList() {
            return new DefaultedListCollector<>();
        }

        @Override
        public Supplier<DefaultedList<T>> supplier() {
            return DefaultedList::of;
        }

        @Override
        public BiConsumer<DefaultedList<T>, T> accumulator() {
            return DefaultedList::add;
        }

        @Override
        public BinaryOperator<DefaultedList<T>> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<DefaultedList<T>, DefaultedList<T>> finisher() {
            return i -> (DefaultedList<T>) i;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return CH_ID;
        }
    }
}
