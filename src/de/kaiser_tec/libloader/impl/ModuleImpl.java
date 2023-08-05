package de.kaiser_tec.libloader.impl;

import de.kaiser_tec.libloader.LibModule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class ModuleImpl implements LibModule {

    private final Object module;
    private final Method getName;
    private final Method getClasses;

    public ModuleImpl(Object module, Method getName, Method getClasses) {
        this.module = module;
        this.getName = getName;
        this.getClasses = getClasses;
    }

    @Override
    public String getName() {
        try {
            return (String) getName.invoke(module);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Class<?>> getClasses() {
        try {
           return  (Collection<Class<?>>) getClasses.invoke(module);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
