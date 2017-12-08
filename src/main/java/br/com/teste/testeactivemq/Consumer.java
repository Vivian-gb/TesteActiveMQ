package br.com.teste.testeactivemq;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer
                implements
                    Runnable,
                    ExceptionListener {
    @SuppressWarnings("resource")
    public void run() {
        try {

            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                            Producer.brokerURL);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false,
                            Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(Producer.DEMO_QUEUE);

            // Create a MessageConsumer from the Session to the Topic or
            // Queue
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(new MessageListener(){

                @Override
                public void onMessage(Message message){
                    TextMessage textMessage  = (TextMessage)message;
                    try{
                        System.out.println(textMessage.getText());
                    } catch(JMSException e){
                        e.printStackTrace();
                    }    
                }

            });
            new Scanner(System.in).nextLine();
            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

    public static void main(String[] args) throws Exception {
        Consumer consumer = new Consumer();
 
        Thread threadConsumer = new Thread(consumer);
        threadConsumer.start();
 
    }
}
