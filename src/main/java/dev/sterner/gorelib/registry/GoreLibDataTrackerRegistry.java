package dev.sterner.gorelib.registry;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;

public interface GoreLibDataTrackerRegistry {
    TrackedDataHandler<DefaultedList<ItemStack>> INVENTORY = new TrackedDataHandler<>() {

        @Override
        public void write(PacketByteBuf buf, DefaultedList<ItemStack> itemStacks) {
            buf.writeInt(itemStacks.size());
            for (ItemStack itemStack : itemStacks) {
                buf.writeItemStack(itemStack);
            }
        }

        @Override
        public DefaultedList<ItemStack> read(PacketByteBuf buf) {
            int length = buf.readInt();
            DefaultedList<ItemStack> list = DefaultedList.ofSize(length, ItemStack.EMPTY);
            list.replaceAll(ignored -> buf.readItemStack());

            return list;
        }

        @Override
        public DefaultedList<ItemStack> copy(DefaultedList<ItemStack> itemStacks) {
            DefaultedList<ItemStack> list = DefaultedList.ofSize(itemStacks.size(), ItemStack.EMPTY);

            for (int i = 0; i < itemStacks.size(); ++i) {
                list.set(i, itemStacks.get(i).copy());
            }

            return list;
        }
    };

    static void init(){
        TrackedDataHandlerRegistry.register(INVENTORY);
    }
}
