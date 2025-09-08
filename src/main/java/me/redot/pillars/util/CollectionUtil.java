package me.redot.pillars.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class CollectionUtil {

    private CollectionUtil() {}

    @Nullable
    public static <T, C extends Collection<T>> T randomElement(@NotNull C collection) {
        if (collection.isEmpty()) return null;
        return collection.stream()
                .skip(collection.size() > 1 ? ThreadLocalRandom.current().nextInt(collection.size()) : 0)
                .findAny()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] concat(T[] array1, T[] array2) {
        if (array1 == null) array1 = (T[]) Array.newInstance(array2.getClass().getComponentType(), 0);
        if (array2 == null) array2 = (T[]) Array.newInstance(array1.getClass().getComponentType(), 0);

        T[] result = (T[]) Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length);
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

}
