package dev.sterner.gorelib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

import java.util.Iterator;

public class ItemUtils {

    /**
     *  Populates an inventory DefaultedList<ItemStack> from a nbt
     *
     * @param compound nbt to read inventory from
     * @param name name of the inventory in the nbt
     * @param inv inventory to populate with items from nbt
     */
    public static void readInventory(NbtCompound compound, String name, DefaultedList<ItemStack> inv) {
        if (compound.contains(name)) {
            NbtList tagList = compound.getList(name, 10);

            for (int i = 0; i < tagList.size(); ++i) {
                NbtCompound slot = tagList.getCompound(i);
                int j = slot.getInt("Slot");
                if (j >= 0 && j < inv.size()) {
                    inv.set(j, ItemStack.fromNbt(slot));
                }
            }

        }
    }

    public static DefaultedList<ItemStack> readItemList(NbtCompound compound, String name) {
        return readItemList(compound, name, true);
    }

    /**
     * Populates a DefaultedList<ItemStack> from a nbt
     *
     * @param compound nbt to read items from
     * @param name name of the items' nbt
     * @param includeEmpty if it should include ItemStack.EMPTY
     * @return List of ItemStacks from the nbt
     */
    public static DefaultedList<ItemStack> readItemList(NbtCompound compound, String name, boolean includeEmpty) {
        DefaultedList<ItemStack> items = DefaultedList.of();
        if (!compound.contains(name)) {
            return items;
        }
        NbtList itemList = compound.getList(name, 10);
        for (int i = 0; i < itemList.size(); i++) {
            ItemStack item = ItemStack.fromNbt(itemList.getCompound(i));
            if (!includeEmpty) {
                if (!item.isEmpty()) {
                    items.add(item);
                }
            } else {
                items.add(item);
            }
        }
        return items;
    }

    public static void readItemList(NbtCompound compound, String name, DefaultedList<ItemStack> list) {
        if (compound.contains(name)) {
            NbtList itemList = compound.getList(name, 10);

            for (int i = 0; i < itemList.size() && i < list.size(); ++i) {
                list.set(i, ItemStack.fromNbt(itemList.getCompound(i)));
            }

        }
    }

    /**
     * Writes into a nbt from an inventory DefaultedList<ItemStack>
     *
     * @param compound nbt to write to
     * @param name name of the inventories' nbt
     * @param inv input inventory
     */
    public static void writeInventory(NbtCompound compound, String name, DefaultedList<ItemStack> inv) {
        NbtList tagList = new NbtList();

        for (int i = 0; i < inv.size(); ++i) {
            if (!(inv.get(i)).isEmpty()) {
                NbtCompound slot = new NbtCompound();
                slot.putInt("Slot", i);
                inv.get(i).writeNbt(slot);
                tagList.add(slot);
            }
        }

        compound.put(name, tagList);
    }

    public static void writeItemList(NbtCompound compound, String name, DefaultedList<ItemStack> list) {
        writeItemList(compound, name, list, true);
    }

    public static void writeItemList(NbtCompound compound, String name, DefaultedList<ItemStack> list, boolean includeEmpty) {
        NbtList itemList = new NbtList();
        Iterator<ItemStack> stackIterator = list.iterator();

        while (true) {
            ItemStack stack;
            do {
                if (!stackIterator.hasNext()) {
                    compound.put(name, itemList);
                    return;
                }

                stack = stackIterator.next();
            } while (!includeEmpty && stack.isEmpty());

            itemList.add(stack.writeNbt(new NbtCompound()));
        }
    }
}