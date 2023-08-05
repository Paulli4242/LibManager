package de.kaiser_tec.libloader.impl;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;

public class LibManagerImpl {

    private static final HashSet<LibEntry> loadedLibs = new HashSet<>();
    private final File libDirectory;
    private LibManagerImpl(File libDirectory) {
        this.libDirectory = libDirectory;
    }

    @SuppressWarnings("unused")
    private boolean loadLib(String name, String moduleClassName, Supplier<InputStream> streamSupplier) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, IOException {
        for(LibEntry e : loadedLibs){
            if(e.moduleClass().getName().equals(moduleClassName)){
                return true;
            }
        }
        File f = new File(libDirectory,moduleClassName);
        System.out.println(f.getAbsolutePath());
        if(!f.exists()){
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            InputStream in = streamSupplier.get();
            in.transferTo(out);
            out.close();
            in.close();
        }
        URLClassLoader classLoader = new URLClassLoader(new URL[]{f.toURI().toURL()},ClassLoader.getSystemClassLoader());
        Class<?> clazz = classLoader.loadClass(moduleClassName);
        //setup reflection
        Constructor<?> construct = clazz.getDeclaredConstructor();
        construct.setAccessible(true);
        Method getClass = clazz.getDeclaredMethod("getClasses");
        getClass.setAccessible(true);
        //reflect
        Object libModule = construct.newInstance();
        LibEntry entry = new LibEntry(name,clazz);
        loadedLibs.add(entry);
        return true;

    }
}