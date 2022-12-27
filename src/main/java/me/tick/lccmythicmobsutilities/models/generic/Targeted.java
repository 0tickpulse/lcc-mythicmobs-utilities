package me.tick.lccmythicmobsutilities.models.generic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;

/**
 * A target that can be a location or entity.
 * In Mythic, skills can target either a location or an entity.
 */
public class Targeted {
    public AbstractLocation location;
    public AbstractEntity entity;
    public Targeted(AbstractLocation location) {
        this.location = location;
    }
    public Targeted(AbstractEntity entity) {
        this.entity = entity;
    }
    public boolean isLocation() {
        return location != null;
    }
    public boolean isEntity() {
        return entity != null;
    }
}
