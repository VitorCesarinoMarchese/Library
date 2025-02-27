import Models.Book;
import Models.Request.BookRequest;

import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
//        BookRequest nBook = new BookRequest("The test", "10", 10, 0, 10);
//        bd.insertBook(nBook, 1);
        bd.HardCode();
        bd.closeDB();
    }
}