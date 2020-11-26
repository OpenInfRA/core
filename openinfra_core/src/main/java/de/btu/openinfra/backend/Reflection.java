package de.btu.openinfra.backend;

import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeElementsScanner;

/**
 * This class supplies methods to request classes for a given package name or
 * a reflections object (<a href="https://code.google.com/p/reflections/">
 * Reflections</a>).
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class Reflection {

    /**
     * This method finds all classes for the given package name (the result
     * also includes classes of the sub packages).
     * @param packageName the package name
     * @return list of all classes for the given package name
     */
    public static <Type> List<Class<? extends Type>> findAllClasses(
            String packageName) {
        Reflections reflections = new Reflections(
                packageName,
                new SubTypesScanner(false),
                new ResourcesScanner(),
                new TypeElementsScanner());

        return findAllClasses(reflections);
    }

    /**
     * This method finds all classes for the given reflections object.
     * @param reflections the reflections object
     * @return list of all classes for the given reflections object
     */
    public static <Type> List<Class<? extends Type>> findAllClasses(
            Reflections reflections) {
        // get all class names
        Set<String> classNames = reflections.getStore()
                .get("TypeElementsScanner").keySet();

        // return the list of classes
        return ReflectionUtils.forNames(classNames, reflections
              .getConfiguration().getClassLoaders());
    }

}
