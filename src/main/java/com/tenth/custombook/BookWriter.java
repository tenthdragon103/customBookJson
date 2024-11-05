package com.tenth.custombook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

import org.bukkit.Material;

public class BookWriter {
    private final BookCommandExec plugin;
    public BookWriter(BookCommandExec plugin) {
        this.plugin = plugin;
    }

    public void sendBook(String jsonFileName, Player player) {
        player.getInventory().addItem(makeBook(jsonFileName));
    }

    public void sendBook(ItemStack book, Player player) { //just in case ig maybe someone wants to use this for their own manual stuff
        player.getInventory().addItem(book);
    }

    public ItemStack makeBook(String jsonFileName) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        File jsonFile = new File(plugin.getDataFolder(), jsonFileName); // getDataFolder() is the plugin dir under /plugins/

        try {
            if (!jsonFile.exists()) {
                plugin.getLogger().warning(ChatColor.RED + "[CustomBook] ERROR: " + jsonFileName + " does not exist.");
                return null;
            }

            Gson gson = new Gson();
            try (FileReader reader = new FileReader(jsonFile)) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                String title = jsonObject.get("title").getAsString();
                String author = jsonObject.get("author").getAsString();
                List<String> lore = gson.fromJson(jsonObject.get("lore"), new TypeToken<List<String>>() {}.getType());
                String[] pages = gson.fromJson(jsonObject.get("pages"), String[].class);

                // TODO: 11/4/2024 make the method able to parse click events and hover events from the json

                BookMeta bookMeta = (BookMeta) book.getItemMeta();

                if (bookMeta != null) {
                    bookMeta.setTitle(title);
                    bookMeta.setAuthor(author);
                    bookMeta.setPages(pages);
                    bookMeta.setLore(lore);
                    book.setItemMeta(bookMeta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }
}
