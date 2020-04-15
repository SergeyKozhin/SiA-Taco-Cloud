package tacos.web.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import tacos.Ingredient;

public class IngredientModelAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientModel> {
    public IngredientModelAssembler() {
        super(IngredientController.class, IngredientModel.class);
    }

    @Override
    protected IngredientModel instantiateModel(Ingredient entity) {
        return new IngredientModel(entity);
    }

    @Override
    public IngredientModel toModel(Ingredient entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
