package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import tacos.Taco;
import tacos.data.TacoRepository;

@RepositoryRestController
public class RecentTacosController {

    private final TacoRepository tacoRepo;

    @Autowired
    public RecentTacosController(TacoRepository tacoRepo) {
        this.tacoRepo = tacoRepo;
    }

    @GetMapping(path = "/tacos/recent", produces = "application/hal+json")
    public Flux<Taco> recentTacos() {
        return tacoRepo.findByOrderByCreatedAtDesc().take(12);
    }
}
