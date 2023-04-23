package dev.sterner.gorelib.interfaces;

import java.util.UUID;

public interface IBlood {
    UUID getEntity();

    void setEntity(UUID uuid);

    String getName();

    void setName(String name);

    int getId();

    void setId(int entityId);

    int getColor();

    void setColor(int color);
}
