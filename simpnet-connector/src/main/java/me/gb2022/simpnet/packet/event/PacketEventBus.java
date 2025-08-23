package me.gb2022.simpnet.packet.event;

import me.gb2022.simpnet.packet.Packet;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public final class PacketEventBus {
    private final HashMap<Object, HashMap<String, List<HandlerMethodInstance>>> handlers = new HashMap<>(128);

    public void registerEventListener(Object el) {
        if (!this.handlers.containsKey(el)) {
            this.handlers.put(el, new HashMap<>());
        }
        Map<String, List<HandlerMethodInstance>> map = this.handlers.get(el);

        for (Method m : el.getClass().getMethods()) {
            PacketEvent eventHandler = m.getAnnotation(PacketEvent.class);
            if (eventHandler == null) {
                continue;
            }
            if (Modifier.isStatic(m.getModifiers())) {
                continue;
            }
            String eid = m.getParameters()[0].getType().getName();
            HandlerMethodInstance instance = new HandlerMethodInstance(el, m);
            if (!map.containsKey(eid)) {
                map.put(eid, new ArrayList<>());
            }
            map.get(eid).add(instance);
        }
    }

    public void unregisterEventListener(Object el) {
        if (!this.handlers.containsKey(el)) {
            return;
        }
        this.handlers.remove(el);
    }

    public void registerEventListener(Class<?> clazz) {
        if (!this.handlers.containsKey(clazz)) {
            this.handlers.put(clazz, new HashMap<>());
        }
        Map<String, List<HandlerMethodInstance>> map = this.handlers.get(clazz);

        for (Method m : clazz.getMethods()) {
            PacketEvent eventHandler = m.getAnnotation(PacketEvent.class);
            if (eventHandler == null) {
                continue;
            }
            if (!Modifier.isStatic(m.getModifiers())) {
                continue;
            }
            String eid = m.getParameters()[0].getType().getName();
            HandlerMethodInstance instance = new HandlerMethodInstance(null, m);
            if (!map.containsKey(eid)) {
                map.put(eid, new ArrayList<>());
            }
            map.get(eid).add(instance);
        }
    }

    public void unregisterEventListener(Class<?> clazz) {
        if (!this.handlers.containsKey(clazz)) {
            return;
        }
        this.handlers.remove(clazz);
    }

    public void call(Packet event) {
        List<HandlerMethodInstance> handlers = new ArrayList<>(128);

        for (HashMap<String, List<HandlerMethodInstance>> listenerHandlers : this.handlers.values()) {
            if (!listenerHandlers.containsKey(event.getClass().getName())) {
                continue;
            }
            handlers.addAll(listenerHandlers.get(event.getClass().getName()));
        }

        handlers.sort(Comparator.comparingInt(HandlerMethodInstance::getPriority));

        for (HandlerMethodInstance instance : handlers) {
            instance.call(event);
        }
    }
}
