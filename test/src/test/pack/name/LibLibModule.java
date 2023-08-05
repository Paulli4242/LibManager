package test.pack.name;

import de.kaiser_tec.libloader.LibModule;

import java.util.Collection;
import java.util.HashSet;

public class LibLibModule implements LibModule {

    private static final HashSet<Class<?>> classes;

    static {
        classes = new HashSet<>();
        classes.add(Lib.class);
        classes.add(Lib2.class);
    }

    @Override
    public String getName() {
        return "Lib";
    }

    @Override
    public Collection<Class<?>> getClasses() {
        return null;
    }
}
