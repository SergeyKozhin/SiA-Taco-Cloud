package tacos.web.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Ingredient;
import tacos.IngredientUDT;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DesignTacoControllerTest {

    @Test
    public void shouldReturnRecentTacos() {
        Taco[] tacos = new Taco[16];
        for (int i = 0; i < 16; i++) {
            tacos[i] = testTaco(i + 1L);
        }
        Flux<Taco> tacoFlux = Flux.just(tacos);

        TacoRepository tacoRepository = Mockito.mock(TacoRepository.class);
        when(tacoRepository.findAll()).thenReturn(tacoFlux);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoController(tacoRepository)).build();

        testClient.get().uri("/design/recent")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Taco.class)
                .hasSize(12)
                .contains(Arrays.copyOf(tacos, 12));
    }

    public void shouldSaveTaco() {
        TacoRepository tacoRepository = Mockito.mock(TacoRepository.class);
        Mono<Taco> unsavedTacoMono = Mono.just(testTaco(null));
        Taco savedTaco = testTaco(null);
        savedTaco.setId(UUID.randomUUID());
        Mono<Taco> savedTacoMono = Mono.just(savedTaco);

        when(tacoRepository.save(any())).thenReturn(savedTacoMono);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoController(tacoRepository)).build();

        testClient.post()
                .uri("/design")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Taco.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Taco.class).isEqualTo(savedTaco);
    }

    private Taco testTaco(Long number) {
        Taco taco = new Taco();
        if (number != null) {
            taco.setId(UUID.randomUUID());
        }
        taco.setName("Taco " + number);
        List<IngredientUDT> ingredients = new ArrayList<>();
        ingredients.add(new IngredientUDT("Ingredient A", Ingredient.Type.WRAP));
        ingredients.add(new IngredientUDT("Ingredient B", Ingredient.Type.PROTEIN));
        taco.setIngredients(ingredients);
        return taco;
    }

}
