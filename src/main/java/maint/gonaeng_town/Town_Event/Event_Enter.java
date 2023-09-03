package maint.gonaeng_town.Town_Event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static maint.gonaeng_town.Gonaeng_Town.TownAria;
import static maint.gonaeng_town.Gonaeng_Town.TownName;

public class Event_Enter implements Listener {
    public static HashMap<UUID, String> PlayerV = new HashMap<>();
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        int X = Math.round(e.getTo().getBlockX());
        int Z = Math.round(e.getTo().getBlockZ());
        for ( String key : TownAria.keySet() ) {
            ArrayList<Integer> AllX = TownAria.get(key).get(0);
            ArrayList<Integer> AllZ = TownAria.get(key).get(1);
            if(AllX.contains(X)&&AllZ.contains(Z)){
                if(!PlayerV.containsKey(e.getPlayer().getUniqueId())){
                    Player TownAdmin = Bukkit.getServer().getPlayer(UUID.fromString(key));
                    e.getPlayer().sendTitle("§6§l"+TownName.get(key), "§7"+TownAdmin.getName(), 2, 40, 2);
                    PlayerV.put(e.getPlayer().getUniqueId(),TownName.get(key));
                }
                break;
            }else if(PlayerV.containsKey(e.getPlayer().getUniqueId())){
                PlayerV.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendTitle("§6§l야생", "§7보호되지 않은 땅입니다.", 2, 40, 2);
            }
        }
    }
}
