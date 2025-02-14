import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
        bd.insertBook("The Simphony Of The Night", "$10.50");
        bd.printDB();
        bd.closeDB();
    }
}