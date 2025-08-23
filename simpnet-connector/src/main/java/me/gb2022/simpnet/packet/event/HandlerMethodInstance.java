package me.gb2022.simpnet.packet.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public final class HandlerMethodInstance {
    private final int priority;
    private final boolean ignoreCancel;
    private final Method method;
    private final Object handler;

    public HandlerMethodInstance(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
        this.method.setAccessible(true);

        PacketEvent handlerAnnotation = method.getAnnotation(PacketEvent.class);
        this.priority = handlerAnnotation.priority();
        this.ignoreCancel = handlerAnnotation.ignoreCancel();
    }

    public void call(Object event) {
        if (event instanceof Cancellable e2) {
            if (!this.ignoreCancel && (e2.isCancelled())) {
                return;
            }
        }
        try {
            this.method.invoke(this.handler, event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new EventCallException(this.handler, event, e);
        }
    }

    public Object getHandler() {
        return handler;
    }

    public Method getMethod() {
        return method;
    }

    public int getPriority() {
        return priority;
    }
}
