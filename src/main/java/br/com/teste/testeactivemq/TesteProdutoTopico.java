package br.com.teste.testeactivemq;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;

import br.com.teste.testeactivemq.modelo.Pedido;
import br.com.teste.testeactivemq.modelo.PedidoFactory;

public class TesteProdutoTopico {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection("user", "senha");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("loja");
        MessageProducer producer = session.createProducer(fila);
        Pedido pedido = new PedidoFactory().geraPedidoComValores();

        StringWriter writer = new StringWriter();
        JAXB.marshal(pedido, writer);
        String xml = writer.toString();

        Message message = session.createTextMessage(xml);
        producer.send(message, DeliveryMode.PERSISTENT,3,5000);
        session.close();
        connection.close();
        context.close();
    }
}
