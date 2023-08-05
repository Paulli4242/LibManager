package de.kaiser_tec.libloader.impl;

import java.util.function.Supplier;

public class LibManagerClassSupplier implements Supplier<Class<?>> {
    @Override
    public Class<?> get() {
        return LibManagerImpl.class;
    }
}
