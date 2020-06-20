package tacos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UserDefinedType("ingredient")
public class IngredientUDT {
    private String name;
    private Ingredient.Type type;
}
