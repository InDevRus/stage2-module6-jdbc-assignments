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
    public Long createUser(User user) {
        try (var preparedStatement = connection.prepareStatement(createUserSQL)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setInt(4, user.getAge());
            preparedStatement.executeUpdate();
        }

        return user.getId();
    }

    @SneakyThrows
    public User updateUser(User user) {
        try (var updateRandomStatement = connection.prepareStatement(updateUserSQL)) {
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
    public void deleteUser(Long userId) {
        try (var statement = connection.prepareStatement(deleteUser)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }
}
