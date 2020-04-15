package tacos.web.api;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import tacos.Ingredient;
import tacos.Ingredient.Type;

@Relation(value = "ingredient", collectionRelation = "ingredients")
public class IngredientModel extends RepresentationModel<IngredientModel> {

    @Getter
    private final String name;

    @Getter
    private final Type type;

    public IngredientModel(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
