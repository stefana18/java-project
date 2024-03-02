package repository;
import domain.Tort;
import exceptions.RepositoryException;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  TortDbRepository extends MemoryRepository<Tort> {


    private String dbLocation = "jdbc:sqlite:Tort.db";
    public TortDbRepository(String dbLocation){
        this.dbLocation = dbLocation;
        openConnection();
        createSchema();
        //initTables();
        loadData();
    }

    private void loadData(){
        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * from tort"); ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Tort t = new Tort(rs.getInt("id"), rs.getString("tip_tort"));
                    this.data.add(t);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private Connection connection = null;


    public void openConnection(){
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(dbLocation);
            if (connection == null || connection.isClosed())
                connection= ds.getConnection();
        } catch (SQLException e) {
            // FIXME not very nice :)
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void closeConnection()
    {
        if (connection != null)
        {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void createSchema()
    { try {
        try (final Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tort(id int, tip_tort varchar(200));");
        }
    } catch (SQLException e) {
        System.err.println("[ERROR] createSchema : " + e.getMessage());
        throw new RuntimeException(e);
    }
    }


    /*public void initTables(){
        List<Tort> torturi = new ArrayList<>();
        torturi.add(new Tort(1, "ciocolata"));
        torturi.add(new Tort(2, "vanilie"));
        torturi.add(new Tort(3, "capsuni"));
        try(PreparedStatement stmt = connection.prepareStatement("INSERT INTO Torturi values (?,?);")){
            for(Tort t: torturi){
                stmt.setInt(1,t.getID());
                stmt.setString(2, t.getTip_tort());
                stmt.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }*/

    @Override
    public void add(Tort tort) throws RepositoryException {
        try {
            super.add(tort);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        try {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO tort VALUES (?, ?)")) {
                statement.setInt(1, tort.getID());
                statement.setString(2, tort.getTip_tort());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        try {
            super.remove(id);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        try {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM tort WHERE id=?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateEntity(Tort tort) {
        try {
            super.updateEntity(tort);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement updateTort = connection.prepareStatement("UPDATE tort SET tip_tort = ? WHERE id = ?")){
                updateTort.setString(2, tort.getTip_tort());
                updateTort.executeUpdate();

                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Tort> getAll(){
        ArrayList<Tort> torturi = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * from tort;")) {
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

}
