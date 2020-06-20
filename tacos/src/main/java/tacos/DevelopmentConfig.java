package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.Ingredient.Type;

import java.util.Arrays;

@Configuration
@Profile("!prod")
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo, UserRepository userRepo,
                                        TacoRepository tacoRepo, PasswordEncoder encoder) {
        return args -> {
            Ingredient flourTortilla = saveAnIngredient(repo, "FLTO", "Flour Tortilla", Type.WRAP);
            Ingredient cornTortilla = saveAnIngredient(repo,"COTO", "Corn Tortilla", Type.WRAP);
            Ingredient groundBeef = saveAnIngredient(repo,"GRBF", "Ground Beef", Type.PROTEIN);
            Ingredient carnitas = saveAnIngredient(repo,"CARN", "Carnitas", Type.PROTEIN);
            Ingredient tomatoes = saveAnIngredient(repo,"TMTO", "Diced Tomatoes", Type.VEGGIES);
            Ingredient lettuce = saveAnIngredient(repo,"LETC", "Lettuce", Type.VEGGIES);
            Ingredient cheddar = saveAnIngredient(repo,"CHED", "Cheddar", Type.CHEESE);
            Ingredient jack = saveAnIngredient(repo,"JACK", "Monterrey Jack", Type.CHEESE);
            Ingredient salsa = saveAnIngredient(repo,"SLSA", "Salsa", Type.SAUCE);
            Ingredient sourCream = saveAnIngredient(repo,"SRCR", "Sour Cream", Type.SAUCE);

//        UserUDT u = new UserUDT(username, fullname, phoneNumber)

            userRepo.save(new User(null, "sergey71109", encoder.encode("TestPassword"), "Sergey Kozhin",
                    "Some street", "Novgorod", "NO", "173009", "9116442505"))
                    .subscribe();

            Taco taco1 = new Taco();
            taco1.setName("Carnivore");
            taco1.setIngredients(Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar));
            tacoRepo.save(taco1).subscribe();

            Taco taco2 = new Taco();
            taco2.setName("Bovine Bounty");
            taco2.setIngredients(Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream));
            tacoRepo.save(taco2).subscribe();

            Taco taco3 = new Taco();
            taco3.setName("Veg-Out");
            taco3.setIngredients(Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
            tacoRepo.save(taco3).subscribe();

        };
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    public CommandLineRunner fetchIngredients(WebClient webClient) {
        return args -> {
            Flux<Ingredient> ingredients = webClient
                    .get()
                    .uri("/ingredientsx")
                    .retrieve().bodyToFlux(Ingredient.class);

            ingredients.subscribe(System.out::println);
        };
    }

    @Bean
    public CommandLineRunner fetchRecent(WebClient webClient) {
        return args -> {
            Flux<Taco> tacos = webClient
                    .get()
                    .uri("/design/recent")
                    .retrieve()
                    .bodyToFlux(Taco.class);

            tacos.subscribe(System.out::println);
        };
    }

    private Ingredient saveAnIngredient(IngredientRepository repo, String id, String name, Type type) {
        Ingredient ingredient = new Ingredient(id, name, type);
        repo.save(ingredient).subscribe();
        return ingredient;
    }
}
