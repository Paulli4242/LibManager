import de.kaiser_tec.libloader.ClassLoaderStreamSupplier;
import de.kaiser_tec.libloader.LibManager;
import test.pack.name.Lib;
import test.pack.name.Lib2;

import java.io.File;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        LibManager.setUpLibManagerReflection(Set.of(ClassLoader.getSystemClassLoader()));
        File f = new File("libs");
        f.mkdirs();
        System.out.println(f.getAbsolutePath());
        LibManager manager = new LibManager(f);
        manager.loadLib("Lib","test.pack.name.LibLibModule", new ClassLoaderStreamSupplier(ClassLoader.getSystemClassLoader(),"testlib.jar"));

        System.out.println(new Lib().getTest());

        Lib2 l = new Lib2();
        System.out.println(l.count());
        System.out.println(l.count());
    }
}
