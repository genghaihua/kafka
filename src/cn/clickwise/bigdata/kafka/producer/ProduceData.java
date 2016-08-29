package cn.clickwise.bigdata.kafka.producer;

import java.util.List;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import redis.clients.jedis.Jedis;
/**
 * 读取redis数据到kafka中
 * @author genghaihua
 *
 * Create on 2016年8月19日 下午5:39:18
 *
 */
public class ProduceData {
	private static final String redisIp = "183.136.168.74";
//	private static final String redisIp = "192.168.10.74";
	private static final int redisPoint = 6388;
	private  void runPro(String[] args) {
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
		Jedis jedis=new Jedis(redisIp, redisPoint);
		Properties props = new Properties();
		props.put("metadata.broker.list", brokerListStr);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);
		List<String> resList=jedis.lrange("cklist", 0, -1);
		if(resList.size()<=0){
			System.err.println("redis has no more data");
			System.exit(1);
		}
		int size=resList.size();
		System.err.println(size);
		for(int i=0;i<size;i++){
//			System.err.println(resList.get(i));
//			KeyedMessage<String, String> data = new KeyedMessage<>("page_visits", "a", resList.get(i));
			KeyedMessage<String, String> data = new KeyedMessage<>("nstat", "a", resList.get(i));
			producer.send(data);
		}
		producer.close();
		jedis.close();
	}


	public static void main(String[] args) {
		ProduceData produceData=new ProduceData();
		produceData.runPro(args);
	}
}
