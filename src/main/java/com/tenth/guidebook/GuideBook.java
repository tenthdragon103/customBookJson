package com.tenth.guidebook;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;

public final class GuideBook extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("helpbook").setExecutor(this);
        getServer().getPluginManager().registerEvents(new GuideBookListener(this), this);
        getLogger().info(ChatColor.GREEN + "Guide Book enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "bye ):");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("helpbook") && sender instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;

            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta();
                return true;
            }
        return false;
    }

    public void sendBook(Player player) {

    }

    public ItemStack makeBook(String JsonFile) {
        ItemStack book = null;
        try {
            Gson gson = new Gson();
            if (getResource(JsonFile) == null) {
                getLogger().warning(ChatColor.RED + "[GuideBook] ERROR: " + JsonFile + " is null");
                return Material.WRITTEN_BOOK;
            }
            InputStreamReader reader = new InputStreamReader(getResource(JsonFile)); //never null. ignore warning.
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            String title = jsonObject.get("title").getAsString();
            String author = jsonObject.get("author").getAsString();
            String[] pages = gson.fromJson(jsonObject.get("pages"), String[].class);

            book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();

            if (bookMeta != null) {
                bookMeta.setTitle(title);
                bookMeta.setAuthor(author);
                bookMeta.setPages(pages);
                book.setItemMeta(bookMeta);
            }

            player.getInventory().addItem(book); // Give the book to the player
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }
}
