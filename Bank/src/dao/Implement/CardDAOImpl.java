package dao.Implement;

import dao.CardDAO;
import dao.DataSource;
import model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CardDAOImpl implements CardDAO<Card> {
    private static final String TABLE_NAME = "card";
    private static final String USERNAME = "username";
    private static final String PASS_WORD = "passWord";
    private static final String BALANCE = "balance";
    private static final String INSERT_CARD = "INSERT INTO %s(%s, %s, %s)VALUES (?, ?, ?)";

    private static final String UPDATE_CARD = "UPDATE %s SET %s = ?, %s = ? WHERE %s = ?";

    private static final String DELETE_CARD = "DELETE FROM %s WHERE %s = ?";
    private static final String SELECT_CARD = "SELECT * FROM %s";
    private static final String SELECT_BY_ID = "SELECT * FROM %s WHERE %s = ?";

    public static CardDAOImpl getInstance() {
        return new CardDAOImpl();
    }

    @Override
    public void insert(Card card) {
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = INSERT_CARD.formatted(TABLE_NAME, USERNAME, PASS_WORD, BALANCE);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, card.getUsername());
            preparedStatement.setString(2, card.getPassword());
            preparedStatement.setFloat(3, card.getBalance());
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
    public void update(Card card) {
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = UPDATE_CARD.formatted(TABLE_NAME, PASS_WORD, BALANCE, USERNAME);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, card.getPassword());
            preparedStatement.setFloat(2, card.getBalance());
            preparedStatement.setString(3, card.getUsername());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            if (result != 0) {
                System.out.println("update successfully!");
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
    public void delete(String id) {
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = DELETE_CARD.formatted(TABLE_NAME, USERNAME);
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
    public ArrayList<Card> selectAll() {
        Connection connection = null;
        ArrayList<Card> cardArrayList = new ArrayList<Card>();
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = SELECT_CARD.formatted(TABLE_NAME);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString(CardDAOImpl.USERNAME);
                String password = resultSet.getString(CardDAOImpl.PASS_WORD);
                float balance = resultSet.getFloat(CardDAOImpl.BALANCE);
                Card card = new Card(username, password, balance);
                cardArrayList.add(card);
            }
            if (cardArrayList != null) {
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
        return cardArrayList;
    }

    @Override
    public Card selectById(String id) {
        Connection connection = null;
        Card card = null;
        try {
            connection = DataSource.getInstance().getConnection();
            String sql = SELECT_BY_ID.formatted(TABLE_NAME, USERNAME);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString(CardDAOImpl.USERNAME);
                String password = resultSet.getString(CardDAOImpl.PASS_WORD);
                float balance = resultSet.getFloat(CardDAOImpl.BALANCE);
                card = new Card(username, password, balance);
            }
            if (card != null) {
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
        return card;
    }
}
