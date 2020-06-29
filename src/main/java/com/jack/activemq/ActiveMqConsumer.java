package com.jack.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ActiveMqConsumer {

    public static final String ACTIVEMQ_URL="tcp://192.168.0.104:61616";
    public static final String QUEUE_NAME="TEST_QUEUE1";

    public static void main(String[] args) {
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        try {
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    new Thread(() -> {
                        if(null!=message && message instanceof TextMessage){
                            TextMessage textMessage=(TextMessage) message;
                            try {
                                System.out.println("消费者接收到消息"+textMessage.getText());
                                TimeUnit.SECONDS.sleep(3);
                                System.out.println("当前线程:"+Thread.currentThread().getName()+", 处理消息完毕");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, UUID.randomUUID()+"").start();

                }
            });
            System.in.read();

            consumer.close();
            session.close();
            connection.close();

            System.out.println("-----消息发布完成-----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
