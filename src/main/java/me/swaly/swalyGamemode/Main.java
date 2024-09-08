package me.swaly.swalyGamemode;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.GameMode;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!getConfig().contains("messages")) {
            saveDefaultConfig();
        }
        getLogger().info("SwalyGamemode plugin sikeresen elindult.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SwalyGamemode plugin sikeresen kikapcsolt.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("gmc")) {
            return handleGamemodeChange(sender, args, GameMode.CREATIVE, "gmc.use", "Kreatív");
        } else if (label.equalsIgnoreCase("gms")) {
            return handleGamemodeChange(sender, args, GameMode.SURVIVAL, "gms.use", "Túlélő");
        } else if (label.equalsIgnoreCase("gmsp")) {
            return handleGamemodeChange(sender, args, GameMode.SPECTATOR, "gmsp.use", "Szemlélő");
        } else if (label.equalsIgnoreCase("gma")) {
            return handleGamemodeChange(sender, args, GameMode.ADVENTURE, "gma.use", "Kaland");
        } else if (label.equalsIgnoreCase("gm")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("gmreload.use")) {
                    reloadConfig();
                    sender.sendMessage("§aA plugin sikeresen újratöltve!");
                    return true;
                } else {
                    sender.sendMessage("§cEhhez nincs jogod.");
                    return true;
                }
            } else {
                if (sender.hasPermission("gm.view")) {
                    sender.sendMessage("§9Elérhető parancsok:\n§b/gmc\n§b/gms\n§b/gmsp\n§b/gma\n§b/gm reload");
                    return true;
                } else {
                    sender.sendMessage("§cEhhez nincs jogod.");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean handleGamemodeChange(CommandSender sender, String[] args, GameMode mode, String permission, String gmStatus) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cEhhez nincs jogod.");
            return true;
        }

        Player target;
        if (args.length == 0 && sender instanceof Player) {
            target = (Player) sender;
        } else if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cA játékos nem található.");
                return true;
            }
        } else {
            sender.sendMessage("Használat: /" + mode.toString().toLowerCase() + " [játékos]");
            return true;
        }

        target.setGameMode(mode);
        String message = getConfig().getString("messages.gamemode-change")
                .replace("{player}", target.getName())
                .replace("{gmstatus}", gmStatus);

        if (!target.equals(sender)) {
            sender.sendMessage(message);
        }
        target.sendMessage(message);
        return true;
    }
}
