/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface EventHandler {
    public byte priority() default 1;
}

