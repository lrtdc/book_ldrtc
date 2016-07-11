package org.light.avro;

import java.io.File;
import java.io.IOException;
//import java.net.InetSocketAddress;
//
//import org.apache.avro.AvroRemoteException;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
//import org.apache.avro.ipc.NettyServer;
//import org.apache.avro.ipc.NettyTransceiver;
//import org.apache.avro.ipc.Server;
//import org.apache.avro.ipc.specific.SpecificRequestor;
//import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
//import org.apache.avro.util.Utf8;

import example.avro.User;
//import example.proto.Mail;
//import example.proto.Message;

public class TestAvro {
	
	public void addUserCompile(){
		User user1 = new User();
		user1.setName("王light");
		user1.setFavoriteNumber(66);
		user1.setFavoriteColor("浅蓝色");
		
		// Alternate constructor
		User user2 = new User("魏Sunny", 88, "red");
		
		// Construct via builder
		User user3 = User.newBuilder()
		             .setName("王Sam")
		             .setFavoriteColor("blue")
		             .setFavoriteNumber(2011)
		             .build();
		
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		try {
			dataFileWriter.create(user1.getSchema(), new File("/Users/a/Desktop/tmp/users.avro"));
			dataFileWriter.append(user1);
			dataFileWriter.append(user2);
			dataFileWriter.append(user3);
			dataFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deserUserCompile(){
		// Deserialize Users from disk
		DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
		DataFileReader<User> dataFileReader = null;
		User user = null;
		try {
			dataFileReader = new DataFileReader<User>(
											new File("/Users/a/Desktop/tmp/users.avro"),
											userDatumReader);
			while (dataFileReader.hasNext()) {
				// Reuse user object by passing it to next(). This saves us from
				// allocating and garbage collecting many objects for files with
				// many items.
				user = dataFileReader.next(user);
				System.out.println(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUserDynamic(){
		Schema schema = null;
		try {
			schema = new Schema.Parser().parse(new File("/Users/a/Desktop/tmp/user.avsc"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GenericRecord user1 = new GenericData.Record(schema);
		user1.put("name", "王成光");
		user1.put("favorite_number", 2012);
		user1.put("favorite_color", "blue");

		GenericRecord user2 = new GenericData.Record(schema);
		user2.put("name", "王占平");
		user2.put("favorite_number", 1998);
		user2.put("favorite_color", "green");
		
		// Serialize user1 and user2 to disk
		File file = new File("/Users/a/Desktop/tmp/userDyn.avro");
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
		try {
			dataFileWriter.create(schema, file);
			dataFileWriter.append(user1);
			dataFileWriter.append(user2);
			dataFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deserUserDynamic(){
		Schema schema = null;
		try {
				schema = new Schema.Parser().parse(new File("/Users/a/Desktop/tmp/user.avsc"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Deserialize users from disk
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		File file = new File("/Users/a/Desktop/tmp/userDyn.avro");
		DataFileReader<GenericRecord> dataFileReader = null;
		GenericRecord user = null;
		try {
			dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
			while (dataFileReader.hasNext()) {
				// Reuse user object by passing it to next(). This saves us from
				// allocating and garbage collecting many objects for files with
				// many items.
				user = dataFileReader.next(user);
				System.out.println(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static class MailImpl implements Mail {
//        // in this simple example just return details of the message
//        public Utf8 send(Message message) {
//            System.out.println("Sending message");
//            return new Utf8("Sending message to " + message.getTo().toString()
//                    + " from " +message.getFrom().toString()
//                    + " with body " +message.getBody().toString());
//        }
//    }
//
//    private static Server server;
//
//    private static void startServer() throws IOException {
//        server = new NettyServer(new SpecificResponder(Mail.class,new MailImpl()),
//        						 new InetSocketAddress(65111));
//        // the server implements the Mail protocol (MailImpl)
//    }
//    
//    public void testRpc(){
//    	System.out.println("Starting server");
//        // usually this would be anotherapp, but for simplicity
//        try {
//			startServer();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        System.out.println("Server started");
//        NettyTransceiver client = null;
//		try {
//			client = new NettyTransceiver(new InetSocketAddress(65111));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        // client code - attach to the server and send a message
//
//        Mail proxy = null;
//		try {
//			proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        System.out.println("Client built, got proxy");
//
//        // fill in the Message record and send it
//
//        Message message = new Message();
//        message.setTo(new Utf8("127.0.0.1"));
//        message.setFrom(new Utf8("127.0.0.1"));
//        message.setBody(new Utf8("this is my message"));
//        System.out.println("Calling proxy.send with message: " + message.toString());
//        try {
//			System.out.println("Result: " +proxy.send(message));
//		} catch (AvroRemoteException e) {
//			e.printStackTrace();
//		}
//
//        // cleanup
//        client.close();
//        server.close();
//    }
	
	public void test(){
//		this.addUserCompile();
		this.deserUserCompile();
		System.out.println("=========以上是静态类方式序列化，以下是动态加载方式序列化==========");
//		this.addUserDynamic();
		this.deserUserDynamic();
		
//		this.testRpc();
	}

	public static void main(String[] args) {
		TestAvro ta = new TestAvro();
		ta.test();
	}

}
