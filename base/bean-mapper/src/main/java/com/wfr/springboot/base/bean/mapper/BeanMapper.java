package com.wfr.springboot.base.bean.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.List;
import java.util.Set;

/**
 * Java Bean对象拷贝工具
 *
 * @author wangfarui
 * @since 2022/8/2
 */
public abstract class BeanMapper {

    public static MapperFacade MAPPER_FACADE;

    static {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MAPPER_FACADE = mapperFactory.getMapperFacade();
    }

    /**
     * 从源对象拷贝到目标对象类型
     *
     * @param source           源对象
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象
     */
    public static <S, D> D copy(S source, Class<D> destinationClass) {
        return MAPPER_FACADE.map(source, destinationClass);
    }

    /**
     * 从源对象拷贝到目标对象
     *
     * @param source      源对象
     * @param destination 目标对象
     * @param <S>         源对象类型
     * @param <D>         目标对象类型
     */
    public static <S, D> void copy(S source, D destination) {
        MAPPER_FACADE.map(source, destination);
    }

    /**
     * 从源对象集合拷贝到目标对象列表
     *
     * @param source           源对象集合
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象列表
     */
    public static <S, D> List<D> copyList(Iterable<S> source, Class<D> destinationClass) {
        return MAPPER_FACADE.mapAsList(source, destinationClass);
    }

    /**
     * 从源对象集合拷贝到目标对象Set
     *
     * @param source           源对象集合
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象Set
     */
    public static <S, D> Set<D> copySet(Iterable<S> source, Class<D> destinationClass) {
        return MAPPER_FACADE.mapAsSet(source, destinationClass);
    }

    /**
     * 从源集合对象拷贝到目标数组对象
     *
     * @param destination      目标对象数组
     * @param source           源对象集合
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象数组
     */
    public static <S, D> D[] copyArray(D[] destination, Iterable<S> source, Class<D> destinationClass) {
        return MAPPER_FACADE.mapAsArray(destination, source, destinationClass);
    }

    /**
     * 从源数组对象拷贝到目标数组对象
     *
     * @param destination      目标对象数组
     * @param source           源对象数组
     * @param destinationClass 目对象标类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象数组
     */
    public static <S, D> D[] copyArray(D[] destination, S[] source, Class<D> destinationClass) {
        return MAPPER_FACADE.mapAsArray(destination, source, destinationClass);
    }

    /**
     * 预先获取orika转换所需要的Type，避免每次转换.
     *
     * @param <E>     对象类型
     * @param rawType 要转换的类型
     * @return 转换后的类型
     */
    public static <E> Type<E> getType(final Class<E> rawType) {
        return TypeFactory.valueOf(rawType);
    }

    /**
     * 极致性能的拷贝出新类型对象.
     * <p>
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     *
     * @param <S>             源对象类型
     * @param <D>             目标对象类型
     * @param source          源对象
     * @param sourceType      源对象类型
     * @param destinationType 目标类型
     * @return 目标对象
     */
    public static <S, D> D copy(S source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPER_FACADE.map(source, sourceType, destinationType);
    }

    /**
     * 极致性能的拷贝出新类型对象到ArrayList.
     * <p>
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     *
     * @param <S>             源对象类型
     * @param <D>             目标对象类型
     * @param sourceList      源对象列表
     * @param sourceType      源对象类型
     * @param destinationType 目标类型
     * @return 目标对象对象列表
     */
    public static <S, D> List<D> copyList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
        return MAPPER_FACADE.mapAsList(sourceList, sourceType, destinationType);
    }

    /**
     * 极致性能的拷贝出新类型对象到数组
     * <p>
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     *
     * @param <S>             源对象类型
     * @param <D>             目标对象类型
     * @param destination     目标对象数组
     * @param source          源对象数组
     * @param sourceType      源对象类型
     * @param destinationType 源对象类型
     * @return 目标对象数组
     */
    public static <S, D> D[] copyArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPER_FACADE.mapAsArray(destination, source, sourceType, destinationType);
    }

    /**
     * {@link BeanMapper} 各实现类的顺序
     * <p>最终实现类将取排序值最大的类</p>
     */
    public enum BeanMapperImplOrdered {
        SPRING(10),
        ORIKA(20),
        HU_TOOL(30);

        private final int order;

        BeanMapperImplOrdered(int order) {
            this.order = order;
        }

        public int getOrder() {
            return this.order;
        }
    }
}
