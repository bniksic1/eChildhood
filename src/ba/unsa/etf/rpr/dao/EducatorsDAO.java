package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.dto.Educator;
import ba.unsa.etf.rpr.dto.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class EducatorsDAO {
    private static EducatorsDAO instance;
    private Connection conn;
    private PreparedStatement educatorsStatement, educatorStatement, educator2Statement, updateStatement, insertStatement, deleteStatement, exists2Statement;

    private EducatorsDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:eChildhoodDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try { //first prepared statement
            educatorsStatement = conn.prepareStatement("SELECT * FROM educators");
        } catch (SQLException e) { //if base is not created
            regenerateBase();
            try {
                educatorsStatement = conn.prepareStatement("SELECT * FROM educators");
            } catch (SQLException ex) { // in this case we cant do anything (bigger problems with base)
                ex.printStackTrace();
            }
        }

        try {
            educatorStatement = conn.prepareStatement("SELECT * FROM educators WHERE username = ? AND password = ?");
            educator2Statement = conn.prepareStatement("SELECT * FROM educators WHERE username = ?");
            updateStatement = conn.prepareStatement("UPDATE educators SET username = ?, password = ?, name = ?, surname = ?," +
                    "birth = ?, location = ?, address = ?, number = ?, email = ?," +
                    "avatar = ?, admin = ? WHERE username = ?");
            insertStatement = conn.prepareStatement("INSERT INTO educators VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            deleteStatement = conn.prepareStatement("DELETE FROM educators WHERE username = ?");
            exists2Statement = conn.prepareStatement("SELECT * FROM educators WHERE username = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerateBase() {
        Scanner reader = null;
        try {
            reader = new Scanner(new File("eChildhoodDB.db.sql"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String statement = "";
        while(reader.hasNext()){
            statement += reader.nextLine();
            if(statement.charAt(statement.length() - 1) == ';') {
                try {
                    Statement stmt = conn.createStatement();
                    stmt.execute(statement);
                    statement = "";
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        reader.close();
    }

    public static EducatorsDAO getInstance(){
        if(instance == null) instance = new EducatorsDAO();
        return instance;
    }

    private Educator getEducatorFromResultSet(ResultSet rs) throws SQLException, ParseException {
        return new Educator(rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getDate(5).toLocalDate(), rs.getString(6),
                rs.getString(7), rs.getLong(8), rs.getString(9),
                rs.getString(10), rs.getBoolean(11));
    }

    public ArrayList<Educator> getEducators(){
        ArrayList<Educator> educatorArrayList = new ArrayList<>();
        try {
            ResultSet rs = educatorsStatement.executeQuery();
            while(rs.next()) {
                if(rs.getString(1).equals("admin")) continue;
                educatorArrayList.add(getEducatorFromResultSet(rs));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return educatorArrayList;
    }

    public Educator getEducator(String username, String password) throws SQLException, ParseException {
        Educator educator = null;
        educatorStatement.setString(1, username);
        educatorStatement.setString(2, password);
        ResultSet rs = educatorStatement.executeQuery();
        if(rs.next()) educator = getEducatorFromResultSet(rs);
        return educator;
    }

    public Person getEducator(String username) throws SQLException, ParseException {
        Educator educator = null;
        educator2Statement.setString(1, username);
        ResultSet rs = educator2Statement.executeQuery();
        if(rs.next()) educator = getEducatorFromResultSet(rs);
        return educator;
    }

    public void update(Educator c, String key) throws SQLException {
        updateStatement.setString(1, c.getUsername());
        updateStatement.setString(2, c.getPassword());
        updateStatement.setString(3, c.getName());
        updateStatement.setString(4, c.getSurname());
        updateStatement.setDate(5, Date.valueOf(c.getDate()));
        updateStatement.setString(6, c.getLocation());
        updateStatement.setString(7, c.getAddress());
        updateStatement.setLong(8, c.getNumber());
        updateStatement.setString(9, c.getEmail());
        updateStatement.setString(10, c.getAvatar());
        updateStatement.setInt(11, c.isAdmin() ? 1 : 0);
        updateStatement.setString(12, key);
        updateStatement.executeUpdate();
    }

    public Connection getConn(){
        return conn;
    }

    public static void removeInstance() {
        if(instance == null) return;
        instance.close();
        instance = null;
    }

    private void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Educator c) throws SQLException {
        insertStatement.setString(1, c.getUsername());
        insertStatement.setString(2, c.getPassword());
        insertStatement.setString(3, c.getName());
        insertStatement.setString(4, c.getSurname());
        insertStatement.setDate(5, Date.valueOf(c.getDate()));
        insertStatement.setString(6, c.getLocation());
        insertStatement.setString(7, c.getAddress());
        insertStatement.setLong(8, c.getNumber());
        insertStatement.setString(9, c.getEmail());
        insertStatement.setString(10, c.getAvatar());
        insertStatement.setInt(11, c.isAdmin() ? 1 : 0);
        insertStatement.executeUpdate();
    }

    public void delete(String username) throws SQLException {
        deleteStatement.setString(1, username);
        deleteStatement.executeUpdate();
    }

    public Educator exists(String username) throws SQLException, ParseException {
        Educator educator = null;
        exists2Statement.setString(1, username);
        ResultSet rs = exists2Statement.executeQuery();
        if(rs.next()) educator = getEducatorFromResultSet(rs);
        return educator;
    }
}
