package jdbc;


import lombok.*;

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
    private final Connection connection = CustomDataSource.getInstance().getConnection();
    private final PreparedStatement preparedStatement = null;
    private final Statement statement = null;

    private static final String CREATE_USER_SQL = "insert into myusers (id, firstname, lastname, age) values (?, ?, ?, ?);";
    private static final String UPDATE_USER_SQL = "update myusers user set firstname = ?, lastname = ?, age = ? where id = ?";
    private static final String DELETE_USER = "delete from myusers where id = ?";
    private static final String FIND_USER_BY_ID_SQL = "select id, firstname, lastname, age from myusers where id = ?";
    private static final String FIND_USER_BY_NAME_SQL = "select id, firstname, lastname, age from myusers where firstname = ? limit 1";
    private static final String FIND_ALL_USER_SQL = "select id, firstname, lastname, age from myusers";
    private static final String FIND_MAXIMUM_USER_ID = "select max(id) as maxid from myusers";
    private static final String COUNT_USERS = "select count(*) as count from myusers";
    private static final String FIND_USER_BY_OFFSET = "select id, firstname, lastname, age from myusers limit 1 offset ?";

    @SneakyThrows
    public Long createUser(User user) {
        try (var creationStatement = connection.prepareStatement(CREATE_USER_SQL)) {
            creationStatement.setLong(1, user.getId());
            creationStatement.setString(2, user.getFirstName());
            creationStatement.setString(3, user.getLastName());
            creationStatement.setInt(4, user.getAge());
            creationStatement.executeUpdate();
        }

        return user.getId();
    }

    @SneakyThrows
    public User updateUser(User user) {
        try (var updateRandomStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
            updateRandomStatement.setString(1, user.getFirstName());
            updateRandomStatement.setString(2, user.getLastName());
            updateRandomStatement.setInt(3, user.getAge());
            updateRandomStatement.setLong(4, user.getId());
            updateRandomStatement.executeUpdate();
        }

        return user;
    }

    @SneakyThrows
    public User findUserById(Long userId) {
        ResultSet result;
        try (var findingStatement = connection.prepareStatement(FIND_USER_BY_NAME_SQL)) {
            findingStatement.setLong(1, userId);
            result = findingStatement.executeQuery();
        }
        result.next();
        return User.fromResultSet(result);
    }

    @SneakyThrows
    public User findUserByName(String userName) {
        ResultSet result;
        try (var findingStatement = connection.prepareStatement(FIND_USER_BY_ID_SQL)) {
            findingStatement.setString(1, userName);
            result = findingStatement.executeQuery();
        }
        result.next();
        return User.fromResultSet(result);
    }

    @SneakyThrows
    public List<User> findAllUser() {
        ResultSet result;
        try (var findingStatement = connection.prepareStatement(FIND_ALL_USER_SQL)) {
            result = findingStatement.executeQuery();
        }
        var foundUsers = new ArrayList<User>();
        while (result.next()) {
            foundUsers.add(User.fromResultSet(result));
        }

        return foundUsers;
    }

    @SneakyThrows
    private long countUsers() {
        long count;
        try (var countStatement = connection.prepareStatement(COUNT_USERS)) {
            count = countStatement.executeQuery().getLong("count");
        }
        return count;
    }

    @SneakyThrows
    public void deleteUser(Long userId) {
        try (var deletionStatement = connection.prepareStatement(DELETE_USER)) {
            deletionStatement.setLong(1, userId);
            deletionStatement.executeUpdate();
        }
    }
}
