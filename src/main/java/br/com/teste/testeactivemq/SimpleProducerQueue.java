package br.com.teste.testeactivemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SimpleProducerQueue {

    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection("user", "senha");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("fila");
        MessageProducer producer = session.createProducer(fila);
        Message message = session.createTextMessage("<pedido><id>123</id></pedido>");
        producer.send(message);
        session.close();
        connection.close();
        context.close();
    }
}
