package dev.sterner.gorelib.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemStackBeamParticleEffect implements ParticleEffect {
    public static final ParticleEffect.Factory<ItemStackBeamParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
        public ItemStackBeamParticleEffect read(ParticleType<ItemStackBeamParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            ItemStringReader.ItemResult itemResult = ItemStringReader.item(Registries.ITEM.getReadOnlyWrapper(), stringReader);
            ItemStack itemStack = new ItemStackArgument(itemResult.item(), itemResult.nbt()).createStack(1, false);
            stringReader.expect(' ');
            int maxAge = stringReader.readInt();
            return new ItemStackBeamParticleEffect(particleType, itemStack, maxAge);
        }

        public ItemStackBeamParticleEffect read(ParticleType<ItemStackBeamParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new ItemStackBeamParticleEffect(particleType, packetByteBuf.readItemStack(), packetByteBuf.readInt());
        }
    };

    private final ParticleType<ItemStackBeamParticleEffect> type;
    private final ItemStack stack;
    private final int maxAge;


    public ItemStackBeamParticleEffect(ParticleType<ItemStackBeamParticleEffect> type, ItemStack stack, int maxAge) {
        this.type = type;
        this.stack = stack;
        this.maxAge = maxAge;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    @Override
    public String asString() {
        Identifier var10000 = Registries.PARTICLE_TYPE.getId(this.getType());
        return var10000 + " " + (new ItemStackArgument(this.stack.getRegistryEntry(), this.stack.getNbt())).asString();
    }

    @Override
    public ParticleType<ItemStackBeamParticleEffect> getType() {
        return this.type;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeItemStack(this.stack);
        buf.writeInt(this.maxAge);
    }
}