package jdbc;


import lombok.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;

    private final DataSource dataSource = CustomDataSource.getInstance();


    private static final String CREATE_USER_SQL = "insert into myusers (firstname, lastname, age) values (?, ?, ?);";

    @SuppressWarnings("unused")
    @SneakyThrows
    public Long createUser(User user) {
        try (var currentCorrection = dataSource.getConnection()) {
            var creationStatement = currentCorrection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            creationStatement.setString(1, user.getFirstName());
            creationStatement.setString(2, user.getLastName());
            creationStatement.setInt(3, user.getAge());
            creationStatement.executeUpdate();

            var keys = creationStatement.getGeneratedKeys();
            keys.next();
            return keys.getLong(1);
        }
    }

    private static final String UPDATE_USER_SQL = "update myusers set firstname = ?, lastname = ?, age = ? where id = ?";

    @SuppressWarnings("unused")
    @SneakyThrows
    public User updateUser(User user) {
        try (var currentConnection = dataSource.getConnection()) {
            var updateRandomStatement = currentConnection.prepareStatement(UPDATE_USER_SQL);
            updateRandomStatement.setString(1, user.getFirstName());
            updateRandomStatement.setString(2, user.getLastName());
            updateRandomStatement.setInt(3, user.getAge());
            updateRandomStatement.setLong(4, user.getId());
            updateRandomStatement.executeUpdate();
        }

        return user;
    }

    private static final String FIND_USER_BY_ID_SQL = "select id, firstname, lastname, age from myusers where id = ?";

    @SuppressWarnings("unused")
    @SneakyThrows
    public User findUserById(Long userId) {
        try (var currentConnection = dataSource.getConnection()) {
            var findingStatement = currentConnection.prepareStatement(FIND_USER_BY_ID_SQL);
            findingStatement.setLong(1, userId);
            var result = findingStatement.executeQuery();
            result.next();
            return User.fromResultSet(result);
        }
    }

    private static final String FIND_USER_BY_NAME_SQL = "select id, firstname, lastname, age from myusers where firstname = ? limit 1";

    @SuppressWarnings("unused")
    @SneakyThrows
    public User findUserByName(String userName) {
        try (var currentConnection = dataSource.getConnection()) {
            var findingStatement = currentConnection.prepareStatement(FIND_USER_BY_NAME_SQL);
            findingStatement.setString(1, userName);
            var result = findingStatement.executeQuery();
            result.next();
            return User.fromResultSet(result);
        }

    }

    private static final String FIND_ALL_USER_SQL = "select id, firstname, lastname, age from myusers";

    @SuppressWarnings("unused")
    @SneakyThrows
    public List<User> findAllUser() {
        try (var currentConnection = dataSource.getConnection()) {
            var findingStatement = currentConnection.prepareStatement(FIND_ALL_USER_SQL);
            ResultSet result = findingStatement.executeQuery();
            var foundUsers = new ArrayList<User>();
            while (result.next()) {
                foundUsers.add(User.fromResultSet(result));
            }

            return foundUsers;
        }
    }

    private static final String DELETE_USER = "delete from myusers where id = ?";

    @SuppressWarnings("unused")
    @SneakyThrows
    public void deleteUser(Long userId) {
        try (var currentConnection = dataSource.getConnection();
             var deletionStatement = currentConnection.prepareStatement(DELETE_USER)) {
            deletionStatement.setLong(1, userId);
            deletionStatement.executeUpdate();
        }
    }
}
