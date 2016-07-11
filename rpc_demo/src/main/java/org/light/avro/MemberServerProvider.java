package org.light.avro;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import org.apache.avro.Protocol;
import org.apache.avro.Protocol.Message;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.generic.GenericResponder;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.specific.SpecificDatumWriter;

import com.ifree.serrpc.builder.MemberIFace;
import com.ifree.serrpc.builder.Members;
import com.ifree.serrpc.impl.MemberIFaceImpl;

public class MemberServerProvider {

	/**
     * 动态序列化：通过动态解析Schema文件进行内容设置，并序列化内容
     * 
     * @throws IOException
     */
    public void MemberInfoDynSer() throws IOException {
        // 1.解析schema文件内容
        Parser parser = new Parser();
        Schema mSchema = parser.parse(this.getClass().getResourceAsStream("/Members.avsc"));
        // 2.构建数据写对象
        DatumWriter<GenericRecord> mGr = new SpecificDatumWriter<GenericRecord>(mSchema);
        DataFileWriter<GenericRecord> mDfw = new DataFileWriter<GenericRecord>(mGr);
        // 3.创建序列化文件
        mDfw.create(mSchema, new File("/Users/a/Desktop/tmp/members.avro"));
        // 4.添加序列化数据
        for (int i = 0; i < 20; i++) {
            GenericRecord gr = new GenericData.Record(mSchema);
            int r = i * new Random().nextInt(50);
            gr.put("userName", "light-" + r);
            gr.put("userPwd", "2016-" + r);
            gr.put("realName", "滔滔" + r + "号");
            mDfw.append(gr);
        }
        // 5.关闭数据文件写对象
        mDfw.close();
        System.out.println("Dyn Builder Ser Start Complete.");
    }

    /**
     * 通过Java工具生成文件方式进行序列化操作 命令：C:\Users\Administrator>java -jar
     * E:\avro\avro-tools-1.7.7.jar compile schema E:\avro\Members.avsc E:\avro
     * 
     * @throws IOException
     */
    public void MemberInfoToolsSer() throws IOException {
        // 1.为Member生成对象进行设置必要的内容，这里实现三种设置方式的演示
        // 1.1、构造方式
        Members m1 = new Members("xiaoming", "123456", "校名");
        // 1.2、属性设置
        Members m2 = new Members();
        m2.setUserName("xiaoyi");
        m2.setUserPwd("888888");
        m2.setRealName("小艺");
        // 1.3、Builder方式设置
        Members m3 = Members.newBuilder().setUserName("xiaohong").setUserPwd("999999").setRealName("小红").build();
        // 2.构建反序列化写对象
        DatumWriter<Members> mDw = new SpecificDatumWriter<Members>(Members.class);
        DataFileWriter<Members> mDfw = new DataFileWriter<Members>(mDw);
        // 2.1.通过对Members.avsc的解析创建Schema
        Schema schema = new Parser().parse(this.getClass().getResourceAsStream("/Members.avsc"));
        // 2.2.打开一个通道，把schema和输出的序列化文件关联起来
        mDfw.create(schema, new File("E:/avro/members.avro"));
        // 4.把刚刚创建的Users类数据追加到数据文件写入对象中
        mDfw.append(m1);
        mDfw.append(m2);
        mDfw.append(m3);
        // 5.关闭数据文件写入对象
        mDfw.close();
        System.out.println("Tools Builder Ser Start Complete.");
    }

    // ******************************************************ser
    // end*********************************************************
    /**
     * 服务端支持的网络通讯协议有：NettyServer、SocketServer、HttpServer
     * 采用HTTPSERVER方式调用
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void MemberHttpRPCDynBuilderServer() throws IOException, InterruptedException {
        // 1.进行业务处理
        GenericResponder gr = bussinessDeal();
        // 2.开启一个HTTP服务端，进行等待客户端的连接
        Server server = new HttpServer(gr, 60090);
        server.start();
        System.out.println("Dyn Builder PRC Start Complete.");
        server.join();
    }

    /**
     * 服务端支持的网络通讯协议有：NettyServer、SocketServer、HttpServer
     * 采用Netty方式调用
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void MemberNettyRPCDynBuilderServer() throws IOException, InterruptedException {
        // 1.进行业务处理
        GenericResponder gr = bussinessDeal();
        // 2.开启一个Netty服务端，进行等待客户端的连接
        Server server = new NettyServer(gr, new InetSocketAddress(60090));
        server.start();
        System.out.println("Dyn Builder PRC Start Complete.");
        server.join();
    }

    /**
     * 主要进行业务处理 服务端逻辑处理 采用动态生成代码处理方式，客户端和服务端只需要有protocol文件即可，不需要手工生成代码
     * 
     * @return
     * @throws IOException
     */
    private GenericResponder bussinessDeal() throws IOException {
        // 1.构建协议
        final Protocol protocol = Protocol.parse(this.getClass().getResourceAsStream("/Members.avpr"));
        // 2.构建业务逻辑及响应客户端
        GenericResponder gr = new GenericResponder(protocol) {
            @Override
            public Object respond(Message message, Object request) throws Exception {
                System.err.println("request:" + request);
                // 3.获取请求信息
                GenericRecord record = (GenericRecord) request;
                GenericRecord retGr = null;
                // 4.判断请求的方法
                if (message.getName().equals("login")) {
                    // 5.获取到传输的参数
                    Object obj = record.get("m");

                    GenericRecord mGr = (GenericRecord) obj;
                    String userName = mGr.get("userName").toString();
                    String userPwd = mGr.get("userPwd").toString();
                    // 6.进行相应的业务逻辑处理
                    System.out.println("Members:" + ",userName:" + userName + mGr + ",userPwd:" + userPwd);
                    String retMsg;
                    if (userName.equalsIgnoreCase("rita") && userPwd.equals("123456")) {
                        retMsg = "哈哈，恭喜你,成功登录。";
                        System.out.println(retMsg);
                    } else {
                        retMsg = "登录失败。";
                        System.out.println(retMsg);
                    }
                    // 7.获取返回值类型
                    retGr = new GenericData.Record(protocol.getMessages().get("login").getResponse());
                    // 8.构造回复消息
                    retGr.put("msg", retMsg);
                }
                System.err.println("DEAL SUCCESS!");
                return retGr;
            }
        };
        return gr;
    }
    
    
    /**
     * Java工具生成协议代码方式：java -jar  E:\avro\avro-tools-1.7.7.jar compile protocol E:\avro\Members.avpr E:\avro
     * 功能和动态调用方式一致
     * @throws InterruptedException 
     */
    public void MemberNettyRPCToolsBuilderServer() throws InterruptedException{
        //1.构造接口和实现类的映射相应对象，MemberIFaceImpl该类为具体的业务实现类
        SpecificResponder responder=new SpecificResponder(MemberIFace.class, new MemberIFaceImpl());
        //2.Netty启动RPC服务
        Server server=new NettyServer(responder, new InetSocketAddress(60090));
        server.start();
        System.out.println("Tools Builder PRC Start Complete.");
        server.join();
    }
    
    public void test(){
//    	try {
//			this.MemberInfoDynSer();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	try {
			this.MemberNettyRPCDynBuilderServer();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args){
    	MemberServerProvider mp = new MemberServerProvider();
    	mp.test();
    }
	
}
