import ui.MainFrame;
import ui.Updater;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Анастасия on 02.05.2017.
 */
public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/department";
    private static final String URLFIXED = URL + "?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
                    "&useLegacyDatetimeCode=false&serverTimezone=UTC;";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) throws SQLException {
        createDatabaseConnection();
        Updater updater = new Updater();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            updater.setFrame(frame);
        });
        new Thread(updater).run();
    }

    private static void createDatabaseConnection() throws SQLException {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(driver);
        Connection connection = DriverManager.getConnection(URLFIXED, USER, PASSWORD);
        if (!connection.isClosed()) {
            System.out.println("Connection to database is established.");
        }
        connection.close();
        if (connection.isClosed()) {
            System.out.println("Connection to database was closed.");
        }
    }
}
