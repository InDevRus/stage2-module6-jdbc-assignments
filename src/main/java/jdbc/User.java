package jdbc;

import lombok.*;

import java.sql.ResultSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;

    @SneakyThrows
    static User fromResultSet(ResultSet resultSet) {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getInt("age")
        );
    }
}
