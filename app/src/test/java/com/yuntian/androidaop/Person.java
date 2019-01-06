package com.yuntian.androidaop;

/**
 * @author chulingyan
 * @time 2019/01/05 22:12
 * @describe
 */
public class Person {


    public boolean  buy(){
        System.out.println("买食材");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("买完食材");
        return true;
    }

    public void  cook(boolean isCan)
    {
        if (isCan){
            System.out.println("开始做菜");
        }else {
            System.out.println("没有煤气,做不了菜");
        }
    }
}
