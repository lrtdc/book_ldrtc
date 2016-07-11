package org.light.jetty;

//import org.eclipse.jetty.http.HttpCompliance;
//import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class OneHandler {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        
        // connector
//        server.getConnectors()[0].getConnectionFactory(HttpConnectionFactory.class)
//                .setHttpCompliance(HttpCompliance.LEGACY);
//        server.setHandler(new HelloHandler("Hi JettyEmbeded "," light测试"));
        
        // Add a single handler on context "/hello"
        ContextHandler context = new ContextHandler();
        context.setContextPath( "/hello" );
        context.setHandler( new HelloHandler("Hi JettyEmbeded "," light测试") );
        // Can be accessed using http://localhost:8080/hello
        server.setHandler( context );

        server.start();
        server.join();
    }
}
