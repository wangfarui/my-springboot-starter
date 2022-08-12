package com.wfr.springboot.base.bean.mapper;

/**
 * {@link BeanMapper} 各实现类的顺序
 * <p>最终实现类将取排序值最大的类</p>
 *
 * @author wangfarui
 * @since 2022/8/11
 */
public enum BeanMapperServiceOrdered {
    ORIKA(10),
    HU_TOOL(20),
    SPRING(30);

    private final int order;

    BeanMapperServiceOrdered(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }
}