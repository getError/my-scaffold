package cn.geterror.serialization.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectorUtil {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static <T> Predicate<T> removeDistinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) != null;
    }

    /**
     *
     * @param collection 待比较集合
     * @param aKey collection的转换方式
     * @param bKey 传入值的转换方式
     * @param <A> 集合的类型
     * @param <B> 传入值的类型
     * @param <C> 转换后的类型
     * @return 获得A类型的容器类按照aKey方法取值的话，是否包含B类型的t按照bKey取值的方法，其中t为传入值
     */
    public static <A,B,C> Predicate<B> containByKey(Collection<A> collection, Function<? super A, C> aKey, Function<? super B,C> bKey) {
        return  t->!collection.stream().map(aKey).filter(i-> Objects.equals(bKey.apply(t),i)).collect(Collectors.toList()).isEmpty();
    }

    public static <A,B,C> Predicate<B> notContainByKey(Collection<A> collection, Function<? super A, C> aKey, Function<? super B,C> bKey) {
        return  t->collection.stream().map(aKey).filter(i-> Objects.equals(bKey.apply(t),i)).collect(Collectors.toList()).isEmpty();
    }

    public static <A,B,C> Function<B,C> pick(Collection<A> collection, Function<? super A, C> aKey, BiPredicate<A,B> pickFunc, Function<? super B,C> bKey, BinaryOperator<C> reduceFunc){
        return b->{
            List<C> collect = collection.stream().filter(a->pickFunc.test(a,b)).map(aKey).collect(Collectors.toList());
            collect.add(bKey.apply(b));
            return collect.stream().reduce(reduceFunc).orElse(null);
        };
    }

    public static <K,V> Map<K,V> newMap(K k,V v){
        HashMap<K, V> map = new HashMap<>();
        map.put(k,v);
        return map;
    }
}
