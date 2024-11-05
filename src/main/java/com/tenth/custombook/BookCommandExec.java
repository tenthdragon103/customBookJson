package com.tenth.custombook;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class BookCommandExec extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("customBook").setExecutor(this);
        this.getCommand("giveCustomBook").setExecutor(this);
        getLogger().info(ChatColor.GREEN + "Custom Book enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "bye ):");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("customBook")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String book = args[0];
                if (getResource(book) == null) {
                    player.sendMessage(ChatColor.RED + "[ERROR CustomBook] The book \"" + book + "\" does not exist or is improperly formatted.");
                } else {
                    BookWriter writer = new BookWriter();
                    writer.sendBook(book + ".json", player);
                }
            } else {
                getLogger().warning("This command can only be ran by a player!");
            }
        } else if (command.getName().equalsIgnoreCase("giveCustomBook")) { //giveCustomBook <playerName> <book>
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) != null) { // check if valid player otherwise complain abt it
                    Player player = Bukkit.getPlayer(args[0]);
                    String book = args[1];
                    if (getResource(book) == null) {
                        sender.sendMessage(ChatColor.RED + "[ERROR CustomBook] The book \"" + book + "\" does not exist or is improperly formatted.");
                    } else {
                        BookWriter writer = new BookWriter();
                        writer.sendBook(book + ".json", player); //player is never null. ignore warning
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "[ERROR CustomBook] Invalid player name \"" + args[0] + "\".");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[ERROR CustomBook] Expected two arguments <playerName> <book>. Received " + args.length + " arguments.");
            }
        }
        return false;
    }


}
