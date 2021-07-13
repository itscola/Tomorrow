/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.event.value;

public class Value<V> {
    public boolean drag;
    public float animX;
    public float animX1;
    public float optionAnim = 0;//present
    public float optionAnimNow = 0;//present

    private String displayName;
    private String name;
    private V value;

    public Value(String displayName, String name) {
        this.displayName = displayName;
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}

