import Models.Book;
import Models.Request.BookRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BD {
    private Connection connection;
    private Statement statement;

    public BD() {
        connectDB();
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
    public void closeDB() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

    public void printDB() {
        try {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println("Models.Book ID: " + resultSet.getInt("id"));
                System.out.println("Models.Book NAME: " + resultSet.getString("name"));
                System.out.println("Models.Book PRICE: " + resultSet.getString("price"));
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
    public void insertBook(BookRequest book) {
        try {
            List<Book> rs = readDB();
            int id = rs.get(rs.size() - 1).Id + 1;
            statement.executeUpdate("INSERT INTO books VALUES(" + id + "," + "'" + book.Name + "'" + "," + "'" + book.Price + "'" + ")");
            System.out.println("Book inserted with name: " + book.Name);
        }catch (SQLException e){
            System.err.println("Error inserting book: " + e.getMessage());
        }
    }
    public void deleteBook(int id) {
        try {
            statement.executeUpdate("DElETE FROM books WHERE id= '" + id + "'");
            ResultSet name = statement.executeQuery("SELECT name FROM books WHERE id= '" + id + "'");
            System.out.println("Book deleted with name: " + name.getString("name"));
        }catch (SQLException e){
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }
    public void updateBook(int id, BookRequest book) {
        try {
            ResultSet oldName = statement.executeQuery("SELECT name FROM books WHERE id= '" + id + "'");
            statement.executeUpdate( "UPDATE books SET name='" + book.Name + "', price='" + book.Price + "' WHERE id= '" + id + "'");
            System.out.println("Book updated with name: " + book.Name);
        }catch (SQLException e){
            System.err.println("Error updating book: " + e.getMessage());
        }
    }

}