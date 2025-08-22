package com.example.repository;


import com.example.domain.Piesa;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLPiesaRepository extends Repository<Piesa> implements AutoCloseable {

    // TODO Locatia bazei de date se poate citi din fisierul de setari

    // NOTE Clasa aceasta nu mai foloseste lista de artisti, ci ii incarca direct
    // din baza de date
    private String JDBC_URL = "jdbc:sqlite:";


    // src/seminar/group323/seminar2/artists.db
    private Connection conn = null;

    public SQLPiesaRepository(String databaseFile) {
        JDBC_URL += databaseFile;
        openConnection();
        initDatabase();
    }

    private void initDatabase() {
        try {
            try (final Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS piese(id int PRIMARY KEY, formatie varchar(50), titlu varchar(50), gen varchar(50), durata varchar(50));");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    @Override
    public void add(Piesa piese) throws RepositoryException {
        super.add(piese);

        // Daca se executa codul de mai jos, inseamna ca nu a avut loc nici o exceptie
        // in super.add(artist);
        try (var statement = conn.prepareStatement("INSERT INTO piese VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, piese.getId());
            statement.setString(2, piese.getFormatie());
            statement.setString(3, piese.getTitlu());
            statement.setString(4, piese.getGen());
            statement.setString(5, piese.getDurata());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la salvarea piesei: " + e.getMessage());
        }
    }


    public List<Piesa> filtrupiese(String query) throws RepositoryException {
            List<Piesa> filtrupiese = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM piese WHERE formatie LIKE ? OR titlu LIKE ? OR gen LIKE ? OR durata LIKE ?")) {
                String search = "%" + query + "%";
                stmt.setString(1, search);
                stmt.setString(2, search);
                stmt.setString(3, search);
                stmt.setString(4, search);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        filtrupiese.add(new Piesa(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                    }
                }
            } catch (SQLException e) {
                throw new RepositoryException("Eroare la filtrare"+ e.getMessage());
            }
            if(filtrupiese.isEmpty()){

                throw new RepositoryException("Filtrare goala");
            }
            else{
            return filtrupiese;}
        }

    @Override
    public Piesa findById(int id) throws RepositoryException {
        try (var statement = conn.prepareStatement("SELECT * FROM piese WHERE id = (?)")) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var piese_id = resultSet.getInt(1);
                var piese_formatie = resultSet.getString(2);
                var piese_titlu = resultSet.getString(3);
                var piese_gen = resultSet.getString(4);
                var piese_durata = resultSet.getString(5);
                return new Piesa(piese_id,piese_formatie,piese_titlu,piese_gen,piese_durata);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la stergerea piesei: " + e.getMessage());
        }
        throw new RepositoryException("Element with id not found=" + id);
    }

    @Override
    public void remove(int id) throws RepositoryException {
        // TODO de implementat in clasa de baza
        super.remove(id);

        try (var statement = conn.prepareStatement("DELETE FROM piese WHERE id = (?)")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            // NOTE
//            e.printStackTrace();
            throw new RepositoryException("Eroare la stergerea piesei: " + e.getMessage());
        }
    }


    @Override
    public Collection<Piesa> getAll() {
        var pieseList = new ArrayList<Piesa>();
        try (var select = conn.prepareStatement("SELECT * FROM piese")) {

            ResultSet resultSet = select.executeQuery();
            while (resultSet.next() == true) {
                var piesa_id = resultSet.getInt(1);
                var piesa_formatie = resultSet.getString(2);
                var piesa_titlu = resultSet.getString(3);
                var piesa_gen = resultSet.getString(2);
                var piesa_durata = resultSet.getString(3);
                pieseList.add(new Piesa(piesa_id, piesa_formatie, piesa_titlu, piesa_gen, piesa_durata));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pieseList;
    }


    private void openConnection() {
        try {
            // with DataSource
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            // Exceptii de tipul RuntimeException se pot arunca fara a fi nevoie de
            // try .. catch (unchacked exceptions)
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() throws Exception {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            // TODO Probabil de scris in loguri
            e.printStackTrace();
        }
    }
}
