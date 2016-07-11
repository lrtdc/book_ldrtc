package org.light.cxf;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

public class CxfClient {

  //第一个参数为服务发布的targetNameSpace，可以通过查看对应的wsdl文件获得，默认是发布Service所在包的包名倒过来的形式；第二个参数是serviceName  
    private final static QName SERVICE_NAME = new QName("http://jaxws.sample.cxftest.tiantian.com/", "HelloWorldService");  
    //第一个参数是服务发布的targetNameSpace，第二个参数是portName  
    private final static QName PORT_NAME = new QName("http://jaxws.sample.cxftest.tiantian.com/", "HelloWorldPort");  
    //服务发布的地址  
    private final static String ADDRESS = "http://localhost:8080/test/jaxws/services/HelloWorld";  
      
    public static void main(String args[]) {  
       Service service = Service.create(SERVICE_NAME);  
       //根据portName、服务发布地址、数据绑定类型创建一个Port。  
       service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, ADDRESS);//默认是SOAP1.1Binding  
       //获取服务  
       HelloWorld hw = service.getPort(HelloWorld.class);  
       String response = hw.sayHi("world");  
       System.out.println(response);  
    }  

}
