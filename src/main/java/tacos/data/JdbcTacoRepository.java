package tacos.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        taco.getIngredients().forEach(ingredient -> saveIngredientToTaco(ingredient, tacoId));

        return taco;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
                "INSERT INTO Taco (name, createdAt) VALUES (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP
        );
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = preparedStatementCreatorFactory
                .newPreparedStatementCreator(Arrays.asList(
                        taco.getName(),
                        new Timestamp(taco.getCreatedAt().getTime())
                ));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(String ingredientId, long tacoId) {
        jdbc.update(
                "INSERT INTO Taco_Ingredients (taco, ingredient) VALUES (?, ?)",
                tacoId,
                ingredientId
        );
    }
}
