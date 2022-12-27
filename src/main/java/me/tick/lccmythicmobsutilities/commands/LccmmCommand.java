package me.tick.lccmythicmobsutilities.commands;

import me.tick.lccmythicmobsutilities.DocumentationGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LccmmCommand implements CommandExecutor, TabCompleter {

    public static String usage = "Usage: /lccmm <generateDocs>";

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (args.length == 0) {
            sender.sendMessage(usage);
            return true;
        }
        switch (args[0]) {
            case "generateDocs" -> {
                sender.sendMessage("Generating docs...");
                try {
                    DocumentationGenerator.writeToFile();
                } catch (Exception e) {
                    sender.sendMessage("An error occurred while generating docs: " + e.getMessage());
                    e.printStackTrace();
                }
                sender.sendMessage("Done!");
            }
            default -> sender.sendMessage("Unknown arg " + args[0] + "!" + System.lineSeparator() + usage);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("generateDocs");
        }
        return List.of();
    }
}
