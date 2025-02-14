import java.sql.SQLException;
public class Main {

    public static void main(String[] args) throws SQLException {
        BD bd = new BD();
        bd.printDB();
        bd.closeDB();
    }
}