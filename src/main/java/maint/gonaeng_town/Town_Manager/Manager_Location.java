package maint.gonaeng_town.Town_Manager;

import maint.gonaeng_town.Gonaeng_Town;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Manager_Location {
    public static HashMap<UUID, BukkitRunnable> Ptasks = new HashMap<>();
    public static int getTownAriaBlock(double LLoc1, double LLoc2, double RLoc1, double RLoc2){
        int ILLoc1 = (int) Math.round(LLoc1);
        int ILLoc2 = (int) Math.round(LLoc2);
        int IRLoc1 = (int) Math.round(RLoc1);
        int IRLoc2 = (int) Math.round(RLoc2);
        return (Math.abs(ILLoc1-IRLoc1)+1)*(Math.abs(ILLoc2-IRLoc2)+1);
    }

    public static void setParticleTownAriaBlock(double LLoc1, double LLoc2, double RLoc1, double RLoc2, Player player, World world){
        UUID playerUUID = player.getPlayer().getUniqueId();
        Location PL = player.getLocation();
        int ILLoc1 = (int) Math.round(LLoc1);
        int ILLoc2 = (int) Math.round(LLoc2);
        int IRLoc1 = (int) Math.round(RLoc1);
        int IRLoc2 = (int) Math.round(RLoc2);
        double XLoc1 = Math.min(LLoc1, RLoc1);
        double ZLoc1 = Math.min(LLoc2, RLoc2);
        double XLoc2 = Math.max(LLoc1, RLoc1);
        double ZLoc2 = Math.max(LLoc2, RLoc2);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GREEN, 1);
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                Location loc1 = new Location(world,XLoc1,PL.getY(),ZLoc1);
                loc1.add(-0.5,0.7,-0.5);

                Location loc2 = new Location(world,XLoc2,PL.getY(),ZLoc1);
                loc2.add(-0.5,0.7,-0.5);

                for(int i = 0; i < Math.abs(ILLoc2-IRLoc2)+1;){
                    world.spawnParticle(Particle.REDSTONE, loc1, 50, 0.1, 0.1, 0.1, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, loc2, 50, 0.1, 0.1, 0.1, 0, dustOptions);
                    loc1.add(0,0,1);
                    loc2.add(0,0,1);
                    i++;
                }

                Location loc3 = new Location(world,XLoc1,PL.getY(),ZLoc1);
                loc3.add(-0.5,0.7,-0.5);

                Location loc4 = new Location(world,XLoc1,PL.getY(),ZLoc2);
                loc4.add(-0.5,0.7,-0.5);

                for(int i = 0; i < Math.abs(ILLoc1-IRLoc1)+1;){
                    world.spawnParticle(Particle.REDSTONE, loc3, 50, 0.1, 0.1, 0.1, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, loc4, 50, 0.1, 0.1, 0.1, 0, dustOptions);
                    loc3.add(1,0,0);
                    loc4.add(1,0,0);
                    i++;
                }
            }
        };
        Ptasks.put(playerUUID, task);
        Ptasks.get(playerUUID).runTaskTimer(Gonaeng_Town.getInstance(), 5, 20L);
    }
}
