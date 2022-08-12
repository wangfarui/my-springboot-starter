package com.wfr.springboot.base.bean.mapper.orika;

import com.wfr.springboot.base.bean.mapper.AbstractBeanMapperService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.metadata.Type;

import java.util.List;

/**
 * 基于 Spring {@link MapperFacade} 实现的 BeanMapper 服务
 *
 * @author wangfarui
 * @since 2022/8/11
 */
public class OrikaBeanMapperService extends AbstractBeanMapperService {

    private MapperFacade mapperFacade;

    @Override
    public <S, D> void copy(S source, D destination) {
        mapperFacade.map(source, destination);
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
     * @param <S>             源对象类型
     * @param <D>             目标对象类型
     * @param destination     目标对象数组
     * @param source          源对象数组
     * @param sourceType      源对象类型
     * @param destinationType 源对象类型
     * @return 目标对象数组
     */
    public <S, D> D[] copyArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return mapperFacade.mapAsArray(destination, source, sourceType, destinationType);
    }
}