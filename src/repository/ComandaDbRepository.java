package repository;

import domain.Comanda;

import domain.Tort;
import exceptions.RepositoryException;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ComandaDbRepository extends MemoryRepository<Comanda> {

    IRepository<Tort> repoTort;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private String dbLocation = "jdbc:sqlite:";

    public ComandaDbRepository(String dbLocation){
        this.dbLocation = dbLocation;
        openConnection();
        createSchema();
        //initTables();
        loadData();

    }

    private ArrayList<Tort> convertToList(String line) {
        String[] tokens = line.split(";");
        ArrayList<Tort> lista = new ArrayList<>();
        int i = 0;
        for(String line1:tokens)
        {
            String[] tok = line1.split(",");
            Tort t = new Tort(Integer.parseInt(tok[0]), tok[1]);
            lista.add(t);
        }
        return lista;
    }

    String toStringArray(ArrayList<Tort> object)
    {
        String elem = new String();
        for(Tort t: object)
            elem = elem+t.getID()+","+t.getTip_tort()+";";
        return elem;
    }

    private void loadData() {
        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * from comanda"); ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    ArrayList<Tort> t= convertToList(rs.getString("lista"));
                    Comanda c = new Comanda(rs.getInt("id"), t, rs.getString("data"));
                    this.data.add(c);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Connection connection = null;

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(dbLocation);
            if (connection == null || connection.isClosed())
                connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createSchema() {
        try {
            try (final Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS comanda(id int, data varchar(20), torturi varchar(200));");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    /*public void initTables(){
        ArrayList<Tort> torturi = new ArrayList<>();
        torturi.add(new Tort(1, "ciocolata"));
        torturi.add(new Tort(2, "vanilie"));
        torturi.add(new Tort(3, "capsuni"));

        List<Comanda> comenzi = new ArrayList<>();
        comenzi.add(new Comanda(1, torturi,"12.12.2023"));
        try(PreparedStatement stmt = connection.prepareStatement("INSERT INTO Comenzi values (?,?,?);")){
            for(Comanda c: comenzi){
                stmt.setInt(1,c.getID());

                String torturiString = c.getTorturi()
                        .stream()
                        .map(Tort::getTip_tort) // Assuming Tort has a getName() method for the flavor
                        .collect(Collectors.joining(","));
                stmt.setString(2, torturiString);
                stmt.setString(3, c.getData());
                stmt.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }*/

    @Override
    public void add(Comanda comanda) {
        try {
            super.add(comanda);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        try {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO comanda VALUES (?, ?, ?)")) {
                statement.setInt(1, comanda.getID());
                String torturiString = comanda.getTorturi()
                        .stream()
                        .map(Tort::getTip_tort) // Assuming Tort has a getName() method for the flavor
                        .collect(Collectors.joining(","));


                statement.setString(2, torturiString);
                LocalDate date = LocalDate.parse(comanda.getData(), formatter);
                statement.setString(3, formatter.format(date));
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
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM comanda WHERE id=?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Tort> fetchTorturiForComanda(int comandaId) throws SQLException {
        List<Tort> torturiForComanda = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Torturi WHERE comandaId = ?")) {
            stmt.setInt(1, comandaId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Tort tort = new Tort(resultSet.getInt("idTort"));
                    tort = repoTort.getById(tort.getID());
                    torturiForComanda.add(tort);
                }
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }
        return torturiForComanda;
    }

    @Override
    public ArrayList<Comanda> getAll() {
        ArrayList<Comanda> comenzi = new ArrayList<>();
        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * from comanda"); ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    int comandaId = rs.getInt("id");
                    ArrayList<Tort> torturi = (ArrayList<Tort>) fetchTorturiForComanda(comandaId);
                    LocalDate date = LocalDate.parse(rs.getString("date"), formatter);
                    String dateString = date.format(formatter);
                    Comanda c = new Comanda(comandaId,
                            torturi,
                            dateString);
                    comenzi.add(c);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return comenzi;
    }

    @Override
    public void updateEntity(Comanda comanda) {
        try {
            super.updateEntity(comanda);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement updateComanda = connection.prepareStatement("UPDATE comanda SET id = ?, torturi = ?, data = ? WHERE id = ?")){
                updateComanda.setInt(1, comanda.getID());

                String torturiString = comanda.getTorturi()
                        .stream()
                        .map(Tort::getTip_tort) // Assuming Tort has a getName() method for the flavor
                        .collect(Collectors.joining(","));

                updateComanda.setString(2, torturiString);
                LocalDate date = LocalDate.parse(comanda.getData(), formatter);
                updateComanda.setString(3, formatter.format(date));
                updateComanda.executeUpdate();
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
