package tacos.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tacos.Order;

@Service
public class KafkaOrderMessagingService implements OrderMessagingService {

    private final KafkaTemplate<String, Order> kafka;

    @Autowired
    public KafkaOrderMessagingService(KafkaTemplate<String, Order> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void sendOrder(Order order) {
        kafka.send("tacocloud.orders.topic", order);
    }

}
