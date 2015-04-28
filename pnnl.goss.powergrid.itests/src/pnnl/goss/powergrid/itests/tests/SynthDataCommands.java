//package pnnl.goss.powergrid.itests.tests;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import org.apache.http.auth.UsernamePasswordCredentials;
//
//import pnnl.goss.core.Client;
//import pnnl.goss.core.Client.PROTOCOL;
//import pnnl.goss.core.GossResponseEvent;
//import pnnl.goss.core.client.ClientServiceFactory;
//
//public class SynthDataCommands {
//
//	ClientServiceFactory factory;
//	Client client;
//
//	public SynthDataCommands() {
//		factory = new ClientServiceFactory();
//		factory.setOpenwireUri("tcp://localhost:61616");
//		client = factory.create(PROTOCOL.STOMP);
//		client.setCredentials(new UsernamePasswordCredentials("system",
//				"manager"));
//	}
//
//	public static void main(String[] args) {
//		SynthDataCommands command = new SynthDataCommands();
//
//		Thread thread1 = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				GossResponseEvent event = new GossResponseEvent() {
//					@Override
//					public void onMessage(Serializable message) {
//						System.out.println(message);
//						String reply = "12";
//						command.client.publish(
//								"synthdata/poorva/newmodel/result",
//								(Serializable) reply);
//					}
//				};
//
//				command.client.subscribeTo("/topic/synthdata/request/newmodel",
//						event);
//
//			}
//		});
//		thread1.start();
//
//		Thread thread2 = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				GossResponseEvent event1 = new GossResponseEvent() {
//					@Override
//					public void onMessage(Serializable message) {
//						System.out.println(message);
//						String reply = SynthDataCommands
//								.readFile(
//										"C:/Users/shar064/MyProjects/GOSS/SyntheticData/SyntheticDataWebClient/full_gp_north_118.xml",
//										StandardCharsets.UTF_8);
//						// String reply = "<id>test</id>";
//						System.out.println(reply);
//						command.client.publishString(
//								"synthdata/poorva/gendata/result",
//								(String) reply);
//					}
//				};
//
//				command.client.subscribeTo("/topic/synthdata/poorva/gendata",
//						event1);
//
//			}
//		});
//		thread2.start();
//
//	}
//
//	static String readFile(String path, Charset encoding) {
//		String reply = null;
//		try {
//			byte[] encoded = Files.readAllBytes(Paths.get(path));
//			reply = new String(encoded, encoding);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return reply;
//
//	}
//
//}
