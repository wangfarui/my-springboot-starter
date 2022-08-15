package com.wfr.springboot.base.bean.mapper.orika;

import com.wfr.springboot.base.bean.mapper.AbstractBeanMapperService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.List;
import java.util.Set;

/**
 * 基于 Spring {@link MapperFacade} 实现的 BeanMapper 服务
 *
 * @author wangfarui
 * @since 2022/8/11
 */
public class OrikaBeanMapperService extends AbstractBeanMapperService {

    private final MapperFacade mapperFacade;

    public OrikaBeanMapperService(MapperFacade mapperFacade) {
        this.mapperFacade = mapperFacade;
    }

    @Override
    public <S, D> void copy(S source, D destination) {
        mapperFacade.map(source, destination);
    }

    @Override
    public <S, D> D copy(S source, Class<D> destinationClass) {
        return mapperFacade.map(source, destinationClass);
    }

    @Override
    public <S, D> List<D> copyList(Iterable<S> source, Class<D> destinationClass) {
        return mapperFacade.mapAsList(source, destinationClass);
    }

    @Override
    public <S, D> Set<D> copySet(Iterable<S> source, Class<D> destinationClass) {
        return mapperFacade.mapAsSet(source, destinationClass);
    }

    @Override
    public <S, D> void copyArray(Iterable<S> source, D[] destination, Class<D> destinationClass) {
        mapperFacade.mapAsArray(destination, source, destinationClass);
    }

    @Override
    public <S, D> void copyArray(S[] source, D[] destination, Class<D> destinationClass) {
        mapperFacade.mapAsArray(destination, source, destinationClass);
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
    public <S, D> D copy(S source, Type<S> sourceType, Type<D> destinationType) {
        return mapperFacade.map(source, sourceType, destinationType);
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
    public <S, D> List<D> copyList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
        return mapperFacade.mapAsList(sourceList, sourceType, destinationType);
    }

    /**
     * 极致性能的拷贝出新类型对象到数组
     * <p>
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     *
     * @param source          源对象数组
     * @param destination     目标对象数组
     * @param sourceType      源对象类型
     * @param destinationType 目标对象类型
     * @param <S>             源对象类型
     * @param <D>             目标对象类型
     * @return 目标对象数组
     */
    public <S, D> D[] copyArray(S[] source, D[] destination, Type<S> sourceType, Type<D> destinationType) {
        return mapperFacade.mapAsArray(destination, source, sourceType, destinationType);
    }
}