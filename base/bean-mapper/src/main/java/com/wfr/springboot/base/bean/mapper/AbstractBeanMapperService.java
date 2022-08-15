package com.wfr.springboot.base.bean.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * java bean 映射抽象服务
 *
 * @author wangfarui
 * @since 2022/8/11
 */
public abstract class AbstractBeanMapperService implements BeanMapperService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractBeanMapperService.class);

    @Override
    public <S, D> D copy(S source, Class<D> destinationClass) {
        D d = null;
        try {
            d = destinationClass.newInstance();
        } catch (Exception e) {
            LOGGER.error("BeanMapperService copy exception", e);
            throw new RuntimeException(e);
        }
        copy(source, d);
        return d;
    }

    @Override
    public <S, D> List<D> copyList(Iterable<S> source, Class<D> destinationClass) {
        List<D> list = new ArrayList<>();
        for (S s : source) {
            D d = copy(s, destinationClass);
            list.add(d);
        }
        return list;
    }

    @Override
    public <S, D> Set<D> copySet(Iterable<S> source, Class<D> destinationClass) {
        Set<D> set = new HashSet<>();
        for (S s : source) {
            D d = copy(s, destinationClass);
            set.add(d);
        }
        return set;
    }

    @Override
    public <S, D> void copyArray(Iterable<S> source, D[] destination, Class<D> destinationClass) {
        int length = destination.length;
        int num = 0;
        for (S s : source) {
            if (num >= length) break;
            D d = copy(s, destinationClass);
            destination[num++] = d;
        }
    }

    @Override
    public <S, D> void copyArray(S[] source, D[] destination, Class<D> destinationClass) {
        for (int i = 0, sSize = source.length, dSize = destination.length; i < sSize && i < dSize; i++) {
            D d = copy(source[i], destinationClass);
            destination[i] = d;
        }
    }
}
