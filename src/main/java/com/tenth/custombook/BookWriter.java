package com.tenth.custombook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.InputStreamReader;
import java.util.List;

public class BookWriter extends JavaPlugin {
    public void sendBook(String jsonFile, Player player) {
        player.getInventory().addItem(makeBook(jsonFile));
    }

    public void sendBook(ItemStack book, Player player) { //just in case ig maybe someone wants to use this for their own manual stuff
        player.getInventory().addItem(book);
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

            String title = jsonObject.get("title").getAsString(); //grab title from json file
            String author = jsonObject.get("author").getAsString(); //grab author from json file
            List<String> lore = gson.fromJson(jsonObject.get("lore"), new TypeToken<List<String>>(){}.getType()); //grab lore, should be whatever this is idek what that means
            String[] pages = gson.fromJson(jsonObject.get("pages"), String[].class); //grab all the pages which should be all strings

            book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();

            if (bookMeta != null) {
                bookMeta.setTitle(title);
                bookMeta.setAuthor(author);
                bookMeta.setPages(pages);
                bookMeta.setLore(lore);
                book.setItemMeta(bookMeta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;

    }
}
