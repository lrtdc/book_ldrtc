package org.light.avro;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;

public class RpcClient {
	
	 /**
     * 采用Netty方式建立和服务端的连接
     * 
     * @throws IOException
     */
    public void testClient() throws IOException {
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

	public static void main(String[] args) {
		RpcClient rce = new RpcClient();
		try {
			rce.testClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
