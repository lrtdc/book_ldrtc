package org.light.cxf;

import javax.xml.ws.Endpoint;

public class CxfServer {

    private final static String ADDRESS = "http://localhost:8080/test/jaxws/services/HelloWorld";  
    
    public static void main(String[] args) {  
//        HelloWorldImpl implementor = new HelloWorldImpl();  
//        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();  
//        // 除了http://localhost部分其他部分可以随便写，但格式不能乱 http://IP:PORT/NAME  
//        factory.setAddress("http://localhost:8080/ws/hello");  
//        factory.setServiceClass(HelloWorld.class);//可省，但不建议，因为可能会有些小问题  
//        factory.setServiceBean(implementor);  
////        //LoggingInInterceptor和LoggingOutInterceptor是日志拦截器，用于输入和输出时显示日志  
//        factory.getInInterceptors().add(new LoggingInInterceptor());  
//        factory.getOutInterceptors().add(new LoggingOutInterceptor());  
//        factory.create();  
//        System.out.println("ws is published");  
        
        
        HelloWorld hw = new HelloWorldImpl();  
        Endpoint.publish(ADDRESS, hw);  
    }  
    
}
