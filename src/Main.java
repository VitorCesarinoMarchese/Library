import Models.Request.BookRequest;

import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
        bd.printDB();
        System.out.println("==========================");
        BookRequest book = new BookRequest("The Low End Theory 2", "$10.00");
        bd.insertBook(book);
        System.out.println("==========================");
        bd.printDB();
        bd.closeDB();
    }
}