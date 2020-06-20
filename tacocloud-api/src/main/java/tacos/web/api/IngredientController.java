package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

import java.net.URI;

@RestController
@RequestMapping(path = "/ingredientsx",
        produces = "application/json")
@CrossOrigin("*")
public class IngredientController {
    private final IngredientRepository ingredientRepo;

    @Autowired
    public IngredientController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @GetMapping
    public Flux<Ingredient> allIngredients() {
        return ingredientRepo.findAll();
    }

    @GetMapping(path = "/{id}")
    public Mono<Ingredient> getIngredientById(@PathVariable("id") String ingredientId) {
        return ingredientRepo.findById(ingredientId);
    }

    @PutMapping("/{id}")
    public void updateIngredient(@PathVariable String id, @RequestBody Ingredient ingredient) {
        if (!ingredient.getId().equals(id)) {
            throw new IllegalStateException("Given ingredient's ID doesn't match the ID in the path.");
        }
        ingredientRepo.save(ingredient).subscribe();
    }

    @PostMapping
    public Mono<ResponseEntity<Ingredient>> createIngredient(@RequestBody Mono<Ingredient> ingredient) {
        return ingredient
                .flatMap(ingredientRepo::save)
                .map(i -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(URI.create("http://localhost:8080/ingredientsx/" + i.getId()));
                    return new ResponseEntity<Ingredient>(i, headers, HttpStatus.CREATED);
                });
    }

    @DeleteMapping(path = "/{id}")
    public void deleteIngredient(@PathVariable String id) {
        ingredientRepo.deleteById(id);
    }
}
