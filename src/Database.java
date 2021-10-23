package passwordsecurity2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

//https://stackoverflow.com/questions/30651830/use-jdbc-mysql-connector-in-intellij-idea
public class Database {
    private String Login;
    private String MotDePasse;
    private boolean Logged = false;

    private final Connection connection;

    final static class MyResult {
        private final boolean first;
        private final String second;

        public MyResult(boolean first, String second) {
            this.first = first;
            this.second = second;
        }

        public boolean getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }
    }

    public Database() throws ClassNotFoundException, SQLException {
        // using JDBC driver
        String driverName = "com.mysql.jdbc.Driver";
        Class.forName(driverName);

        // localhost databaza s nazvnom zadanie4
        String url = "jdbc:mysql://localhost:3306/zadanie4";

        // pouzivatel do vasej MySQL databazy
        String username = "root";
        // heslo k pouzivatelovi
        String password = "root";
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Database connection established");
    }

    public void closeBdd() throws SQLException {
        connection.close();
    }

    public MyResult find(String parameter) {
        try {
            // najdi meno heslo a salt podla mena
            PreparedStatement preparedStatement = connection.prepareStatement("select name, password, salt from users where name=?");
            preparedStatement.setString(1, parameter);

            ResultSet rs = preparedStatement.executeQuery();

            //ak existuje zaznam vrat true a poskladany string
            if (rs.next()) {
                String result = rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getString(3);
                System.out.println(result);
                return new MyResult(true, result);

            //ak neextije vrat false a chybovu hlasku
            }else{
                return new MyResult(true, "Meno sa nenaslo");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new MyResult(false, "Chyba hladania");
        }
    }

    public boolean exist(String parameter) {
        return find(parameter).getFirst();
    }

    public MyResult addUser(String name, String password, String salt) {
        try {
            // skonroluj ci nahodou uz neexistuje s danym menom
            if (exist(name)) {
                return new MyResult(false, "Meno uz existuje");
            }

            // pridaj
            String query = "insert into users (name, password, salt) values (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, salt);

            preparedStatement.execute();
            System.out.println("uspesne registrovany");
            return new MyResult(true, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new MyResult(false, "Chyba pridavania");
        }

    }

}