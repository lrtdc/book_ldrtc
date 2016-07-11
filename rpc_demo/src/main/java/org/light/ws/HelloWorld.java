package org.light.ws;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

// 使用jdk1.6.0_24以上版本
// 1、添加注解
@WebService
public class HelloWorld {
    // 2、至少包含一个可以对外公开的服务
    public String a(String name) {
        return "屌丝" + name;
    }

    // 3、第一个参数称为Binding即绑定地址，
    // 第二个参数是实现者，即谁提供服务
    public static void main(String[] args) {
        HelloWorld h = new HelloWorld();
        Endpoint.publish("http://localhost:8014/hello", h);
    }
}
