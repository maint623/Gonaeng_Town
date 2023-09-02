package maint.gonaeng_town;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static maint.gonaeng_town.Create_Event.*;
import static maint.gonaeng_town.Town_DB.DB_TownCreate.*;
import static maint.gonaeng_town.Town_DB.DB_Villager.VillagerInsert;
import static maint.gonaeng_town.Town_DB.DB_Villager.VillagerIsDataExists;

public class Town_Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            UUID playerUUID = p.getUniqueId();
            if(args[0].equalsIgnoreCase("구역")) {
                if (p.hasPermission("Town.CreateTown")) {
                    //마을 이름 중복 확인
                    boolean dataExists = isDataExists("TownName", args[1]);
                    //만든 마을 확인
                    boolean dataExist = isDataExists("CreateUserID", playerUUID.toString());
                    //소속 마을 확인
                    boolean VillagerDataExists = VillagerIsDataExists("UserID", playerUUID.toString());
                    if (dataExist) {
                        if (SelectAriaLoc.get(playerUUID).get(0)!=null&&SelectAriaLoc.get(playerUUID).get(1)!=null) {
                            TownAriaUpdate(playerUUID, SelectAriaLoc.get(playerUUID).get(0).getY(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getY(), SelectAriaLoc.get(playerUUID).get(1).getZ(),SelectAriaWorld.get(playerUUID));
                            SelectAriaLoc.remove(playerUUID);
                            p.sendMessage("§e마을 구역이 변경되었습니다.");
                        }else{
                            p.sendMessage("§c지정 된 공간이 없습니다.");
                        }
                    } else {
                        if (!VillagerDataExists) {
                            if (!dataExists) {
                                if (SelectAriaLoc.get(playerUUID).get(0)!=null&&SelectAriaLoc.get(playerUUID).get(1)!=null) {
                                    TownCreateInsert(playerUUID, args[1], SelectAriaLoc.get(playerUUID).get(0).getY(), SelectAriaLoc.get(playerUUID).get(0).getZ(), SelectAriaLoc.get(playerUUID).get(1).getY(), SelectAriaLoc.get(playerUUID).get(1).getZ(),SelectAriaWorld.get(playerUUID));
                                    VillagerInsert(playerUUID, playerUUID);
                                    SelectAriaLoc.remove(playerUUID);
                                    Bukkit.broadcastMessage("§e"+p.getName()+"님이 새로운 ["+args[1]+"] 마을을 생성하셨습니다.");
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
                    }
                }
            }
        }
        return true;
    }
}
