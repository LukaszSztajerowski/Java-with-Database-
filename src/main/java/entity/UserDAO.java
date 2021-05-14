package entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;


public class UserDAO {

    public static final String CREATE_USER_QUERY = "private static final String CREATE_USER_QUERY =\n" +
            "\"INSERT INTO users(username, email, password) VALUES (?, ?, ?)\";\n";

    public static final String READ_ONE_ROW = "Select * from user where id = ?";
    public static final String UPDATE_ROW = "Update user set email = ?, username = ?, password = ? where id = ?";
    public static final String DELETE = "delete from user where id = ?";
    public static final String FIND_ALL = "select * from user";

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read (int userId){
        try(Connection conn = DBUtil.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(READ_ONE_ROW);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            User user = new User();
            if (resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(hashPassword(resultSet.getString("password")));
                return user;
            }else return user;

        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user){
        try (Connection conn = DBUtil.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ROW);
//            if(user.getId()!=0){
            Scanner scanner = new Scanner(System.in);
            System.out.println("podaj nowe imie: ");
            String newName = scanner.nextLine();
            System.out.println("podaj nowy adres email: ");
            String newEmail = scanner.nextLine();
            System.out.println("podaj nowe hasło: ");
            String newPassword = scanner.nextLine();

            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, newName);
            preparedStatement.setString(3, hashPassword(newPassword));
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
//            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int userId){
        try(Connection conn = DBUtil.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE);
            preparedStatement.setInt(1,userId);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public User[] findAll() {

        User[] users = new User[0];
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            users = new User[0];
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users = addToArray(user, users);
            }
            return users;


        } catch (SQLException e) {
            e.printStackTrace();
            return users;
        }
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers;
    }
}


