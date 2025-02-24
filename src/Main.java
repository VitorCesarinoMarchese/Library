import Models.Request.BookRequest;

import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
        bd.printBooks();
        bd.printLastChanges();
        bd.closeDB();
    }
}