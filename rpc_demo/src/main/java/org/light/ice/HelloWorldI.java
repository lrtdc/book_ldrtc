package org.light.ice;

import Ice.Current;
import myHelloWorld._HelloWorldDisp;

public class HelloWorldI extends _HelloWorldDisp {

    /**
     * 
     */
    private static final long serialVersionUID = -4377575729891343023L;

    @Override
    public void say(String s, Current __current) {
        System.out.println("Hello World!"+" the string s is " + s);
    }

}
