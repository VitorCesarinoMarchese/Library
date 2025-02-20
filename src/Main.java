import Models.Request.BookRequest;

import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
        BookRequest book = new BookRequest("No One Mourns The Wicked", "20", 5, 0, 5);
        bd.insertBook(book, 1);
        bd.printBooks();
        bd.closeDB();
    }
}