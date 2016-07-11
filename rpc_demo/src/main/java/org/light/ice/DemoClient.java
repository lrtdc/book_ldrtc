package org.light.ice;

import Ice.AsyncResult;
import myDemo.DemoPrx;
import myDemo.DemoPrxHelper;

public class DemoClient {

    public static void main(String[] args) {
        int status = 0;
        // Communicator实例
        Ice.Communicator ic = null;
        try {
            // 调用Ice.Util.Initialize()初始化Ice run time
            ic = Ice.Util.initialize(args);
            // 根据servant的名称以及服务器ip、端口获取远程服务代理
            Ice.ObjectPrx base = ic.stringToProxy("FirstIceDemo:tcp -h 127.0.0.1 -p 10000");
//            Ice.ObjectPrx base = ic.stringToProxy("SimplePrinter:default -p 10000");
            // 将上面的代理向下转换成一个Printer接口的代理
            DemoPrx firstDemo = DemoPrxHelper.checkedCast(base);
            // 如果转换成功
            if (firstDemo == null) {
                throw new Error("Invalid proxy");
            }
            // 调用这个代理，将字符串传给它
            //异步调用： AMI
            AsyncResult ar = firstDemo.begin_say("王光的第一个ICE测试--异步调用");
            System.out.println(firstDemo.end_say(ar));
            //同步调用
            System.out.println(firstDemo.say("王光的第一个ICE测试--同步"));
        } catch (Ice.LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
            status = 1;
        } finally {
            if (ic != null) {
                ic.destroy();
            }
        }
        System.exit(status);
    }

}
