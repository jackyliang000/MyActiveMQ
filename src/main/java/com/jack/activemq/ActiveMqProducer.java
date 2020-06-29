package com.jack.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMqProducer {

    public static final String ACTIVEMQ_URL="tcp://192.168.0.104:61616";
    public static final String QUEUE_NAME="TEST_QUEUE1";

    public static void main(String[] args) {
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        try {
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);
            MessageProducer producer = session.createProducer(queue);
            for (int i = 0; i <3; i++) {
                TextMessage textMessage = session.createTextMessage("---第" + i + "条message");
                producer.send(textMessage);
            }
            producer.close();
            session.close();
            connection.close();

            System.out.println("-----消息发布完成-----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
