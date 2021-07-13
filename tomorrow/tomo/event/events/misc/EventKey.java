/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.event.events.misc;

import tomorrow.tomo.event.Event;

public class EventKey
extends Event {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

