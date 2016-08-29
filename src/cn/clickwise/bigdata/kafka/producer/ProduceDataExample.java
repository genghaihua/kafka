package cn.clickwise.bigdata.kafka.producer;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

//import kafka.admin.TopicCommand;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class ProduceDataExample {
	private static void runPro(String[] args) {
		if (args.length <=0) {
			System.err.println("Usage:<brokerip1> <brokerip2> <brokerip3>......");
			return;
		}
		String brokerListStr = "";
		for (int i = 0; i < args.length; i++) {
			if (brokerListStr.length() <= 0) {
				brokerListStr = (args[i] + ":9092");
			} else {
				brokerListStr += ("," + args[i] + ":9092");
			}
		}
//		long events = Long.parseLong(args[0]);
		long events=200000;
		Random rnd = new Random();

		Properties props = new Properties();
		props.put("metadata.broker.list", brokerListStr);
//		props.put("metadata.broker.list", "broker1:9092,broker2:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// props.put("partitioner.class", "example.producer.SimplePartitioner");
		props.put("request.required.acks", "1");

		ProducerConfig config = new ProducerConfig(props);

		Producer<String, String> producer = new Producer<String, String>(config);

		for (long nEvents = 0; nEvents < events; nEvents++) {
			long runtime = new Date().getTime();
			String ip = "192.168.2." + rnd.nextInt(255);
			String msg = runtime + ",www.example.com," + ip;
			KeyedMessage<String, String> data = new KeyedMessage<String, String>("page_visits", ip, msg);
			producer.send(data);
		}
		producer.close();
	}


	public static void main(String[] args) {
		runPro(args);
	}
}
