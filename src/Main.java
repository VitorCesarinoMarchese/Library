import Models.Request.BookRequest;

import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
//        BookRequest book = new BookRequest("The Tetris Effect", "10", 10, 0, 10);
//        bd.insertBook(book, 1);
//        bd.deleteBook(2, 1);
        bd.printBooks();
        bd.printLastChanges();
        bd.closeDB();
    }
}