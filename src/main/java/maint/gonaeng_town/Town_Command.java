package maint.gonaeng_town;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static maint.gonaeng_town.Gonaeng_Town.TownAria;
import static maint.gonaeng_town.Gonaeng_Town.TownName;
import static maint.gonaeng_town.Town_Event.Event_Create.*;
import static maint.gonaeng_town.Town_DB.DB_TownCreate.*;
import static maint.gonaeng_town.Town_DB.DB_Villager.VillagerInsert;
import static maint.gonaeng_town.Town_DB.DB_Villager.VillagerIsDataExists;
import static maint.gonaeng_town.Town_Manager.Manager_Location.*;
import static maint.gonaeng_town.Town_Manager.Manager_Location.TownAriaUpdate;
import static maint.gonaeng_town.Town_Manager.Manager_Location.getAllLoc;

public class Town_Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            UUID playerUUID = p.getUniqueId();
            if(args[0].equalsIgnoreCase("구역")) {
                //만든 마을 확인
                boolean dataExist = isDataExists("CreateUserID", playerUUID.toString());
                if (dataExist) {
                    if (SelectAriaLoc.containsKey(playerUUID)&&SelectAriaLoc.get(playerUUID).get(0)!=null&&SelectAriaLoc.get(playerUUID).get(1)!=null) {
                        int BlockCount = getTownAriaBlock(SelectAriaLoc.get(playerUUID).get(0).getX(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getX(), SelectAriaLoc.get(playerUUID).get(1).getZ());
                        TownAriaUpdates(playerUUID, SelectAriaLoc.get(playerUUID).get(0).getX(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getX(), SelectAriaLoc.get(playerUUID).get(1).getZ(),SelectAriaWorld.get(playerUUID).getName(),BlockCount);
                        p.sendMessage("§e마을 구역이 변경되었습니다.("+BlockCount+"블록)");
                        int X1 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(0).getX());
                        int X2 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(1).getX());
                        int Z1 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(0).getZ());
                        int Z2 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(1).getZ());
                        TownAriaUpdate(X1, X2, Z1, Z2, playerUUID.toString());
                        SelectAriaLoc.remove(playerUUID);
                    }else{
                        p.sendMessage("§c지정 된 공간이 없습니다.");
                    }
                }else{
                    p.sendMessage("§c당신은 마을 주인이 아닙니다.");
                }
                if (SelectMode.contains(playerUUID)) {
                    SelectMode.remove(playerUUID);
                    SelectAria.remove(playerUUID);
                    tasks.get(playerUUID).cancel();
                    tasks.remove(playerUUID);
                    SetLoc.remove(playerUUID);
                    if(Ptasks.containsKey(playerUUID)){
                        Ptasks.get(playerUUID).cancel();
                        Ptasks.remove(playerUUID);
                    }
                }
            }else if(args[0].equalsIgnoreCase("생성")) {
                boolean dataExists = isDataExists("TownName", args[1]);
                //소속 마을 확인
                boolean VillagerDataExists = VillagerIsDataExists("UserID", playerUUID.toString());
                if (!VillagerDataExists) {
                    if (!dataExists) {
                        if (SelectAriaLoc.containsKey(playerUUID)&&SelectAriaLoc.get(playerUUID).get(0)!=null&&SelectAriaLoc.get(playerUUID).get(1)!=null) {
                            int BlockCount = getTownAriaBlock(SelectAriaLoc.get(playerUUID).get(0).getX(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getX(), SelectAriaLoc.get(playerUUID).get(1).getZ());
                            TownAriaUpdates(playerUUID, SelectAriaLoc.get(playerUUID).get(0).getX(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getX(), SelectAriaLoc.get(playerUUID).get(1).getZ(),SelectAriaWorld.get(playerUUID).getName(),BlockCount);
                            TownCreateInsert(playerUUID, args[1], SelectAriaLoc.get(playerUUID).get(0).getX(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getX(), SelectAriaLoc.get(playerUUID).get(1).getZ(),SelectAriaWorld.get(playerUUID).getName(),BlockCount);
                            VillagerInsert(playerUUID, playerUUID);
                            Bukkit.broadcastMessage("§e"+p.getName()+"님이 새로운 ["+args[1]+"] 마을을 생성하셨습니다. ("+BlockCount+"블록)");
                            int X1 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(0).getX());
                            int X2 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(1).getX());
                            int Z1 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(0).getZ());
                            int Z2 = (int)Math.round(SelectAriaLoc.get(playerUUID).get(1).getZ());
                            TownAriaUpdate(X1, X2, Z1, Z2, playerUUID.toString());
                            TownName.put(playerUUID.toString(),args[1]);
                            SelectAriaLoc.remove(playerUUID);
                        }else{
                            p.sendMessage("§c지정 된 공간이 없습니다.");
                        }
                    } else {
                        p.sendMessage("§c이미 [" + args[1] + "] 마을이 있습니다.");
                    }
                } else {
                    p.sendMessage("§c이미 소속 되어 있는 마을이 있습니다.");
                }
            }
            if (SelectMode.contains(playerUUID)) {
                SelectMode.remove(playerUUID);
                SelectAria.remove(playerUUID);
                tasks.get(playerUUID).cancel();
                tasks.remove(playerUUID);
                SetLoc.remove(playerUUID);
                if(Ptasks.containsKey(playerUUID)){
                    Ptasks.get(playerUUID).cancel();
                    Ptasks.remove(playerUUID);
                }
            }
            }
        return true;
    }
}
