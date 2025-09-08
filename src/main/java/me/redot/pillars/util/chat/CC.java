package me.redot.pillars.util.chat;

import net.kyori.adventure.text.format.TextColor;

import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public enum CC {

    RED(new ChatColor(TextColor.color(0xFF5555))),
    B_RED(new ChatColor(RED, BOLD)),
    I_RED(new ChatColor(RED, ITALIC)),
    BI_RED(new ChatColor(RED, BOLD, ITALIC)),

    D_RED(new ChatColor(TextColor.color(0xAA0000))),
    BD_RED(new ChatColor(D_RED, BOLD)),
    ID_RED(new ChatColor(D_RED, ITALIC)),
    BID_RED(new ChatColor(D_RED, BOLD, ITALIC)),

    GOLD(new ChatColor(TextColor.color(0xFFAA00))),
    B_GOLD(new ChatColor(GOLD, BOLD)),
    I_GOLD(new ChatColor(GOLD, ITALIC)),
    BI_GOLD(new ChatColor(GOLD, BOLD, ITALIC)),

    YELLOW(new ChatColor(TextColor.color(0xFFFF55))),
    B_YELLOW(new ChatColor(YELLOW, BOLD)),
    I_YELLOW(new ChatColor(YELLOW, ITALIC)),
    BI_YELLOW(new ChatColor(YELLOW, BOLD, ITALIC)),

    PINK(new ChatColor(TextColor.color(0xFF55FF))),
    B_PINK(new ChatColor(PINK, BOLD)),
    I_PINK(new ChatColor(PINK, ITALIC)),
    BI_PINK(new ChatColor(PINK, BOLD, ITALIC)),

    PURPLE(new ChatColor(TextColor.color(0xAA00AA))),
    B_PURPLE(new ChatColor(PURPLE, BOLD)),
    I_PURPLE(new ChatColor(PURPLE, ITALIC)),
    BI_PURPLE(new ChatColor(PURPLE, BOLD, ITALIC)),

    LIME(new ChatColor(TextColor.color(0x55FF55))),
    B_LIME(new ChatColor(LIME, BOLD)),
    I_LIME(new ChatColor(LIME, ITALIC)),
    BI_LIME(new ChatColor(LIME, BOLD, ITALIC)),

    GREEN(new ChatColor(TextColor.color(0x00AA00))),
    B_GREEN(new ChatColor(GREEN, BOLD)),
    I_GREEN(new ChatColor(GREEN, ITALIC)),
    BI_GREEN(new ChatColor(GREEN, BOLD, ITALIC)),

    BLUE(new ChatColor(TextColor.color(0x5555FF))),
    B_BLUE(new ChatColor(BLUE, BOLD)),
    I_BLUE(new ChatColor(BLUE, ITALIC)),
    BI_BLUE(new ChatColor(BLUE, BOLD, ITALIC)),

    D_BLUE(new ChatColor(TextColor.color(0x0000AA))),
    BD_BLUE(new ChatColor(D_BLUE, BOLD)),
    ID_BLUE(new ChatColor(D_BLUE, ITALIC)),
    BID_BLUE(new ChatColor(D_BLUE, BOLD, ITALIC)),

    AQUA(new ChatColor(TextColor.color(0x55FFFF))),
    B_AQUA(new ChatColor(AQUA, BOLD)),
    I_AQUA(new ChatColor(AQUA, ITALIC)),
    BI_AQUA(new ChatColor(AQUA, BOLD, ITALIC)),

    D_AQUA(new ChatColor(TextColor.color(0x00AAAA))),
    BD_AQUA(new ChatColor(D_AQUA, BOLD)),
    ID_AQUA(new ChatColor(D_AQUA, ITALIC)),
    BID_AQUA(new ChatColor(D_AQUA, BOLD, ITALIC)),

    WHITE(new ChatColor(TextColor.color(0xFFFFFF))),
    B_WHITE(new ChatColor(WHITE, BOLD)),
    I_WHITE(new ChatColor(WHITE, ITALIC)),
    BI_WHITE(new ChatColor(WHITE, BOLD, ITALIC)),

    GRAY(new ChatColor(TextColor.color(0xAAAAAA))),
    B_GRAY(new ChatColor(GRAY, BOLD)),
    I_GRAY(new ChatColor(GRAY, ITALIC)),
    BI_GRAY(new ChatColor(GRAY, BOLD, ITALIC)),

    D_GRAY(new ChatColor(TextColor.color(0x555555))),
    BD_GRAY(new ChatColor(D_GRAY, BOLD)),
    ID_GRAY(new ChatColor(D_GRAY, ITALIC)),
    BID_GRAY(new ChatColor(D_GRAY, BOLD, ITALIC)),

    BLACK(new ChatColor(TextColor.color(0x000000))),
    B_BLACK(new ChatColor(BLACK, BOLD)),
    I_BLACK(new ChatColor(BLACK, ITALIC)),
    BI_BLACK(new ChatColor(BLACK, BOLD, ITALIC)),

    PRIMARY(new ChatColor(AQUA)),
    B_PRIMARY(new ChatColor(PRIMARY, BOLD)),
    I_PRIMARY(new ChatColor(PRIMARY, ITALIC)),
    BI_PRIMARY(new ChatColor(PRIMARY, BOLD, ITALIC)),

    SECONDARY(new ChatColor(PINK)),
    B_SECONDARY(new ChatColor(SECONDARY, BOLD)),
    I_SECONDARY(new ChatColor(SECONDARY, ITALIC)),
    BI_SECONDARY(new ChatColor(SECONDARY, BOLD, ITALIC));

    private final ChatColor color;

    CC(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return this.color;
    }

}