package tacos.restclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tacos.Ingredient;
import tacos.Taco;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
public class TacoCloudClient {
    private final RestTemplate rest;
    private final Traverson traverson;

    @Autowired
    public TacoCloudClient(RestTemplate rest, Traverson traverson) {
        this.rest = rest;
        this.traverson = traverson;
    }

    public Ingredient getIngredientById(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity =
                rest.getForEntity(
                        "http://localhost:8080/ingredientsx/{id}",
                        Ingredient.class, ingredientId);

        log.info("Fetched time: " + responseEntity.getHeaders().getDate());

        return responseEntity.getBody();
    }

    public List<Ingredient> getAllIngredients() {
        return rest.exchange("http://localhost:8080/ingredientsx", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Ingredient>>() {
                }).getBody();
    }

    public void updateIngredient(Ingredient ingredient) {
        rest.put("http://localhost:8080/ingredientsx/{id}",
                ingredient, ingredient.getId());
    }

    public void deleteIngredient(Ingredient ingredient) {
        rest.delete("http://localhost:8080/ingredientsx/{id}",
                ingredient.getId());
    }

    public URI createIngredientUrl(Ingredient ingredient) {
        return rest.postForLocation(
                "http://localhost:8080/ingredientsx/",
                ingredient);
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity =
                rest.postForEntity(
                        "http://localhost:8080/ingredientsx",
                        ingredient, Ingredient.class);

        log.info("New resource created at: " + responseEntity.getHeaders().getLocation());

        return responseEntity.getBody();
    }

    public Iterable<Ingredient> getAllIngredientsWithTraverson() {
        ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType =
                new ParameterizedTypeReference<CollectionModel<Ingredient>>() {
                };

        CollectionModel<Ingredient> ingredientsModel =
                traverson
                        .follow("ingredients")
                        .toObject(ingredientType);

        assert ingredientsModel != null;
        return ingredientsModel.getContent();
    }

    public Iterable<Taco> getRecentTacosWithTraverson() {
        ParameterizedTypeReference<CollectionModel<Taco>> tacoType =
                new ParameterizedTypeReference<CollectionModel<Taco>>() {
                };

        CollectionModel<Taco> tacosModel =
                traverson
                        .follow("tacos")
                        .follow("recents")
                        .toObject(tacoType);

        assert tacosModel != null;
        return tacosModel.getContent();
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        String ingredientsUrl = traverson
                .follow("ingredients")
                .asLink()
                .getHref();

        return rest.postForObject(ingredientsUrl, ingredient, Ingredient.class);
    }
}
