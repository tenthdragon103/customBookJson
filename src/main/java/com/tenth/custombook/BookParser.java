package com.tenth.custombook;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tenth.custombook.BookDatatypes.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class BookParser {
    private static final Gson GSON = new Gson();
    private final BookCommandExec plugin;
    public BookParser(BookCommandExec plugin) {
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

            try (FileReader reader = new FileReader(jsonFile)) {
                JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);

                Book bookData = new Book();

                bookData.title = jsonObject.get("title").getAsString();
                bookData.author = jsonObject.get("author").getAsString();
                bookData.lore = GSON.fromJson(jsonObject.get("lore"), new TypeToken<List<String>>() {}.getType());
                bookData.pages = parsePages(jsonObject.toString());

                BookMeta bookMeta = (BookMeta) book.getItemMeta();

                if (bookMeta != null) {
                    bookMeta.setTitle(bookData.title);
                    bookMeta.setAuthor(bookData.author);
                    bookMeta.setLore(bookData.lore);

                    List<String> pageContents = new ArrayList<>();
                    for (Page page : bookData.pages) { // iterate through each Page instance
                        StringBuilder pageContent = new StringBuilder();
                        for (TextComponent component : page.components) { // iterate through each component
                            pageContent.append(component.text); // add text to page
                        }
                        pageContents.add(pageContent.toString()); // add whole page content
                    }

                    bookMeta.setFireResistant(true);
                    bookMeta.setRarity(ItemRarity.EPIC);

                    bookMeta.setPages(pageContents); // set the pages to meta
                    plugin.getLogger().info(bookMeta.getAsString());
                    book.setItemMeta(bookMeta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    public List<Page> parsePages(String json) {
        List<Page> pages = new ArrayList<>();

        // Parse the root JSON object
        JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
        JsonArray jsonPages = jsonObject.getAsJsonArray("pages");

        // Process each JSON page into a Page object
        for (JsonElement pageElement : jsonPages) {

            if (!pageElement.isJsonArray()) {
                plugin.getLogger().warning("Expected JsonArray for page, but found: " + pageElement);
                continue;
            }

            Page page = new Page();
            page.components = new ArrayList<>(); // Initialize components list

            JsonArray componentsArray = pageElement.getAsJsonArray();

            // Process each JSON component within a page
            for (JsonElement componentElement : componentsArray) {
                JsonObject componentObj = componentElement.getAsJsonObject();

                TextComponent textComponent = new TextComponent();
                textComponent.text = componentObj.get("text").getAsString();

                // Parse hover event if present
                if (componentObj.has("hoverEvent")) {
                    JsonObject hoverEventObj = componentObj.getAsJsonObject("hoverEvent");

                    if (hoverEventObj != null && hoverEventObj.has("action")) {
                        HoverEvent hoverEvent = new HoverEvent();
                        hoverEvent.action = hoverEventObj.get("action").getAsString();

                        // Collect hover contents
                        if (hoverEventObj.has("contents")) {
                            JsonArray hoverContents = hoverEventObj.getAsJsonArray("contents");
                            List<String> hoverTexts = new ArrayList<>();
                            for (JsonElement hoverText : hoverContents) {
                                hoverTexts.add(hoverText.getAsString());
                            }
                            hoverEvent.contents = hoverTexts;
                        }

                        textComponent.hoverEvent = hoverEvent;
                    }
                }

                // Parse click event if present
                if (componentObj.has("clickEvent")) {
                    JsonObject clickEventObj = componentObj.getAsJsonObject("clickEvent");

                    if (clickEventObj != null && clickEventObj.has("action") && clickEventObj.has("value")) {
                        ClickEvent clickEvent = new ClickEvent();
                        clickEvent.action = clickEventObj.get("action").getAsString();
                        clickEvent.value = clickEventObj.get("value").getAsString();

                        textComponent.clickEvent = clickEvent;
                    }
                }

                // Add the component to the page
                page.components.add(textComponent);
            }

            // Add the processed page to the list of pages
            pages.add(page);
        }

        return pages;
    }
}