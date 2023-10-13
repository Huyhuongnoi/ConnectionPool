package dao.Implement;

import dao.DataSource;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements dao.UserDAO<User> {
    private final String TABLE_NAME = "user";
    private final String USERNAME = "username";
    private final String FULL_NAME = "fullName";
    private final String AGE = "age";
    private final String SEX = "sex";
    private final String ADDRESS = "address";
    private static final String CHECK_EXISTENCE = "SELECT COUNT(*) AS username FROM card WHERE username = ? LIMIT 1";
    private static final String INSERT_USER = "INSERT INTO %s(%s, %s, %s, %s, %s)VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?";
    private static final String DELETE_USER = "DELETE FROM %s WHERE %s = ?";
    private static final String SELECT_USER = "SELECT * FROM %s";
    private static final String SELECT_BY_ID = "SELECT * FROM %s WHERE %s = ?";

    public static UserDAOImpl getInstance() {
        return new UserDAOImpl();
    }

    @Override
    public void insert(User user) {
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = INSERT_USER.formatted(TABLE_NAME, USERNAME, FULL_NAME, AGE, SEX, ADDRESS);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getFullName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getSex());
            preparedStatement.setString(5, user.getAddress());
            int result = preparedStatement.executeUpdate();
            if (result != 0) {
                System.out.println("added successfully!");
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing");
                }
            }
        }
    }

    @Override
    public void update(User user) {
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = UPDATE_USER.formatted(TABLE_NAME, FULL_NAME, AGE, SEX, ADDRESS, USERNAME);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getSex());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getUsername());
            int result = preparedStatement.executeUpdate();
            if (result != 0) {
                System.out.println("update successfully!");
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing");
                }
            }
        }
    }

    @Override
    public void delete(String id) {
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = DELETE_USER.formatted(TABLE_NAME, USERNAME);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            int result = preparedStatement.executeUpdate();
            if (result != 0) {
                System.out.println("delete successfully!");
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing");
                }
            }
        }
    }

    @Override
    public ArrayList<User> selectAll() {
        Connection connection = null;
        ArrayList<User> userArrayList = new ArrayList<User>();
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = SELECT_USER.formatted(TABLE_NAME);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString(this.USERNAME);
                String fullName = resultSet.getString(this.FULL_NAME);
                int age = resultSet.getInt(this.AGE);
                String sex = resultSet.getString(this.SEX);
                String address = resultSet.getString(this.ADDRESS);
                User user = new User(username, fullName, age, sex, address);
                userArrayList.add(user);
            }
            if (userArrayList != null) {
                System.out.println("select successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing");
                }
            }
        }
        return userArrayList;
    }

    @Override
    public User selectById(String id) {
        Connection connection = null;
        User user = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = SELECT_BY_ID.formatted(TABLE_NAME, USERNAME);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString(this.USERNAME);
                String fullName = resultSet.getString(this.FULL_NAME);
                int age = resultSet.getInt(this.AGE);
                String sex = resultSet.getString(this.SEX);
                String address = resultSet.getString(this.ADDRESS);
                user = new User(username, fullName, age, sex, address);

            }
            if (user != null) {
                System.out.println("select by id successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing");
                }
            }
        }
        return user;
    }

    @Override
    public boolean CheckExistence(String id) {
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHECK_EXISTENCE);
            statement.setString(1, USERNAME);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("username");
                if (count != 0) {
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing");
                }
            }
        }
        return false;
    }
}