package maint.gonaeng_town.Town_Event;

import maint.gonaeng_town.Gonaeng_Town;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static maint.gonaeng_town.Town_DB.DB_TownCreate.*;
import static maint.gonaeng_town.Town_Manager.Manager_Location.Ptasks;
import static maint.gonaeng_town.Town_Manager.Manager_Location.setParticleTownAriaBlock;

public class Event_Create implements Listener {
    public static ArrayList<UUID> SelectMode = new ArrayList<>();
    public static ArrayList<UUID> SetLoc = new ArrayList<>();
    public static HashMap<UUID, BukkitRunnable> tasks = new HashMap<>();
    public static HashMap<UUID, ArrayList<String>> SelectAria = new HashMap<>();
    public static HashMap<UUID, ArrayList<Location>> SelectAriaLoc = new HashMap<>();
    public static HashMap<UUID, World> SelectAriaWorld = new HashMap<>();
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if(SelectMode.contains(playerUUID)){
            SelectMode.remove(playerUUID);
            SelectAria.remove(playerUUID);
            tasks.get(playerUUID).cancel();
            tasks.remove(playerUUID);
            SetLoc.remove(playerUUID);
            SelectAriaLoc.remove(playerUUID);
            if(Ptasks.containsKey(playerUUID)){
                Ptasks.get(playerUUID).cancel();
                Ptasks.remove(playerUUID);
            }
        }
    }

    @EventHandler
    public void PlayerChangeSlot(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = event.getPlayer().getUniqueId();
        if(player.getInventory().getItem(event.getNewSlot())!=null&&player.getInventory().getItem(event.getNewSlot()).getType() == Material.GOLDEN_HOE){
            SelectMode.add(playerUUID);
            ArrayList<String> Aria = new ArrayList<>();
            Aria.add("§cX");
            Aria.add("§cX");
            SelectAria.put(playerUUID,Aria);
            ArrayList<Location> AriaLoc = new ArrayList<>();
            AriaLoc.add(null);
            AriaLoc.add(null);
            SelectAriaLoc.put(playerUUID,AriaLoc);
            boolean dataExist = isDataExists("CreateUserID", playerUUID.toString());
            if(dataExist){
                if(getTownWorld(playerUUID).equals(player.getWorld())) {
                    ArrayList<Double> loc = TownCreateSelect(playerUUID);
                    setParticleTownAriaBlock(loc.get(0), loc.get(1), loc.get(2), loc.get(3), player,player.getWorld());
                }
            }
            tasks.put(playerUUID,SelectMessage(player,playerUUID));
            tasks.get(playerUUID).runTaskTimer(Gonaeng_Town.getInstance(),0L, 20L);
        }else if(player.getInventory().getItem(event.getPreviousSlot())!=null&&player.getInventory().getItem(event.getPreviousSlot()).getType() == Material.GOLDEN_HOE){
            if(SelectMode.contains(playerUUID)){
                SelectMode.remove(playerUUID);
                SelectAria.remove(playerUUID);
                tasks.get(playerUUID).cancel();
                tasks.remove(playerUUID);
                SetLoc.remove(playerUUID);
                SelectAriaLoc.remove(playerUUID);
                player.sendTitle("§6§l선택", "§c모드 사용 중지", 0, 40, 0);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
                if(Ptasks.containsKey(playerUUID)){
                    Ptasks.get(playerUUID).cancel();
                    Ptasks.remove(playerUUID);
                }
            }
        }
    }
    @EventHandler
    private void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = event.getPlayer().getUniqueId();
        if(SelectMode.contains(playerUUID)) {
            if(!SetLoc.contains(playerUUID)) {
                if (event.getHand() == EquipmentSlot.HAND) {
                    if (player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_HOE) {
                        event.setCancelled(true);
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            ArrayList<String> Aria = new ArrayList<>();
                            Aria.add(SelectAria.get(playerUUID).get(0));
                            Aria.add("§aO");
                            SelectAria.put(playerUUID, Aria);
                            ArrayList<Location> AriaLoc = new ArrayList<>();
                            AriaLoc.add(SelectAriaLoc.get(playerUUID).get(0));
                            AriaLoc.add(event.getClickedBlock().getLocation().add(1,0,1));
                            SelectAriaLoc.put(playerUUID, AriaLoc);
                            tasks.get(playerUUID).cancel();
                            tasks.put(playerUUID, SelectMessage(player, playerUUID));
                            tasks.get(playerUUID).runTaskTimer(Gonaeng_Town.getInstance(), 0L, 20L);
                        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            ArrayList<String> Aria = new ArrayList<>();
                            Aria.add("§aO");
                            Aria.add(SelectAria.get(playerUUID).get(1));
                            SelectAria.put(playerUUID, Aria);
                            ArrayList<Location> AriaLoc = new ArrayList<>();
                            AriaLoc.add(event.getClickedBlock().getLocation().add(1,0,1));
                            AriaLoc.add(SelectAriaLoc.get(playerUUID).get(1));
                            SelectAriaLoc.put(playerUUID, AriaLoc);
                            tasks.get(playerUUID).cancel();
                            tasks.put(playerUUID, SelectMessage(player, playerUUID));
                            tasks.get(playerUUID).runTaskTimer(Gonaeng_Town.getInstance(), 0L, 20L);
                        }
                        if (SelectAria.get(playerUUID).get(1).equals("§aO") && SelectAria.get(playerUUID).get(0).equals("§aO")) {
                            if(SelectAriaLoc.get(playerUUID).get(0).equals(SelectAriaLoc.get(playerUUID).get(1))){
                                player.sendMessage("§c구역이 너무 작습니다.");
                                if(SelectMode.contains(playerUUID)){
                                    SelectMode.remove(playerUUID);
                                    SelectAria.remove(playerUUID);
                                    tasks.get(playerUUID).cancel();
                                    tasks.remove(playerUUID);
                                    SetLoc.remove(playerUUID);
                                    SelectAriaLoc.remove(playerUUID);
                                    player.sendTitle("§6§l선택", "§c모드 사용 중지", 0, 40, 0);
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
                                    if(Ptasks.containsKey(playerUUID)){
                                        Ptasks.get(playerUUID).cancel();
                                        Ptasks.remove(playerUUID);
                                    }
                                }
                            }else {
                                player.sendMessage("왼쪽: " + SelectAriaLoc.get(playerUUID).get(0).getX() + " " + SelectAriaLoc.get(playerUUID).get(0).getY() + " " + SelectAriaLoc.get(playerUUID).get(0).getZ() + " 오른쪽: " + SelectAriaLoc.get(playerUUID).get(1).getX() + " " + SelectAriaLoc.get(playerUUID).get(1).getY() + " " + SelectAriaLoc.get(playerUUID).get(1).getZ());
                                SetLoc.add(playerUUID);
                                tasks.get(playerUUID).cancel();
                                SelectAriaWorld.put(playerUUID, player.getWorld());
                                setParticleTownAriaBlock(SelectAriaLoc.get(playerUUID).get(0).getX(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getX(), SelectAriaLoc.get(playerUUID).get(1).getZ(), player, player.getWorld());
                                boolean dataExist = isDataExists("CreateUserID", playerUUID.toString());
                                BukkitRunnable task = getBukkitRunnable(dataExist, player);
                                tasks.put(playerUUID, task);
                                tasks.get(playerUUID).runTaskTimer(Gonaeng_Town.getInstance(), 0L, 20L);
                            }
                        }
                    }
                }
            }
        }
    }
    private static BukkitRunnable getBukkitRunnable(boolean dataExist, Player player) {
        String TitleMessage;
        if(dataExist){
            TitleMessage = "§7§l구역 설정: §e/마을 구역";
        }else{
            TitleMessage = "§7§l생성: §e/마을 생성 <마을 이름>";
        }
        String finalTitleMessage = TitleMessage;
        return new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§6§l선택 완료", finalTitleMessage, 0, 40, 0);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6§l코너 설정: §6왼쪽/오른쪽 클릭 §7§l취소: §7도구 슬롯 변경"));
            }
        };
    }
    private BukkitRunnable SelectMessage(Player player, UUID playerUUID){
        return new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§6§l선택", "§7§l왼쪽: "+SelectAria.get(playerUUID).get(0)+" §7§l오른쪽: "+SelectAria.get(playerUUID).get(1), 0, 40, 0);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6§l코너 설정: §6왼쪽/오른쪽 클릭 §7§l취소: §7도구 슬롯 변경"));
            }
        };
    }
}
