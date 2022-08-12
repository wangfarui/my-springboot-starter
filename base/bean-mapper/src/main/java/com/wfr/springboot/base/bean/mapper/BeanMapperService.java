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

    <S, D> D copy(S source, Class<D> destinationClass);

    <S, D> void copy(S source, D destination);

    <S, D> List<D> copyList(Iterable<S> source, Class<D> destinationClass);

    <S, D> Set<D> copySet(Iterable<S> source, Class<D> destinationClass);

    <S, D> void copyArray(Iterable<S> source, D[] destination, Class<D> destinationClass);

    <S, D> void copyArray(S[] source, D[] destination, Class<D> destinationClass);
}
