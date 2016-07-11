package org.light.ice;

import Ice.Current;
import myDemo._DemoDisp;

public class DemoI extends _DemoDisp {

    /**
     * 
     */
    private static final long serialVersionUID = 8823361341775010885L;

    @Override
    public String say(String s, Current __current) {
        System.out.println("---收到客户端请求参数："+s);
        return "Demo Test : "+s;
    }

}
