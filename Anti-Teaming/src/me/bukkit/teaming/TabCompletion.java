package me.bukkit.teaming;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompletion implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("anti_teaming") && (sender instanceof Player || sender instanceof ConsoleCommandSender)) {
            List<String> options = new ArrayList<>();
            if (args.length == 1) {
                if (args[0].isEmpty() || args[0].toLowerCase().equals("o")) {
                    options.add("on");
                    options.add("off");
                }
                if (args[0].toLowerCase().equals("on")) options.remove("off");
                if (args[0].toLowerCase().equals("of")) options.remove("on");

//                for (String s : options){
//                    if (!s.toLowerCase().startsWith(args[0].toLowerCase())){
//                        options.remove(s);
//                    }
//                }
                Collections.sort(options);
                return options;
            } else if (args.length >= 1) return new ArrayList<>();
        }
        return null;
    }
}
