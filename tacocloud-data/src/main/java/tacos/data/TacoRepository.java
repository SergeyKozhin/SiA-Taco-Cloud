package tacos.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import tacos.Taco;

import java.util.UUID;

public interface TacoRepository extends ReactiveMongoRepository<Taco, String> {

    Flux<Taco> findByOrderByCreatedAtDesc();

}
