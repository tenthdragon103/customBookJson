package com.tenth.custombook.BookDatatypes;

import java.util.ArrayList;
import java.util.List;

public class Book {
    public List<Page> pages;
    public String title;
    public List<String> lore;
    public String author;

    public List<String> pagesToStringList() {
        List<String> pageStrings = new ArrayList<>();

        for (Page page : pages) {
            StringBuilder pageContent = new StringBuilder("[");

            for (int i = 0; i < page.components.size(); i++) {
                TextComponent component = page.components.get(i);
                pageContent.append("{");

                // Add text
                if (component.text != null) {
                    pageContent.append("\"text\":\"").append(escapeJson(component.text)).append("\"");
                }

                // Add hover event if present
                if (component.hoverEvent != null) {
                    pageContent.append(",\"hoverEvent\":{");
                    pageContent.append("\"action\":\"").append(component.hoverEvent.action).append("\",");
                    if (component.hoverEvent.contents != null  && !component.hoverEvent.contents.isEmpty()) {
                        pageContent.append("\"contents\":[");
                        for (int j = 0; j < component.hoverEvent.contents.size(); j++) {
                            pageContent.append("\"").append(component.hoverEvent.contents.get(j)).append("\"");
                            if (j < component.hoverEvent.contents.size() - 1) { // check if its not the last object
                                pageContent.append(",");
                            }
                        }
                        pageContent.append("]");
                    } else {
                        pageContent.append("\"contents\":[]"); // put empty array to avoid malformed json
                    }

                    pageContent.append("}");
                }

                // Add click event if present
                if (component.clickEvent != null) {
                    pageContent.append(",\"clickEvent\":{");
                    pageContent.append("\"action\":\"").append(component.clickEvent.action).append("\",");
                    pageContent.append("\"value\":\"").append(component.clickEvent.value).append("\"");
                    pageContent.append("}");
                }

                pageContent.append("}");

                if (i < page.components.size() - 1) { // check if its not the last object
                    pageContent.append(",");
                }
            }

            pageContent.append("]");
            pageStrings.add(pageContent.toString());
        }

        return pageStrings;
    }

    private String escapeJson(String input) { // voodoo shit (aka chatgpt written and i didnt bother to read it)
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
