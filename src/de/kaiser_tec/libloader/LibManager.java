package de.kaiser_tec.libloader;

import de.kaiser_tec.libloader.impl.LibManagerClassSupplier;
import de.kaiser_tec.libloader.impl.LibManagerImpl;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class LibManager {

    private static Class<?> reflectionClazz;
    private static Constructor<?> reflectionConstructor;
    private static Method reflectionLoadLib;



    private final Object implementation;
    public LibManager(File libDirectory) {
        try {
            implementation = reflectionConstructor.newInstance(libDirectory);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Boolean> loadLibAsync(String name, String moduleClassName, Supplier<InputStream> streamSupplier){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Thread t = new Thread(()->{
            try {
                future.complete((boolean) reflectionLoadLib.invoke(implementation,name,moduleClassName,streamSupplier));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.setName("LibLoaderThread");
        return future;
    }

    public boolean loadLib(String name, String moduleClassName, Supplier<InputStream> streamSupplier) {
        try {
            return (boolean) reflectionLoadLib.invoke(implementation,name,moduleClassName,streamSupplier);
        } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setUpLibManagerReflection(Collection<ClassLoader> classLoader){
        for(ClassLoader cl:classLoader){
            try{
                //get other loaded ClassManager
                Class<?> lm = cl.loadClass("de.kaiser_tec.libloader.LibManager");
                Field f = lm.getDeclaredField("libManagerClass");
                f.setAccessible(true);
                reflectionClazz = (Class<?>) f.get(null);
                //checks if implementation is loaded
            }catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | ClassCastException e){
            }
        }
        if(reflectionClazz == null) {
            reflectionClazz = new LibManagerClassSupplier().get();
        }
        try {

            //Constructor
            reflectionConstructor = reflectionClazz.getDeclaredConstructor(File.class);
            reflectionConstructor.setAccessible(true);

            //reflectionLoadLib
            reflectionLoadLib = reflectionClazz.getDeclaredMethod("loadLib",String.class,String.class, Supplier.class);
            reflectionLoadLib.setAccessible(true);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
