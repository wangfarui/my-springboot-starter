package com.wfr.springboot.base.bean.mapper;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * BeanMapper 测试类
 *
 * @author wangfarui
 * @since 2022/8/2
 */
public class BeanMapperTest {

    /**
     * 100 size use time:
     * orika use time : 95317800
     * hutool use time : 104420100
     * spring use time : 108595800
     * <p>
     * 1000 size use time:
     * orika use time : 86626600
     * hutool use time : 117273800
     * spring use time : 108322100
     * <p>
     * 10000 size use time:
     * orika use time : 96582700
     * hutool use time : 149189200
     * spring use time : 106992200
     * <p>
     * 100000 size use time:
     * orika use time : 120014700
     * hutool use time : 257560300
     * spring use time : 132152100
     * <p>
     * 多次1000 size use time:
     * spring use time : 104891900
     * spring use time : 2369500
     * hutool use time : 124324100
     * hutool use time : 10030400
     * orika use time : 87074000
     * orika use time : 9007400
     */
    @Test
    public void mapperEfficiencyTest() {
        int i = 0;
        int size = 1000;
        List<A> aList = new ArrayList<>(size);
        for (; i < size; i++) {
            ArrayList<String> objects = new ArrayList<>();
            objects.add("list:" + size);
            aList.add(new A(i, "zhangsan" + i, objects, "备注" + i));
        }

//        mapperBuSpring(aList);
//        mapperBuSpring(aList);
//        mapperByHuTool(aList);
//        mapperByHuTool(aList);
        mapperByOrika(aList);
        mapperByOrika(aList);

    }

    private void mapperByOrika(List<A> aList) {
        DefaultMapperFactory build = new DefaultMapperFactory.Builder().build();
        MapperFacade mapperFacade = build.getMapperFacade();

        long nowNanoTime = System.nanoTime();
        List<B> bListByOrika = mapperFacade.mapAsList(aList, B.class);
        long endNanoTime = System.nanoTime();
        System.out.println("orika use time : " + (endNanoTime - nowNanoTime));
        System.out.println(bListByOrika.get(0).toString());
    }

    /**
     * 反射
     */
    private void mapperByHuTool(List<A> aList) {
        long nowNanoTime = System.nanoTime();
        List<B> bListByHuTool = BeanUtil.copyToList(aList, B.class);
        long endNanoTime = System.nanoTime();
        System.out.println("hutool use time : " + (endNanoTime - nowNanoTime));
        System.out.println(bListByHuTool.get(0).toString());
    }

    /**
     * 反射
     */
    private void mapperBuSpring(List<A> aList) {
        long nowNanoTime = System.nanoTime();
        List<B> bListBySpring = new ArrayList<>(aList.size());
        for (A a : aList) {
            B b = new B();
            BeanUtils.copyProperties(a, b);
            bListBySpring.add(b);
        }
        long endNanoTime = System.nanoTime();
        System.out.println("spring use time : " + (endNanoTime - nowNanoTime));
        System.out.println(bListBySpring.get(0).toString());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class A {
        private Integer id;
        private String name;
        private List<String> codes;
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class B {
        private Integer id;
        private String name;
        private List<String> codes;
        private String remark;
    }
}
