package com.wfr.springboot.base.bean.mapper;

import java.util.List;
import java.util.Set;

/**
 * {@link BeanMapper} 服务类
 *
 * @author wangfarui
 * @since 2022/8/11
 */
public interface BeanMapperService {

    /**
     * 从源对象拷贝到目标对象类型
     *
     * @param source           源对象
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象
     */
    <S, D> D copy(S source, Class<D> destinationClass);

    /**
     * 从源对象拷贝到目标对象
     *
     * @param source      源对象
     * @param destination 目标对象
     * @param <S>         源对象类型
     * @param <D>         目标对象类型
     */
    <S, D> void copy(S source, D destination);

    /**
     * 从源对象集合拷贝到目标对象列表
     *
     * @param source           源对象集合
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象列表
     */
    <S, D> List<D> copyList(Iterable<S> source, Class<D> destinationClass);

    /**
     * 从源对象集合拷贝到目标对象Set
     *
     * @param source           源对象集合
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     * @return 目标对象Set
     */
    <S, D> Set<D> copySet(Iterable<S> source, Class<D> destinationClass);

    /**
     * 从源对象集合拷贝到目标对象数组
     *
     * @param source           源对象集合
     * @param destination      目标对象数组
     * @param destinationClass 目标对象类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     */
    <S, D> void copyArray(Iterable<S> source, D[] destination, Class<D> destinationClass);

    /**
     * 从源对象数组拷贝到目标对象数组
     *
     * @param source           源对象数组
     * @param destination      目标对象数组
     * @param destinationClass 目对象标类型
     * @param <S>              源对象类型
     * @param <D>              目标对象类型
     */
    <S, D> void copyArray(S[] source, D[] destination, Class<D> destinationClass);
}
