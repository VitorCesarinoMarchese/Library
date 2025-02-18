import Models.Request.BookRequest;

import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
        bd.printDB();
        System.out.println("==========================");
        System.out.println("==========================");
        bd.updateBook(4, new BookRequest("The Updated Book", "$10.00"));
        System.out.println("==========================");
        bd.printDB();
        bd.closeDB();
    }
}