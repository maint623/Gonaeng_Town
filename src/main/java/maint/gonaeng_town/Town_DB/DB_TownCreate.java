package maint.gonaeng_town.Town_DB;

import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static maint.gonaeng_town.Gonaeng_Town.connection;
import static org.bukkit.Bukkit.getServer;

public class DB_TownCreate {
    public static void TownCreateInsert(UUID userid, String TownName, double LLocX, double LLocZ, double RLocX, double RLocZ, String world, int Block) {
        String sql = "insert into townloc (CreateUserID, TownName, LLocX, LLocZ, RLocX, RLocZ, World, Block) values ('" + userid +"','" + TownName +"','" + LLocX +"','" + LLocZ +"','" + RLocX +"','" + RLocZ +"','" + world +"','" + Block +"');";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void TownAriaUpdates(UUID userid, double LLocX, double LLocZ, double RLocX, double RLocZ, String world, int Block) {
        String sql = "update townloc set LLocX='"+LLocX+"',LLocZ='"+LLocZ+"',RLocX='"+RLocX+"',RLocZ='"+RLocZ+"',World='"+world+"',Block='"+Block+"' where CreateUserID='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static World getTownWorld(UUID userid) {
        String sql = "select * from townloc where CreateUserID='"+userid+"';";
        World world = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(true){
                try {
                    if (!Objects.requireNonNull(rs).next()) break;
                    world = getServer().getWorld(rs.getString("World"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            rs.close();
            return world;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ResultSet getTownRS() {
        String sql = "select * from townloc;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<Double> TownCreateSelect(UUID userid) {
        String sql = "select * from townloc where CreateUserID='"+userid+"';";
        ArrayList<Double> Loc = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(true){
                try {
                    if (!Objects.requireNonNull(rs).next()) break;
                    Loc.add(rs.getDouble("LLocX"));
                    Loc.add(rs.getDouble("LLocZ"));
                    Loc.add(rs.getDouble("RLocX"));
                    Loc.add(rs.getDouble("RLocZ"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            rs.close();
            return Loc;
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
