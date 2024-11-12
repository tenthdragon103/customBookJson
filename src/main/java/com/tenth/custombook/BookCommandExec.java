package com.tenth.custombook;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class BookCommandExec extends JavaPlugin {
    private BookParser bookParser;

    @Override
    public void onEnable() {
        this.bookParser = new BookParser(this);
        this.getCommand("customBook").setExecutor(this);
        this.getCommand("giveCustomBook").setExecutor(this);
        getLogger().info(ChatColor.GREEN + "Custom Book enabled");

        if (!getDataFolder().exists()) { // if datafolder doesnt exist create one
            getDataFolder().mkdirs();
        }

        File jsonFile = new File(getDataFolder(), "examplebook.json");

        if (!jsonFile.exists()) {
            try (InputStream in = getResource("examplebook.json")) { // default file inside jar
                if (in != null) {
                    Files.copy(in, jsonFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    getLogger().info("Default examplebook.json created.");
                } else {
                    getLogger().warning("Default examplebook.json not found.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "bye ):");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("customBook")) { // customBook <book>
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String book = args[0];
                    File bookFile = new File(getDataFolder(), book + ".json");
                    if (!bookFile.exists()) {
                        player.sendMessage(ChatColor.RED + "[CustomBook] ERROR: The book \"" + book + "\" does not exist or is improperly formatted.");
                        return true;
                    } else {
                        bookParser.sendBook(bookFile.getName(), player);
                        return true;
                    }
                } else {
                    getLogger().warning("This command can only be run by a player!");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[CustomBook] ERROR: Expected one argument <book>. Received " + args.length + " arguments.");
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("giveCustomBook")) { // giveCustomBook <playerName> <book>
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "[CustomBook] ERROR: Invalid player name \"" + args[0] + "\".");
                    return true;
                }
                String book = args[1];
                File bookFile = new File(getDataFolder(), book + ".json");
                if (!bookFile.exists()) {
                    sender.sendMessage(ChatColor.RED + "[CustomBook] ERROR: The book \"" + book + "\" does not exist or is improperly formatted.");
                } else {
                    bookParser.sendBook(bookFile.getName(), targetPlayer);
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "[CustomBook] ERROR: Expected two arguments <playerName> <book>. Received " + args.length + " arguments.");
                return true;
            }
        }
        return false;
    }
}