package me.bukkit.teaming;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final Main plugin = Main.getPlugin(Main.class);
    private final FileConfiguration cfg = plugin.yaml.cfg;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("anti_teaming") && (sender instanceof Player || sender instanceof ConsoleCommandSender)) {
            if (sender instanceof Player) {
                if (!sender.isOp()) {
                    sender.sendMessage(plugin.redpref + "You must be an operator to perform this command!");
                    return false;
                }
            }
            int arg = args.length;
            if (arg == 0 || arg > 1) {
                sender.sendMessage(plugin.redpref + "Anti Teaming Usage: /anti_teaming [on/off]");
                return false;
            }

            String state = args[0];
            if (state.equals("on")) {
                sender.sendMessage(plugin.manager.toggle(true));
            } else if (state.equals("off")) {
                sender.sendMessage(plugin.manager.toggle(false));
            } else {
                sender.sendMessage(plugin.redpref + "Anti Teaming Usage: /anti_teaming [on/off]");
                return false;
            }

        }
        return true;
    }
}
