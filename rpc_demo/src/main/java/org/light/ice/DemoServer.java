package org.light.ice;

public class DemoServer {

    public static void main(String[] args) {
        int status = 0;
        // Communicator实例，是ice run time的主句柄
        Ice.Communicator ic = null;
        try {
            // 调用Ice.Util.Initialize()初始化Ice run time
            System.out.println("初始化ice run time...");
            ic = Ice.Util.initialize(args); // args参数可传可不传
            // 创建一个对象适配器，传入适配器名字和在10000端口处接收来的请求
            System.out.println("创建对象适配器，监听端口10000...");
            Ice.ObjectAdapter adapter =
                    ic.createObjectAdapterWithEndpoints("DemoAdapter", "default -p 10000");
            // 实例化一个DemoI对象，为Demo接口创建一个servant
            System.out.println("为接口创建servant...");
            Ice.Object object = new DemoI();
            // 调用适配器的add,告诉它有一个新的servant,传递的参数是刚才的servant,这里的“FirstIceDemo”是Servant的名字
            System.out.println("对象适配器加入servant...");
            adapter.add(object, Ice.Util.stringToIdentity("FirstIceDemo"));
            // 调用适配器的activate()方法，激活适配器。被激活后，服务器开始处理来自客户的请求。
            System.out.println("激活适配器，服务器等待处理请求...");
            adapter.activate();
            // 这个方法挂起发出调用的线程，直到服务器实现终止为止。或我们自己发出一个调用关闭。
            ic.waitForShutdown();
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
