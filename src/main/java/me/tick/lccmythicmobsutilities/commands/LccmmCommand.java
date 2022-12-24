package me.tick.lccmythicmobsutilities.commands;

import me.tick.lccmythicmobsutilities.DocumentationGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LccmmCommand implements CommandExecutor {

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
            }
            default -> sender.sendMessage("Unknown arg" + args[0] + "!" + System.lineSeparator() + usage);
        }
        return true;
    }
}
