package org.eugene.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eugene.common.Environment;
import org.eugene.common.UserCredentials;
import org.eugene.common.exceptions.CommandExecutingException;
import org.eugene.common.modelCSV.Chapter;
import org.eugene.common.modelCSV.Coordinates;
import org.eugene.common.modelCSV.MeleeWeapon;
import org.eugene.common.modelCSV.SpaceMarine;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;


public class DBManager {
    private final Logger logger = LogManager.getLogger(DBManager.class);
    private final String dbURL = Environment.getDbUrl();
    private Properties info;
    private Connection connection;

    public DBManager() throws IOException, SQLException {
        loadConfigurationInfo();
        connect();
    }


    public void connect() {
        try {
            connection = DriverManager.getConnection(dbURL, info);
        } catch (SQLException e) {
            try {
                var info = new Properties();
                info.load(new FileInputStream(Environment.getDefaultDbConfigFilePath()));
                connection = DriverManager.getConnection(dbURL, info);
            } catch (IOException | SQLException ex) {
                logger.error("Connecting to DB failed: " + e.getMessage());
                System.exit(1);
            }

        }
        logger.info("Connection with DB sustained successfully");
    }


    private void loadConfigurationInfo() throws IOException {
        info = new Properties();
        try {
            info.load(new FileInputStream(Environment.getDbConfigFilePath()));
        } catch (IOException e) {
            info.load(new FileInputStream(Environment.getDefaultDbConfigFilePath()));
        }
    }



    public void clear(String username) throws SQLException {
        var u_id = getOwnerIdByUsername(username);
        var query = "delete from spacemarine where owner_id = ?";
        var ps = connection.prepareStatement(query);
        ps.setInt(1, u_id);
        ps.execute();
    }

    private int getOwnerIdByUsername(String username) throws SQLException {
        var query = "SELECT id from users where username = ?";
        var ps = connection.prepareStatement(query);
        ps.setString(1, username);
        var res = ps.executeQuery();
        if (res.next()) {
            return res.getInt("id");
        } else {
            throw new SQLException("User with such username not found");
        }
    }

    public void authenticateUser(UserCredentials user) throws SQLException {
        if (isUserExists(user.username())) {
            if (!isPasswordCorrect(user)) {
                throw new SQLException("Wrong password");
            }
        } else {
            addUser(user);
        }
    }

    private boolean isUserExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

    private boolean isPasswordCorrect(UserCredentials user) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.username());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(user.password());
                }
            }
        }
        return false;
    }

    public void addUser(UserCredentials user) throws SQLException {
        if (!isUserExists(user.username())) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                var res = ps.executeUpdate();
                if (res != 0) {
                    logger.info("User <" + user.username() + "> added successfully");
                }
            }
        }
    }

    public LinkedHashSet<SpaceMarine> readCollection() throws SQLException {
        var col = new LinkedHashSet<SpaceMarine>();
        var q = "select * from spacemarine";
        var ps = connection.prepareStatement(q);
        var res = ps.executeQuery();
        while (res.next()) {
            var s = getSpaceMarine(res);
            col.add(s);
        }
        return col;
    }

    private SpaceMarine getSpaceMarine(ResultSet res) {
        try {
            MeleeWeapon mw = !Objects.equals(res.getString("melee_weapon"), "") ? (MeleeWeapon.valueOf(res.getString("melee_weapon"))) : null;
            SpaceMarine sm;
            sm = new SpaceMarine(
                    res.getLong("id"),
                    res.getString("name"),
                    getCoordinates(res.getInt("coordinates_id")),
                    res.getBoolean("loyal"),
                    res.getInt("health"),
                    res.getInt("heartCount"),
                    mw,
                    getChapter(res.getInt("chapter_id"))
            );
            return sm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Chapter getChapter(int chapterId) throws SQLException {
        var q = "select * from chapter where id = ?";
        var ps = connection.prepareStatement(q);
        ps.setInt(1, chapterId);
        var res = ps.executeQuery();
        if (res.next()) {
            return new Chapter(
                    res.getString("name"),
                    res.getString("parent_legion"),
                    res.getInt("marines_count")
            );
        } else {
            throw new SQLException("Chapter not found");
        }
    }


    private Coordinates getCoordinates(int id) throws SQLException {
        var q = "select * from coordinates where id = ?";
        var ps = connection.prepareStatement(q);
        ps.setInt(1, id);
        var res = ps.executeQuery();
        if (res.next()) {
            return new Coordinates(
                    res.getDouble("x"),
                    res.getFloat("y")
            );
        }
        throw new CommandExecutingException("Coordinates not found");
    }

    public void updateElementById(int id, SpaceMarine sm, UserCredentials user) {
        try {
            updateCoordinatesById(id, sm.getCoordinates(), user);
            updateChapterById(id, sm.getChapter(), user);

            var q = "update spacemarine " +
                    "set name = ?, " +
                    "creationdate = ?, " +
                    "loyal = ?, " +
                    "health = ?, " +
                    "heartCount = ?, " +
                    "melee_weapon = ? " +
                    "where id = ? and owner_id = ?";
            var ps = connection.prepareStatement(q);
            var mw = sm.getMeleeWeapon() != null ? sm.getMeleeWeapon().name() : "";
            ps.setString(1, sm.getName());
            ps.setTimestamp(2, Timestamp.valueOf(sm.getCreationDate()));
            ps.setBoolean(3, sm.getLoyal());
            ps.setInt(4, sm.getHealth());
            ps.setInt(5, sm.getHeartCount());
            ps.setString(6, mw);
            ps.setInt(7, id);
            ps.setInt(8, getOwnerIdByUsername(user.username()));
            ps.execute();
            logger.info("Element updated successfully");
        } catch (SQLException e) {
            throw new CommandExecutingException(e.getMessage());
        }
    }

    private void updateChapterById(int id, Chapter c, UserCredentials user) throws SQLException {
        var q = "update chapter " +
                "set name = ?, " +
                "parent_legion = ?, " +
                "marines_count = ? " +
                "where id = ? and owner_id = ?";
        var ps = connection.prepareStatement(q);
        ps.setString(1, c.getName());
        ps.setString(2, c.getParentLegion());
        ps.setInt(3, c.getMarinesCount());
        ps.setInt(4, id);
        ps.setInt(5, getOwnerIdByUsername(user.username()));
        ps.execute();
    }

    private void updateCoordinatesById(int id, Coordinates coordinates, UserCredentials user) throws SQLException {
        var q = "update coordinates " +
                "set x = ?, " +
                "y = ? " +
                "where id = ? and owner_id = ?";
        var ps = connection.prepareStatement(q);
        ps.setDouble(1, coordinates.getX());
        ps.setFloat(2, coordinates.getY());
        ps.setInt(3, id);
        ps.setInt(4, getOwnerIdByUsername(user.username()));
        ps.execute();
    }

    public void removeById(int id, UserCredentials user) {
        try {
            authenticateUser(user);
            var q = "delete from spacemarine where id = ? and owner_id = ?";
            var ps = connection.prepareStatement(q);
            ps.setInt(1, id);
            ps.setInt(2, getOwnerIdByUsername(user.username()));
            if (ps.executeUpdate() == 0) {
                throw new CommandExecutingException("Element with such id and owner wasn't found");
            }
        } catch (SQLException e) {
            throw new CommandExecutingException(e.getMessage());
        }
    }

    public void addSpaceMarine(SpaceMarine spaceMarine, UserCredentials user) throws SQLException {
        var owner_id = getOwnerIdByUsername(user.username());
        var coord_id = addCoordinates(spaceMarine.getCoordinates());
        var ch_id = addChapter(spaceMarine.getChapter());
        var q = "INSERT INTO spacemarine(" +
                "name, " +
                "coordinates_id, " +
                "creationdate, " +
                "loyal, " +
                "health, " +
                "heartCount, " +
                "melee_weapon, " +
                "chapter_id, " +
                "owner_id" + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(q);
        ps.setString(1, spaceMarine.getName());
        ps.setInt(2, coord_id);
        ps.setTimestamp(3, Timestamp.valueOf(spaceMarine.getCreationDate()));
        ps.setBoolean(4, spaceMarine.getLoyal());
        ps.setInt(5, spaceMarine.getHealth());
        ps.setInt(6, spaceMarine.getHeartCount());
        ps.setString(7, spaceMarine.getMeleeWeapon() == null ? "" : spaceMarine.getMeleeWeapon().name());
        ps.setInt(8, ch_id);
        ps.setInt(9, owner_id);
        ps.execute();
    }

    private int addChapter(Chapter c) throws SQLException {
        var q = "insert into chapter (name, parent_legion, marines_count) values (?, ?, ?) returning id";
        var ps = connection.prepareStatement(q);
        ps.setString(1, c.getName());
        ps.setString(2, c.getParentLegion());
        ps.setInt(3, c.getMarinesCount());
        var res = ps.executeQuery();
        res.next();
        return res.getInt("id");

    }

    private int addCoordinates(Coordinates c) throws SQLException {
        var query = "INSERT INTO coordinates(x, y) VALUES (?, ?) returning id";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDouble(1, c.getX());
        ps.setFloat(2, c.getY());
        var res = ps.executeQuery();
        res.next();
        return res.getInt("id");
    }
}