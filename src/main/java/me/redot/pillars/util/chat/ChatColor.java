package me.redot.pillars.util.chat;

import me.redot.pillars.util.CollectionUtil;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ChatColor implements Cloneable {

    private final TextColor color;
    private final TextDecoration[] decorations;

    public ChatColor(CC chatColor, TextDecoration... more) {
        this(chatColor.getColor(), more);
    }

    public ChatColor(ChatColor otherChatColor, TextDecoration... more) {
        this.color = otherChatColor.color;
        this.decorations = CollectionUtil.concat(otherChatColor.decorations, more);
    }

    public ChatColor(TextColor color, TextDecoration... decorations) {
        this.decorations = decorations;
        this.color = color;
    }

    public Style asStyle() {
        if (this.decorations.length > 0) {
            return Style.style(this.color, this.decorations);
        }
        return Style.style(this.color);
    }

    @Override
    @SuppressWarnings("all")
    public ChatColor clone() {
        return new ChatColor(this.color, this.decorations);
    }

}
