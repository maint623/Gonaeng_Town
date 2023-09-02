package maint.gonaeng_town.Town_DB;

import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import static maint.gonaeng_town.Gonaeng_Town.connection;

public class DB_TownCreate {
    public static void TownCreateInsert(UUID userid, String TownName, double LLocX, double LLocZ, double RLocX, double RLocZ, World world) {
        String sql = "insert into townloc (CreateUserID, TownName, LLocX, LLocZ, RLocX, RLocZ, World) values ('" + userid +"','" + TownName +"','" + LLocX +"','" + LLocZ +"','" + RLocX +"','" + RLocZ +"','" + world +"');";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void TownAriaUpdate(UUID userid, double LLocX, double LLocZ, double RLocX, double RLocZ, World world) {
        String sql = "update playtime set LLocX='"+LLocX+"',LLocY='"+LLocZ+"',RLocX='"+RLocX+"',RLocY='"+RLocZ+"',World='"+world+"' where CreateUserID='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Integer TownCreateSelect(UUID userid) {
        String sql = "select * from playtime where UserID='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while(true){
                try {
                    if (!Objects.requireNonNull(rs).next()) break;
                    count = rs.getInt("PlaySecond");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            rs.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isDataExists(String columnName, String value) {
        boolean exists = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String query = "SELECT COUNT(*) FROM townloc WHERE " + columnName + " = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, value);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = (count > 0);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return exists;
    }
}
