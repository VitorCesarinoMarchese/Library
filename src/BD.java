import Models.Book;
import Models.Request.BookRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
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
    public void HardCode() throws SQLException {
//        statement.executeUpdate("INSERT INTO users(name, email, password, adm) VALUES(" + "'Tept'"  + "," + "'tept@gmail.com'" + "," + "'tept1234'" + ","+ 1 + ")");
//        ResultSet rs = statement.executeQuery("SELECT * FROM users");
//        while (rs.next()){
//            System.out.println(rs.getString("id"));
//            System.out.println(rs.getString("name"));
//            System.out.println(rs.getString("email"));
//            System.out.println(rs.getString("password"));
//            System.out.println(rs.getString("adm"));
//        }
    }
    public void printBooks() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println("Book ID: " + resultSet.getInt("id"));
                System.out.println("Book NAME: " + resultSet.getString("name"));
                System.out.println("Book PRICE: " + resultSet.getString("price"));
                System.out.println("Book TOTAL: " + resultSet.getInt("total_quantity"));
                System.out.println("Book RENT: " + resultSet.getInt("rent_quantity"));
                System.out.println("Book AVAILABLE: " + resultSet.getInt("available_quantity"));
                System.out.println("---------------------------------------");
            }
        }catch (SQLException e){
            System.err.println("Error printing database: " + e.getMessage());
        }
    }

    public void printLastChanges() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM last_changes");
            while (resultSet.next()) {
                System.out.println("Last Change ID: " + resultSet.getInt("id"));
                System.out.println("Book ID: " + resultSet.getString("book_id"));
                System.out.println("User ID: " + resultSet.getString("user_id"));
                System.out.println("Last Change Type: " + resultSet.getString("type").toUpperCase());
                System.out.println("Last Change DATE: " + resultSet.getString("date"));
                System.out.println("---------------------------------------");
            }
        }catch (SQLException e){
            System.err.println("Error printing database: " + e.getMessage());
        }
    }

    public List<Book> readBooks() {
        List<Book> books = new ArrayList<>();
        try {
            if (statement != null) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
                while (resultSet.next()) {
                    books.add(new Book(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("price"), resultSet.getInt("total_quantity"), resultSet.getInt("rent_quantity"), resultSet.getInt("available_quantity"), resultSet.getInt("last_changes_id"), resultSet.getString("creation_date")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading database: " + e.getMessage());
        }
        return books;
    }

    public void insertBook(BookRequest book, int user_id) {
        String insertBookQuery = "INSERT INTO books(name, price, total_quantity, rent_quantity, available_quantity) " +
                "VALUES(?, ?, ?, ?, ?)";
        String selectBookQuery = "SELECT * FROM books WHERE name = ?";
        String insertLastChangesQuery = "INSERT INTO last_changes(book_id, user_id, type) VALUES(?, ?, ?)";
        String selectLastChangeQuery = "SELECT id FROM last_changes WHERE book_id = ? AND user_id = ?";
        String updateBookQuery = "UPDATE books SET last_changes_id = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
             PreparedStatement insertBookStmt = connection.prepareStatement(insertBookQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement selectBookStmt = connection.prepareStatement(selectBookQuery);
             PreparedStatement insertLastChangeStmt = connection.prepareStatement(insertLastChangesQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement selectLastChangeStmt = connection.prepareStatement(selectLastChangeQuery);
             PreparedStatement updateBookStmt = connection.prepareStatement(updateBookQuery)) {

            connection.setAutoCommit(false);

            insertBookStmt.setString(1, book.Name);
            insertBookStmt.setString(2, book.Price);
            insertBookStmt.setInt(3, book.TotalQuantity);
            insertBookStmt.setInt(4, book.RentQuantity);
            insertBookStmt.setInt(5, book.AvailableQuantity);
            insertBookStmt.executeUpdate();

            ResultSet bookKeys = insertBookStmt.getGeneratedKeys();
            if (!bookKeys.next()) {
                throw new SQLException("Error inserting book: no ID generated.");
            }
            int bookId = bookKeys.getInt(1);

            insertLastChangeStmt.setInt(1, bookId);
            insertLastChangeStmt.setInt(2, user_id);
            insertLastChangeStmt.setString(3, "insert");
            insertLastChangeStmt.executeUpdate();

            ResultSet lastChangeKeys = insertLastChangeStmt.getGeneratedKeys();
            if (!lastChangeKeys.next()) {
                throw new SQLException("Error inserting last alteration: no ID generated.");
            }
            int lastChangeId = lastChangeKeys.getInt(1);

            updateBookStmt.setInt(1, lastChangeId);
            updateBookStmt.setInt(2, bookId);
            updateBookStmt.executeUpdate();

            connection.commit();

            System.out.println("Book inserted with success: " + book.Name);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting the book: " + e.getMessage());
        }
    }

    public void deleteBook(int id, int user) {
        String getBookName = "SELECT name FROM books WHERE id= ?";
        String deleteBook = "DElETE FROM books WHERE id= ?";
        String insertLastChange = "INSERT INTO last_changes(book_id, user_id, type) VALUES(?, ?, ?)";


        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db")){
            String bookName = null;

            connection.setAutoCommit(false);

            try(PreparedStatement getBookNamesStmt = connection.prepareStatement(getBookName)) {
                getBookNamesStmt.setInt(1, id);
                ResultSet rs = getBookNamesStmt.executeQuery();
                if(rs.next()){
                    bookName = rs.getString("name");
                }else{
                    throw new SQLException("No book find with ID: " + id);
                }
            }

            try(PreparedStatement insertLastChangeStmt = connection.prepareStatement(insertLastChange)) {
                insertLastChangeStmt.setInt(1, id);
                insertLastChangeStmt.setInt(2, user);
                insertLastChangeStmt.setString(3, "delete");
                insertLastChangeStmt.executeUpdate();
            }
            try (PreparedStatement deleteBookStmt = connection.prepareStatement(deleteBook)) {
                deleteBookStmt.setInt(1, id);
                int affectedRows = deleteBookStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Error deleting book: no rows affected.");
                }
            }
            connection.commit();
            System.out.println("Book deleted with name: " + bookName);
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