package liguozhong.bigdata.mq.kafka;

import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import liguozhong.bigdata.util.DateUtils;

/**
 * 用户下单时，往kafka发布消息
 * 
 * @author 李国忠<br>
 * @version 创建时间：2015年2月24日 下午7:13:54
 */
public class OrderProducer {
	/**
	 * 该方法模拟11。11疯狂下单0.1秒下一个单
	 */
	public void begin11_11() {
		Producer<Integer, String> producer = getProducer();
		Random random = new Random();
		while (true) {
			String money = random.nextInt(10) + "";// 订单金额
			String date = DateUtils.date2String(null, DateUtils.YMDHMS);// 下单日期
			String provinceId = random.nextInt(34) + "";// 省份id
			createOrder(money, date, provinceId, producer);
			try {
				Thread.sleep(1 * 100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 用户下单时，往kafka发布消息,消息内容以tab键隔开<br>
	 * 中国有34个省级行政区 其中，有23个省、5个自治区、4个直辖市、2个特别行政区.
	 * 
	 * @param money
	 *            订单金额
	 * @param date
	 *            日期
	 * @param provinceId
	 *            用户的省份id
	 * @param producer
	 *            kafka消息队列发布消息的对象
	 */
	private void createOrder(String money, String date, String provinceId,
			Producer<Integer, String> producer) {
		// 订单的消息格式：金额 日期 省份id
		String orderInfo = money + "\t" + date + "\t" + provinceId;
		producer.send(new KeyedMessage<Integer, String>(
				KafkaProperties.ORDER_TOPIC, orderInfo));
	}

	private Producer<Integer, String> getProducer() {
		Properties props = new Properties();
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list", KafkaProperties.BROKER_LIST);
		return new Producer<Integer, String>(new ProducerConfig(props));
	}
}
