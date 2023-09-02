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
    private static final String CREATE_USER_SQL = "insert into myusers (id, firstname, lastname, age) values (?, ?, ?, ?);";
    private static final String UPDATE_USER_SQL = "update myusers user set firstname = ?, lastname = ?, age = ? where id = ?";
    private static final String DELETE_USER = "delete from myusers where id = ?";
    private static final String FIND_USER_BY_ID_SQL = "select id, firstname, lastname, age from myusers where id = ?";
    private static final String FIND_USER_BY_NAME_SQL = "select id, firstname, lastname, age from myusers where firstname = ? limit 1";
    private static final String FIND_ALL_USER_SQL = "select id, firstname, lastname, age from myusers";

    @SneakyThrows
    public Long createUser(User user) {
        throw new UnsupportedOperationException();
//        try (var creationStatement = dataSource.getConnection().prepareStatement(CREATE_USER_SQL)) {
//            creationStatement.setLong(1, user.getId());
//            creationStatement.setString(2, user.getFirstName());
//            creationStatement.setString(3, user.getLastName());
//            creationStatement.setInt(4, user.getAge());
//            creationStatement.executeUpdate();
//        }
//
//        return user.getId();
    }

    @SneakyThrows
    public User updateUser(User user) {
        throw new UnsupportedOperationException();
//        try (var updateRandomStatement = dataSource.getConnection().prepareStatement(UPDATE_USER_SQL)) {
//            updateRandomStatement.setString(1, user.getFirstName());
//            updateRandomStatement.setString(2, user.getLastName());
//            updateRandomStatement.setInt(3, user.getAge());
//            updateRandomStatement.setLong(4, user.getId());
//            updateRandomStatement.executeUpdate();
//        }
//
//        return user;
    }

    @SneakyThrows
    public User findUserById(Long userId) {
        throw new UnsupportedOperationException();
//        ResultSet result;
//        try (var findingStatement = dataSource.getConnection().prepareStatement(FIND_USER_BY_NAME_SQL)) {
//            findingStatement.setLong(1, userId);
//            result = findingStatement.executeQuery();
//        }
//        result.next();
//        return User.fromResultSet(result);
    }

    @SneakyThrows
    public User findUserByName(String userName) {
        throw new UnsupportedOperationException();
//        ResultSet result;
//        try (var findingStatement = dataSource.getConnection().prepareStatement(FIND_USER_BY_ID_SQL)) {
//            findingStatement.setString(1, userName);
//            result = findingStatement.executeQuery();
//        }
//        result.next();
//        return User.fromResultSet(result);
    }

    @SneakyThrows
    public List<User> findAllUser() {
        throw new UnsupportedOperationException();
//        ResultSet result;
//        try (var findingStatement = dataSource.getConnection().prepareStatement(FIND_ALL_USER_SQL)) {
//            result = findingStatement.executeQuery();
//        }
//        var foundUsers = new ArrayList<User>();
//        while (result.next()) {
//            foundUsers.add(User.fromResultSet(result));
//        }
//
//        return foundUsers;
    }

    @SneakyThrows
    public void deleteUser(Long userId) {
        try (var connection = dataSource.getConnection();
             var deletionStatement = connection.prepareStatement(DELETE_USER)) {
            deletionStatement.setLong(1, userId);
            deletionStatement.executeUpdate();
        }
    }
}
