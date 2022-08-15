package com.wfr.springboot.base.bean.mapper;

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

    private static BeanMapperService BEAN_MAPPER_SERVICE;

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
        return BEAN_MAPPER_SERVICE.copy(source, destinationClass);
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
        BEAN_MAPPER_SERVICE.copy(source, destination);
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
        return BEAN_MAPPER_SERVICE.copyList(source, destinationClass);
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
        return BEAN_MAPPER_SERVICE.copySet(source, destinationClass);
    }

    /**
     * 从源集合对象拷贝到目标数组对象
     *
     * @param source           源对象集合
     * @param destination      目标对象数组
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     */
    public static <S, D> void copyArray(Iterable<S> source, D[] destination, Class<D> destinationClass) {
        BEAN_MAPPER_SERVICE.copyArray(source, destination, destinationClass);
    }

    /**
     * 从源数组对象拷贝到目标数组对象
     *
     * @param source           源对象数组
     * @param destination      目标对象数组
     * @param destinationClass 目对象标类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     */
    public static <S, D> void copyArray(S[] source, D[] destination, Class<D> destinationClass) {
        BEAN_MAPPER_SERVICE.copyArray(source, destination, destinationClass);
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
     * 配置 BeanMapperService 实现类
     *
     * @param beanMapperService BeanMapper 服务
     */
    public static void setBeanMapperService(BeanMapperService beanMapperService) {
        if (BEAN_MAPPER_SERVICE == null) {
            BEAN_MAPPER_SERVICE = beanMapperService;
        }
    }
}
