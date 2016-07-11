package org.light.ws.jdk;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class HelloJws {
    
    public String sayHello(String name){
        return "Hi, "+name+", 你好！";
    }
    
    public String getBornDesc(String name, String homeTown){
        return name+" 的故乡是 "+homeTown;
    }
    
    @WebMethod(exclude=true)
    public String getResumeDesc(String name, String degree){
        return name+" 的学历是 "+degree;
    }
    
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/hellojws", new HelloJws());
    }
}
