package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.dto.Child;
import ba.unsa.etf.rpr.dto.Educator;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChildhoodDAO {
    private static ChildhoodDAO instance;
    private Connection conn;
    private PreparedStatement childhoodStatement, existsStatement, exists2Statement, insertStatement, updateStatement, deleteStatement, delete2Statement;

    private ChildhoodDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:eChildhoodDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try { //first prepared statement
            childhoodStatement = conn.prepareStatement("SELECT * FROM childhood");
        } catch (SQLException e) { //if base is not created
            regenerateBase();
            try {
                childhoodStatement = conn.prepareStatement("SELECT * FROM childhood");
            } catch (SQLException ex) { // in this case we cant do anything (bigger problems with base)
                ex.printStackTrace();
            }
        }

        try {
            existsStatement = conn.prepareStatement("SELECT * FROM childhood WHERE username = ? AND password = ?");
            exists2Statement = conn.prepareStatement("SELECT * FROM childhood WHERE username = ?");
            insertStatement = conn.prepareStatement("INSERT INTO childhood VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            updateStatement = conn.prepareStatement("UPDATE childhood SET username = ?, password = ?, name = ?, surname = ?," +
                    "birth = ?, location = ?, address = ?, parent_name = ?, parent_email = ?," +
                    "parent_number = ?, avatar = ?, educator = ? WHERE username = ?");
            deleteStatement = conn.prepareStatement("DELETE FROM childhood WHERE username = ?");
            delete2Statement = conn.prepareStatement("DELETE FROM childhood WHERE educator = ?");
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

    public static ChildhoodDAO getInstance(){
        if(instance == null) instance = new ChildhoodDAO();
        return instance;
    }

    private Child getChildFromResultSet(ResultSet rs) throws SQLException, ParseException {
        Child child = new Child(rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getDate(5).toLocalDate(), rs.getString(6),
                rs.getString(7), rs.getString(8), rs.getString(9),
                rs.getLong(10), rs.getString(11), null);
        child.setEducator((Educator) EducatorsDAO.getInstance().getEducator(rs.getString(12)));
        return child;
    }

    public ArrayList<Child> getChildhood(){
        ArrayList<Child> childArrayList = new ArrayList<>();
        try {
            ResultSet rs = childhoodStatement.executeQuery();
            while(rs.next())
                childArrayList.add(getChildFromResultSet(rs));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return childArrayList;
    }

    public Child exists(String username, String password) throws SQLException, ParseException {
        Child child = null;
        existsStatement.setString(1, username);
        existsStatement.setString(2, password);
        ResultSet rs = existsStatement.executeQuery();
        if(rs.next()) child = getChildFromResultSet(rs);
        return child;
    }

    public Child exists(String username) throws SQLException, ParseException {
        Child child = null;
        exists2Statement.setString(1, username);
        ResultSet rs = exists2Statement.executeQuery();
        if(rs.next()) child = getChildFromResultSet(rs);
        return child;
    }

    public Connection getConn(){
        return conn;
    }

    public void insert(Child c) throws SQLException {
        insertStatement.setString(1, c.getUsername());
        insertStatement.setString(2, c.getPassword());
        insertStatement.setString(3, c.getName());
        insertStatement.setString(4, c.getSurname());
        insertStatement.setDate(5, Date.valueOf(c.getDate()));
        insertStatement.setString(6, c.getLocation());
        insertStatement.setString(7, c.getAddress());
        insertStatement.setString(8, c.getParentName());
        insertStatement.setString(9, c.getEmail());
        insertStatement.setLong(10, c.getParentNumber());
        insertStatement.setString(11, c.getAvatar());
        insertStatement.setString(12, c.getEducator().getUsername());
        insertStatement.executeUpdate();
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

    public void update(Child c, String key) throws SQLException {
        updateStatement.setString(1, c.getUsername());
        updateStatement.setString(2, c.getPassword());
        updateStatement.setString(3, c.getName());
        updateStatement.setString(4, c.getSurname());
        updateStatement.setDate(5, Date.valueOf(c.getDate()));
        updateStatement.setString(6, c.getLocation());
        updateStatement.setString(7, c.getAddress());
        updateStatement.setString(8, c.getParentName());
        updateStatement.setLong(9, c.getParentNumber());
        updateStatement.setString(10, c.getEmail());
        updateStatement.setString(11, c.getAvatar());
        updateStatement.setString(12, c.getEducator().getUsername());
        updateStatement.setString(13, key);
        updateStatement.executeUpdate();
    }

    public void delete(String key) throws SQLException {
        deleteStatement.setString(1, key);
        deleteStatement.executeUpdate();
    }

    public void deleteForEducator(String username) throws SQLException {
        delete2Statement.setString(1, username);
        delete2Statement.executeUpdate();
    }
}
