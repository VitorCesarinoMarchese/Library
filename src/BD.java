import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BD {
    private Connection connection;
    private Statement statement;

    public BD() {
        connectDB(); // Connect to DB when object is created
    }

    private void connectDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:base.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
    public void printDB() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println("Book ID: " + resultSet.getInt("id"));
                System.out.println("Book NAME: " + resultSet.getString("name"));
                System.out.println("Book PRICE: " + resultSet.getString("price"));
            }
        }catch (SQLException e){
            System.err.println("Error printing database: " + e.getMessage());
        }
    }

    public List<Book> readDB() {
        List<Book> books = new ArrayList<>();
        try {
            if (statement != null) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
                while (resultSet.next()) {
                    books.add(new Book(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("price")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading database: " + e.getMessage());
        }
        return books;
    }
    public void insertBook(String name, String price) {
        try {
            List<Book> rs = readDB();
            int id = rs.size() + 1;
            statement.executeUpdate("INSERT INTO books VALUES(" + id + ", '" + name + "', '" + price + "')");
        }catch (SQLException e){
            System.err.println("Error inserting book: " + e.getMessage());
        }
    }

    public boolean deleteBook(String name) {
        try {
            statement.executeUpdate("DELETE FROM books WHERE name=" + name);
            return true;
        }catch (SQLException e){
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }

    public void closeDB() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

}