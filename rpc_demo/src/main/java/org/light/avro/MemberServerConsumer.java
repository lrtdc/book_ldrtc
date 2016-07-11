package org.light.avro;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.specific.SpecificDatumReader;

import com.ifree.serrpc.builder.MemberIFace;
import com.ifree.serrpc.builder.Members;
import com.ifree.serrpc.builder.Retmsg;

/**
 * 服务消费者 该类测试了通过工具和动态序列化及反序列化两种方式，同时测试了通过工具生成代码及动态调用RPC服务两种方式
 * 
 * @author ifree
 *
 */
public class MemberServerConsumer {

    /**
     * 动态反序列：通过Schema文件进行动态反序列化操作
     * 
     * @throws IOException
     */
    public void MemberInfoDynDeser() throws IOException {
        // 1.schema文件解析
        Parser parser = new Parser();
        Schema mSchema = parser.parse(this.getClass().getResourceAsStream("/Members.avsc"));

        // 2.构建数据读对象
        DatumReader<GenericRecord> mGr = new SpecificDatumReader<GenericRecord>(mSchema);
        DataFileReader<GenericRecord> mDfr = new DataFileReader<GenericRecord>(new File("/Users/a/Desktop/tmp/members.avro"), mGr);
        // 3.从序列化文件中进行数据反序列化取出数据
        GenericRecord gr = null;
        while (mDfr.hasNext()) {
            gr = mDfr.next();
            System.err.println("deser data:" + gr.toString());
        }
        mDfr.close();
        System.out.println("Dyn Builder Ser Start Complete.");
    }

    /**
     * 通过Java工具来生成必要的类，进行反序列化操作
     * 
     * @throws IOException
     */
    public void MemberInfoToolsDeser() throws IOException {
        // 1.构建反序列化读取对象
        DatumReader<Members> mDr = new SpecificDatumReader<Members>(Members.class);
        DataFileReader<Members> mDfr = new DataFileReader<Members>(new File("E:/avro/members.avro"), mDr);
        Members m = null;
        // 2.循环读取文件数据
        while (mDfr.hasNext()) {
            m = mDfr.next();
            System.err.println("tools deser data :" + m);
        }
        // 3.关闭读取对象
        mDfr.close();
        System.out.println("Tools Builder Ser Start Complete.");
    }

    /**
     * 采用HTTP方式建立和服务端的连接
     * 
     * @throws IOException
     */
    public void MemberHttpRPCDynBuilderClient() throws IOException {
        // 1.建立和服务端的http通讯
        Transceiver transceiver = new HttpTransceiver(new URL("http://127.0.0.1:60090"));
        bussinessDeal(transceiver);
    }

    /**
     * 采用Netty方式建立和服务端的连接
     * 
     * @throws IOException
     */
    public void MemberNettyRPCDynBuilderClient() throws IOException {
        // 1.建立和服务端的Netty通讯
        Transceiver transceiver = new NettyTransceiver(new InetSocketAddress("127.0.0.1", 60090));
        // 2.进行必要的业务处理
        bussinessDeal(transceiver);
    }

    /**
     * 进行必要的业务处理
     * 
     * @param transceiver
     * @throws IOException
     */
    private void bussinessDeal(Transceiver transceiver) throws IOException {
        // 2.获取协议
        Protocol protocol = Protocol.parse(this.getClass().getResourceAsStream("/Members.avpr"));
        // 3.根据协议和通讯构造请求对象
        GenericRequestor requestor = new GenericRequestor(protocol, transceiver);
        // 4.根据schema获取messages主节点内容
        GenericRecord loginGr = new GenericData.Record(protocol.getMessages().get("login").getRequest());
        // 5.在根据协议里面获取request中的schema
        GenericRecord mGr = new GenericData.Record(protocol.getType("Members"));
        // 6.设置request中的请求数据
        mGr.put("userName", "rita");
        mGr.put("userPwd", "123456");
        // 7、把二级内容加入到一级message的主节点中
        loginGr.put("m", mGr);
        // 8.设置完毕后，请求方法，正式发送访问请求信息，并得到响应内容
        Object retObj = requestor.request("login", loginGr);
        // 9.进行解析操作
        GenericRecord upGr = (GenericRecord) retObj;
        System.out.println(upGr.get("msg"));
    }

    /**
     * Java工具生成协议代码方式：java -jar E:\avro\avro-tools-1.7.7.jar compile protocol
     * E:\avro\Members.avpr E:\avro 功能和动态调用方式一致
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public void MemberNettyRPCToolsBuilderClient() throws InterruptedException, IOException {
        // 1.和服务端建立通讯
        Transceiver transceiver = new NettyTransceiver(new InetSocketAddress("192.168.1.116", 60090));
        // 2.获取客户端对象
        MemberIFace memberIFace = SpecificRequestor.getClient(MemberIFace.class, transceiver);
        // 3.进行数据设置
        Members members = new Members();
        members.setUserName("rita");
        members.setUserPwd("123456");
        // 开始调用登录方法
        Retmsg retmsg = memberIFace.login(members);
        System.out.println("Recive Msg:" + retmsg.getMsg());
    }
    
    public void test(){
//    	try {
//			this.MemberInfoDynDeser();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    
		try {
			this.MemberNettyRPCDynBuilderClient();
		} catch (IOException e) {
			e.printStackTrace();
		};
		
    }
    
    public static void main(String[] args){
    	MemberServerConsumer mp = new MemberServerConsumer();
    	mp.test();
    }
}

