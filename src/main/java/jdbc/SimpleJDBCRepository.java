package jdbc;


import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {
    private Connection connection = CustomDataSource.getInstance().getConnection();
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;

    private static final String createUserSQL = "insert into myusers (id, firstname, lastname, age) values (?, ?, ?, ?);";
    private static final String updateUserSQL = "update myusers user set firstname = ?, lastname = ?, age = ? where id = ?";
    private static final String deleteUser = "delete from myusers where id = ?";
    private static final String findUserByIdSQL = "select id, firstname, lastname, age from myusers where id = ?";
    private static final String findUserByNameSQL = "select id, firstname, lastname, age from myusers where firstname = ? limit 1";
    private static final String findAllUserSQL = "select id, firstname, lastname, age from myusers";
    private static final String findMaximumUserId = "select max(id) as maxid from myusers";
    private static final String countUsers = "select count(*) as count from myusers";
    private static final String findUserByOffset = "select id, firstname, lastname, age from myusers limit 1 offset ?";

    private static User generateRandomUser(long id) {
        var randomFirstName = StringUtils.capitalize(RandomStringUtils.randomAlphabetic(4, 10).toLowerCase());
        var randomLastName = StringUtils.capitalize(RandomStringUtils.randomAlphabetic(5, 10).toLowerCase());
        var randomAge = ThreadLocalRandom.current().nextInt(12, 100);
        return new User(id, randomFirstName, randomLastName, randomAge);
    }

    @SneakyThrows
    public Long createUser() {
        long maxId;
        try (var preparedStatement = connection.prepareStatement(findMaximumUserId)) {
            maxId = preparedStatement.executeQuery().getLong("maxid");
        }

        var newUserId = Math.addExact(maxId, 1L);
        var user = generateRandomUser(newUserId);

        try (var preparedStatement = connection.prepareStatement(createUserSQL)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setInt(4, user.getAge());
            preparedStatement.executeUpdate();
        }

        return newUserId;
    }

    @SneakyThrows
    public User updateUser() {
        var usersCount = countUsers();
        var userNumber = ThreadLocalRandom.current().nextLong(0, usersCount);

        User user;
        try (var randomUserStatement = connection.prepareStatement(findUserByOffset)) {
            randomUserStatement.setLong(1, userNumber);
            var result = randomUserStatement.executeQuery();
            result.next();
            user = User.fromResultSet(result);
        }

        var newUser = generateRandomUser(user.getId());
        try (var updateRandomStatement = connection.prepareStatement(updateUserSQL)) {
            updateRandomStatement.setString(1, newUser.getFirstName());
            updateRandomStatement.setString(2, newUser.getLastName());
            updateRandomStatement.setInt(3, newUser.getAge());
            updateRandomStatement.setLong(4, user.getId());
            updateRandomStatement.executeUpdate();
        }

        return newUser;
    }

    @SneakyThrows
    public User findUserById(Long userId) {
        ResultSet result;
        try (var preparedStatement = connection.prepareStatement(findUserByNameSQL)) {
            preparedStatement.setLong(1, userId);
            result = preparedStatement.executeQuery();
        }
        result.next();
        return User.fromResultSet(result);
    }

    @SneakyThrows
    public User findUserByName(String userName) {
        ResultSet result;
        try (var preparedStatement = connection.prepareStatement(findUserByIdSQL)) {
            preparedStatement.setString(1, userName);
            result = preparedStatement.executeQuery();
        }
        result.next();
        return User.fromResultSet(result);
    }

    @SneakyThrows
    public List<User> findAllUser() {
        ResultSet result;
        try (var preparedStatement = connection.prepareStatement(findAllUserSQL)) {
            result = preparedStatement.executeQuery();
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
        try (var preparedStatement = connection.prepareStatement(countUsers)) {
            count = preparedStatement.executeQuery().getLong("count");
        }
        return count;
    }

    @SneakyThrows
    private void deleteUser(Long userId) {
        try (var statement = connection.prepareStatement(deleteUser)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }
}
