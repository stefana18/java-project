package jdbc;

import domain.Tort;
import exceptions.RepositoryException;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JDBC {

    private static final String JDBC_URL =
            "jdbc:sqlite:Tort.db";

    private Connection conn = null;

    private void openConnection() {
        try {
            // with DriverManager
//            if (conn == null || conn.isClosed())
//                conn = DriverManager.getConnection(JDBC_URL);

            // with DataSource
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createSchema() {
        try {
            try (final Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tort(id int, tip_tort varchar(400));");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

   public void initTables() {
       try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO tort values (?,?);")) {
           for (int i = 1; i <= 100; i++) {
               Tort tort = generateRandomTort(i);
               stmt.setInt(1, tort.getID());
               stmt.setString(2, tort.getTip_tort());
               stmt.executeUpdate();
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }

    public void add(Tort tort) throws RepositoryException {
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO tort VALUES (?, ?)")) {
                statement.setInt(1, tort.getID());
                statement.setString(2, tort.getTip_tort());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(int id) {

        try {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM tort WHERE id=?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Tort> getAll() {
        ArrayList<Tort> torturi = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * from tort;")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tort t = new Tort(rs.getInt(1), rs.getString(2));
                torturi.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return torturi;
    }

    public void updateEntity(Tort tort) {

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement updateTort = conn.prepareStatement("UPDATE tort SET tip_tort = ? WHERE id = ?")) {
                updateTort.setString(2, tort.getTip_tort());
                updateTort.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Tort generateRandomTort(int id) {
        String[] tipuriTort = {"Chocolate", "Vanilla", "Strawberry", "Lemon", "Caramel", "Black Forest", "Cheesecake"};
        int index = new Random().nextInt(tipuriTort.length);
        String tipTort = tipuriTort[index];
        return new Tort(id, tipTort);
    }

    public static void main(String[] args) throws RepositoryException {
        JDBC db_example = new JDBC();
        db_example.openConnection();
        db_example.createSchema();
        db_example.initTables();

        //Tort tort = new Tort(5,"cioco");
        //db_example.add(tort);

       /* Random random = new Random();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String[] tipuri = new String[]{"tarta", "tort", "prajitura", "ecler", "placinta"
                , "strudel" , "cozonac" , "pandispan" , "budinca" , "baclava" , "croissant" , "briosa" , "clatita" , "cremsnit" ,
                "foietaj" , "crema" , "vafe", "soufle"};
        String[] arome = new String[]{"ciocolata", "vanilie", "zmeura", "cirese", "caramel"
                , "afine", "capsuni", "visine", "mere", "pere", "caise", "nuca",
                "rom", "lamaie", "coacaze", "cocos", "migdale", "prune", "struguri", "banane"};

        int id=db_example.getAll().size()+1;
        int idTort = db_example.getAll().size();
        for(int i=1;i<=100;i++) {

            ArrayList<Tort> torturi = new ArrayList<>();

            int numberOfTorturi = 1 + random.nextInt(5);

            for (int j = 0; j < numberOfTorturi; j++) {
                int i1 = random.nextInt(tipuri.length);
                int i2 = random.nextInt(arome.length);
                String tip = tipuri[i1] + "_" + arome[i2];
                torturi.add(new Tort(j+1, tip));
            }

            long ms = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            String data = dateFormat.format(new Date(ms));
            Comanda comanda = new Comanda(id,torturi,data);
            //db_example.add(comand);
            id++;

        }

        db_example.remove(1);
        db_example.remove(2);
        db_example.remove(3);
        db_example.remove(5);



        ArrayList<Tort> torturi = db_example.getAll();
        for(Tort tort1 : torturi)
            System.out.println(tort1);
*/

        db_example.closeConnection();

    }
}
