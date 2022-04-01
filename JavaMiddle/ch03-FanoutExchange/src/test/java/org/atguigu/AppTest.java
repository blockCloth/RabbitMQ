package org.atguigu;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue(){
        char c = '中';

        String s1 = "hello";
        String s2 = new String("hello");

        if (s2 == s1)
            System.out.println("111");
        else
            System.out.println("");
    }
}
