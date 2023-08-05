package de.kaiser_tec.libloader;

import java.io.InputStream;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ClassLoaderStreamSupplier implements Supplier<InputStream> {

    private final ClassLoader classLoader;
    private final String resource;

    public ClassLoaderStreamSupplier(ClassLoader classLoader, String resource) {
        this.classLoader = classLoader;
        this.resource = resource;
    }

    @Override
    public InputStream get() {
        return classLoader.getResourceAsStream(resource);
    }
}
