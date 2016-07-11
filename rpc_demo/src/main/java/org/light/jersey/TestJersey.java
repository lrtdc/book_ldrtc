package org.light.jersey;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

//import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
//import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class TestJersey {
    
//    public void test(){
//        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
//        ResourceConfig config = new ResourceConfig(MyResource.class);
//        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
////        server.start();
//    }

    public static void main(String[] args) {
       TestJersey tj = new TestJersey();
//       tj.test();

    }

}
