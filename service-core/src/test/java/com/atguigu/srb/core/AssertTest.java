package com.atguigu.srb.core;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * @author cz
 * @create 2021/4/20 15:22
 */
public class AssertTest {

    @Test
    public void test1(){
        Object a = null;
        if (a == null){
            throw new IllegalArgumentException("canshuyichang ");
        }
    }

    @Test
    public void test2(){
        Object a = null;
        //用断言替代if
        Assert.notNull(a,"参数不能为空");
    }
}
