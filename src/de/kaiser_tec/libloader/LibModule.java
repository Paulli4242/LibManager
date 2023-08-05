package de.kaiser_tec.libloader;

import java.util.Collection;

public interface LibModule {

    String getName();
    Collection<Class<?>> getClasses();

}
