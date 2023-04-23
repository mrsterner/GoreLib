package dev.sterner.gorelib.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;


public class GoreLibC2SPacket {
    private Identifier id;
    private Entity entity;
    private boolean writeFullEntityNbt;
    private Vec3d vec3d;

    private GoreLibC2SPacket(){

    }

    public void send(GoreLibC2SPacket packet) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        byte presenceMask = 0;
        if(packet.entity != null){
            presenceMask |= 1 << 0;
            buf.writeInt(entity.getId());
            buf.writeRegistryValue(Registry.ENTITY_TYPE, entity.getType());
            buf.writeBlockPos(entity.getBlockPos());
            if(packet.writeFullEntityNbt){
                presenceMask |= 1 << 1;
                buf.writeNbt(entity.writeNbt(new NbtCompound()));
            }
        }
        if(packet.vec3d != null){
            presenceMask |= 1 << 2;
            buf.writeDouble(packet.vec3d.x);
            buf.writeDouble(packet.vec3d.y);
            buf.writeDouble(packet.vec3d.z);
        }
        buf.writeByte(presenceMask);
        ClientPlayNetworking.send(id, buf);
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
        int entityId = 0;
        EntityType<?> entityType;
        BlockPos entityPos;
        NbtCompound nbtCompound = new NbtCompound();
        Vec3d vec3d;

        byte presenceMask = buf.readByte();
        if ((presenceMask & (1 << 0)) != 0) {
            entityId = buf.readInt();
            entityType = buf.readRegistryValue(Registry.ENTITY_TYPE);
            entityPos = buf.readBlockPos();
            if((presenceMask & (1 << 1)) != 0){
                nbtCompound = buf.readNbt();
            }
        } else {
            entityPos = null;
            entityType = null;
        }
        if ((presenceMask & (1 << 2)) != 0) {
            vec3d = new Vec3d(buf.readDouble(),buf.readDouble(),buf.readDouble());
        } else {
            vec3d = null;
        }

        int finalEntityId = entityId;
        NbtCompound finalNbtCompound = nbtCompound;
        server.execute(() -> {
            executeServer(player, finalEntityId, entityType, entityPos, finalNbtCompound, vec3d, network, sender);
        });
    }

    public static void executeServer(ServerPlayerEntity player, int entityId, EntityType<?> entityType, BlockPos entityPos, NbtCompound nbtCompound, Vec3d vec3d, ServerPlayNetworkHandler network, PacketSender sender) {

    }

    public static class Builder {
        private final GoreLibC2SPacket packet = new GoreLibC2SPacket();

        public Builder(Identifier id) {
            this.packet.id = id;
        }

        public GoreLibC2SPacket build() {
            return this.packet;
        }

        public Builder setId(Identifier id) {
            this.packet.id = id;
            return this;
        }

        public Builder setEntity(Entity entity) {
            this.packet.entity = entity;
            return this;
        }

        public Builder setVec3d(Vec3d vec3d) {
            this.packet.vec3d = vec3d;
            return this;
        }

    }
}
