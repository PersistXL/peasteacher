package com.peas.hsf.tool;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by duanyihui on 2016/12/5.
 */
public class ClassUtil {

    public static Set<Class<?>> getClasses(String pack) {
        return getClasses(pack, true);
    }

    public static Set<Class<?>> getClasses(String pack, boolean recursive) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                switch (protocol) {
                    case "file":
                        findAndAddClassesInPackageByFile(packageName, URLDecoder.decode(url.getFile(), "UTF-8"), recursive, classes);
                        break;
                    case "jar":
                        findAndAddClassesInPackageByJar(packageName, url, packageDirName, recursive, classes);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static void findAndAddClassesInPackageByJar(String packageName,
                                                        URL url,
                                                        String packageDirName,
                                                        final boolean recursive,
                                                        Set<Class<?>> classes) {
        try {
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }
                if (name.startsWith(packageDirName)) {
                    int idx = name.lastIndexOf('/');
                    if (idx != -1) {
                        packageName = name.substring(0, idx).replace('/', '.');
                    }
                    if ((idx != -1) || recursive) {
                        if (name.endsWith(".class") && !entry.isDirectory()) {
                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                            try {
                                addClass(classes, packageName + '.' + className);
                            } catch (Throwable e) {
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 以文件的形式来获取包下的所有Class
     * <p>
     */
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                         String packagePath,
                                                         final boolean recursive,
                                                         Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith(".class")));
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    addClass(classes, packageName + '.' + className);
                } catch (Throwable e) {
                }
            }
        }
    }

    private static void addClass(Set<Class<?>> classes, String classFullName) {
        try {
            classes.add(Thread.currentThread().getContextClassLoader().loadClass(classFullName));
        } catch (Throwable t) {
        }
    }
}


