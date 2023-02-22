package cn.geterror.serialization.enums;

import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 支持通过Id自定义枚举正反序列化
 * @author hanyecong02
 */
@SuppressWarnings("unused")
public interface IdEnum<T extends Enum<T> & IdEnum<T>> {

    int getId();

    static List<Integer> mapIntList(List<? extends IdEnum> enums){
        return enums.stream().map(IdEnum::getId).collect(Collectors.toList());
    }

    static <E extends Enum<E> & IdEnum<E>> EnumSet<E> allFor(Class<E> elementType, Predicate<E> predicate) {

        EnumSet<E> values = EnumSet.allOf(elementType);

        List<E> elements = values.stream()
                .filter(predicate)
                .collect(Collectors.toList());

        if (elements.isEmpty()) {
            return EnumSet.noneOf(elementType);
        } else {
            return EnumSet.copyOf(elements);
        }

    }

    static <E extends Enum<E> & IdEnum<E>> E findFor(Class<E> elementType, Predicate<E> predicate) {

        Set<E> es = allFor(elementType, predicate);

        if (es.isEmpty()) {
            throw new NoSuchElementException("No enums value for " + elementType + " meets the predicate.");
        }

        return es.iterator().next();
    }

    static <E extends Enum<E> & IdEnum<E>> E findForId(Class<E> elementType, int id) {
        try {
            return findFor(elementType, x -> x.getId() == id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Enum " + elementType + " has no id with " + id);
        }
    }

    static Set<Integer> idSetOf(EnumSet<?> idEnums) {

        return idEnums.stream().map(e -> {
            if (!(e instanceof IdEnum<?>)) {
                throw new IllegalArgumentException("argument must be IdEnum set.");
            }

            return ((IdEnum<?>) e).getId();
        }).collect(Collectors.toSet());
    }

    static <E extends IdEnum<?>> E getById(Class<E> clazz, int id) {

        if (!IdEnum.class.isAssignableFrom(clazz)) {
            throw new ClassCastException("Enum should implement IdEnum interface.");
        }

        //noinspection unchecked, rawtypes
        return (E) findForId((Class) clazz, id);
    }
}
