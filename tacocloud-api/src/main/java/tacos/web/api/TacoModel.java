package tacos.web.api;

import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import tacos.Taco;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Relation(value = "taco", collectionRelation = "tacos")
public class TacoModel extends RepresentationModel<TacoModel> {

    @Getter
    private final String name;

    @Getter
    private final Date createdAt;

    @Getter
    private final List<IngredientModel> ingredients;

    public TacoModel(Taco taco) {
        this.name = taco.getName();
        this.createdAt = taco.getCreatedAt();
        IngredientModelAssembler assembler = new IngredientModelAssembler();
        this.ingredients = taco.getIngredients().stream().map(assembler::toModel).collect(Collectors.toList());
    }
}