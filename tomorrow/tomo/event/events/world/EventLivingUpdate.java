package tomorrow.tomo.event.events.world;

import net.minecraft.entity.Entity;
import tomorrow.tomo.event.Event;

public class EventLivingUpdate extends Event {
    private Entity entity;
    public EventLivingUpdate(Entity entity) {
        super();
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
