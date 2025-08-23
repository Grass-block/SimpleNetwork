package me.gb2022.simpnet.packet.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an event handler annotation for a method
 * <p>target method should have one arg(event class) and no return.</p>
 *
 * @author GrassBlock2022
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PacketEvent {
    int priority() default 0;

    boolean ignoreCancel() default false;
}
