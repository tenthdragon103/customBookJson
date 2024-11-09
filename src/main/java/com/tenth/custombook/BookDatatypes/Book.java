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
                pageContent.append("\"text\":\"").append(component.text).append("\"");

                // Add hover event if present
                if (component.hoverEvent != null) {
                    pageContent.append(",\"hoverEvent\":{");
                    pageContent.append("\"action\":\"").append(component.hoverEvent.action).append("\",");
                    pageContent.append("\"contents\":[");
                    for (int j = 0; j < component.hoverEvent.contents.size(); j++) {
                        pageContent.append("\"").append(component.hoverEvent.contents.get(j)).append("\"");
                        if (j < component.hoverEvent.contents.size() - 1) {
                            pageContent.append(",");
                        }
                    }
                    pageContent.append("]}");
                }

                // Add click event if present
                if (component.clickEvent != null) {
                    pageContent.append(",\"clickEvent\":{");
                    pageContent.append("\"action\":\"").append(component.clickEvent.action).append("\",");
                    pageContent.append("\"value\":\"").append(component.clickEvent.value).append("\"");
                    pageContent.append("}");
                }

                pageContent.append("}");

                // If this isn't the last component, add a comma
                if (i < page.components.size() - 1) {
                    pageContent.append(",");
                }
            }

            pageContent.append("]");
            pageStrings.add(pageContent.toString());
        }

        return pageStrings;
    }
}
