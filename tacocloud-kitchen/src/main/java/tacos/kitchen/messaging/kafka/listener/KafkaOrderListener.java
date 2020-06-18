package tacos.kitchen.messaging.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import tacos.Order;
import tacos.kitchen.KitchenUI;

@Component
@Slf4j
public class KafkaOrderListener {

    private final KitchenUI ui;

    @Autowired
    public KafkaOrderListener(KitchenUI ui) {
        this.ui = ui;
    }

    @KafkaListener(topics = "tacocloud.orders.topic", groupId = "test-consumer-group")
    public void handle(Order order, Message<Order> message) {
        MessageHeaders headers = message.getHeaders();
        log.info("Received from partition {} with timestamp {}",
                headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
                headers.get(KafkaHeaders.RECEIVED_TIMESTAMP));

        ui.displayOrder(order);
    }
}
