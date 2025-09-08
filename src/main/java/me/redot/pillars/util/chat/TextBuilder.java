package me.redot.pillars.util.chat;

import me.redot.pillars.cache.model.player.GamePlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.function.Consumer;

public class TextBuilder {

    private Component component = Component.empty();
    private final CC primary, secondary;

    private TextBuilder() {
        this.primary = CC.PRIMARY;
        this.secondary = CC.SECONDARY;
    }

    private TextBuilder(CC primary, CC secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public TextBuilder add(String text) {
        return this.add(text, this.primary);
    }

    public TextBuilder arg(String text) {
        return this.add(text, this.secondary);
    }

    public TextBuilder add(String text, CC color) {
        return this.add(text, color.getColor());
    }

    public TextBuilder add(String text, int color, TextDecoration... decorations) {
        return this.add(text, new ChatColor(TextColor.color(color), decorations));
    }

    public TextBuilder add(String text, ChatColor color) {
        this.component = this.component.append(Component.text(text, color.asStyle()));
        return this;
    }

    public TextBuilder addComponent(Component component) {
        this.component = this.component.append(component);
        return this;
    }

    public TextBuilder append(TextBuilder builder) {
        return this.addOther(builder);
    }

    public TextBuilder addOther(TextBuilder builderToAppend) {
        this.component = this.component.append(builderToAppend.build());
        return this;
    }

    public TextBuilder newLine() {
        this.component = this.component.appendNewline();
        return this;
    }

    public TextBuilder clickAction(ClickEvent clickEvent) {
        this.component = this.component.clickEvent(clickEvent);
        return this;
    }

    public TextBuilder hoverText(Component component) {
        this.component = this.component.hoverEvent(HoverEvent.showText(component));
        return this;
    }

    public TextBuilder hoverEvent(HoverEvent<?> hoverEvent) {
        this.component = this.component.hoverEvent(hoverEvent);
        return this;
    }

    public static TextBuilder success(String text) {
        return success().add(text);
    }

    public static TextBuilder success() {
        return new TextBuilder(CC.LIME, CC.GREEN);
    }

    public static TextBuilder error(String text) {
        return error().add(text);
    }

    public static TextBuilder error() {
        return new TextBuilder(CC.RED, CC.D_RED);
    }

    public static TextBuilder warning(String text) {
        return warning().add(text);
    }

    public static TextBuilder warning() {
        return new TextBuilder(CC.GRAY, CC.YELLOW);
    }

    public static TextBuilder empty(String text) {
        return empty().add(text);
    }

    public static TextBuilder empty() {
        return new TextBuilder(CC.WHITE, CC.WHITE);
    }

    public static TextBuilder text() {
        return new TextBuilder(CC.PRIMARY, CC.SECONDARY);
    }

    public static TextBuilder text(String text) {
        return text().add(text);
    }

    public static TextBuilder text(String text, int color, TextDecoration... decorations) {
        return text().add(text, color, decorations);
    }

    public static TextBuilder text(String text, CC color) {
        return text().add(text, color);
    }

    public static TextBuilder custom(CC primary, CC secondary) {
        return new TextBuilder(primary, secondary);
    }

    public <T extends GamePlayer<?>> TextBuilder send(T gamePlayer) {
        return this.send(gamePlayer.getPlayer());
    }

    public <T extends Audience> TextBuilder send(T audience) {
        audience.sendMessage(this.build());
        return this;
    }

    public TextBuilder tap(Consumer<TextBuilder> consumer) {
        consumer.accept(this);
        return this;
    }

    public Component build() {
        return this.component;
    }

}