package org.light.jetty;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class JettyWS {

    public static void main(String[] args) throws Exception {
        // 实例化1个监听端口8080的Jetty服务对象
        Server server = new Server(8080);
        // ServletHandler继承自ScopedHandler，是Jetty中用于存储所有Filter、FilterMapping、Servlet、ServletMapping的地方，
        // 以及用于实现一次请求所对应的Filter链和Servlet执行流程的类。对Servlet的框架实现中，它也被认为是Handler链的末端，
        //因而在它的doHandle()方法中没有调用nextHandle()方法。
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        // 为特定Servlet映射到特定访问路径
        handler.addServletWithMapping(HelloServlet.class, "/hello");
        //服务启动
        server.start();
        // 当前线程和主线程绑定，和进程共进退
        server.join();
    }
    
    //http://localhost:8080/hello?name=王成光&age=34

    @SuppressWarnings("serial")
    public static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String name = request.getParameter("name");
            String age = request.getParameter("age");
            
            PrintWriter pwt = response.getWriter();
//            pwt.write("姓名：<h1>"+name+"</h1>年龄："+age);
            pwt.append("姓名：<h1>"+name+"</h1>年龄："+age);
            pwt.flush();
            pwt.close();
            
//            response.getWriter().println("姓名：<h1>"+name+"</h1>年龄："+age);
        }
    }

}
