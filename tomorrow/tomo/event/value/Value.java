package tomorrow.tomo.event.value;

public class Value<V> {
    public boolean drag;
    public float animX;
    public float animX1;
    public float optionAnim = 0;
    public float optionAnimNow = 0;

    public String displayName;
    public String name;
    public V value;
    public boolean visible;

    public Value(String displayName, String name) {
        this.displayName = displayName;
        this.name = name;
        this.visible = true;
    }

    public Value(String displayName, String name, boolean visible) {
        this.displayName = displayName;
        this.name = name;
        this.visible = visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
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

