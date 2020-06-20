package tacos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@UserDefinedType
public class UserUDT {

    private final String username;
    private final String fullname;
    private final String phoneNumber;

}
