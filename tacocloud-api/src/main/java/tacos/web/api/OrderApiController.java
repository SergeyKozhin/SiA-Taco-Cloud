package tacos.web.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Order;
import tacos.data.OrderRepository;
import tacos.messaging.OrderMessagingService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/orders",
        produces = "application/json")
@CrossOrigin("*")
public class OrderApiController {

    private final OrderRepository orderRepo;
    private final OrderMessagingService messagingService;

    public OrderApiController(OrderRepository orderRepo, OrderMessagingService messagingService) {
        this.orderRepo = orderRepo;
        this.messagingService = messagingService;
    }

    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> createOrder(@RequestBody Order order) {
        messagingService.sendOrder(order);
        return orderRepo.save(order);
    }

    @PatchMapping(path = "/{orderId}", consumes = "application/json")
    public Mono<Order> patchOrder(@PathVariable("orderId") UUID orderId,
                                  @RequestBody Order patch) {
        return orderRepo.findById(orderId).map(
                order -> {
                    if (patch.getDeliveryName() != null) {
                        order.setDeliveryName(patch.getDeliveryName());
                    }
                    if (patch.getDeliveryStreet() != null) {
                        order.setDeliveryStreet(patch.getDeliveryStreet());
                    }
                    if (patch.getDeliveryCity() != null) {
                        order.setDeliveryCity(patch.getDeliveryCity());
                    }
                    if (patch.getDeliveryState() != null) {
                        order.setDeliveryState(patch.getDeliveryState());
                    }
                    if (patch.getDeliveryZip() != null) {
                        order.setDeliveryZip(patch.getDeliveryState());
                    }
                    if (patch.getCcNumber() != null) {
                        order.setCcNumber(patch.getCcNumber());
                    }
                    if (patch.getCcExpiration() != null) {
                        order.setCcExpiration(patch.getCcExpiration());
                    }
                    if (patch.getCcCVV() != null) {
                        order.setCcCVV(patch.getCcCVV());
                    }
                    return order;
                })
                .flatMap(orderRepo::save);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") UUID orderId) {
        try {
            orderRepo.deleteById(orderId);
        } catch (EmptyResultDataAccessException ignored) {
        }
    }
}
