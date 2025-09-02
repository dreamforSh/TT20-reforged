package net.sjhub.tt20forged.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.sjhub.tt20forged.TT20Forged;
import net.sjhub.tt20forged.config.MainConfig;
import net.sjhub.tt20forged.util.TPSUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainCommand {

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("tt20").executes(MainCommand::executeMain)
                        .then(Commands.literal("tps").executes(MainCommand::executeTps))
                        .then(Commands.literal("status").executes(MainCommand::executeStatus))
                        .then(Commands.literal("toggle").requires(ctx -> ctx.hasPermission(3))
                                .then(Commands.argument("key", StringArgumentType.string())
                                        .suggests(MainCommand::suggestConfigKeys)
                                        .executes(MainCommand::executeToggle)))
                        .then(Commands.literal("reload").requires(ctx -> ctx.hasPermission(2)).executes(MainCommand::executeReload))
                        .then(Commands.literal("set").requires(ctx -> ctx.hasPermission(3))
                                .then(Commands.literal("cap")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                                .executes(MainCommand::executeSetCap))))
                        .then(Commands.literal("mask").requires(ctx -> ctx.hasPermission(3))
                                .then(Commands.literal("list").executes(MainCommand::executeMaskList))
                                .then(Commands.literal("type")
                                        .then(Commands.argument("value", StringArgumentType.string())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(new String[]{"whitelist", "blacklist"}, builder))
                                                .executes(MainCommand::executeMaskType)))
                                .then(Commands.literal("add")
                                        .then(Commands.argument("block", ResourceLocationArgument.id())
                                                .executes(MainCommand::executeMaskAdd)))
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("block", StringArgumentType.string())
                                                .suggests(MainCommand::suggestMaskBlocks)
                                                .executes(MainCommand::executeMaskRemove))))
        );
    }

    private static int executeSetCap(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        int cap = IntegerArgumentType.getInteger(context, "value");
        TT20Forged.config.setTickRepeatCap(cap);
        TT20Forged.config.save();
        source.sendSystemMessage(Component.translatable("tt20.command.set.cap.feedback", cap));
        return 1;
    }

    private static int executeMaskList(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        List<String> blocks = TT20Forged.blockEntityMaskConfig.getBlocks();
        String type = TT20Forged.blockEntityMaskConfig.getType();

        source.sendSystemMessage(Component.translatable("tt20.command.mask.list.header", type, blocks.size()));
        for (String block : blocks) {
            source.sendSystemMessage(Component.literal(" - " + block));
        }
        return 1;
    }

    private static int executeMaskType(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String type = StringArgumentType.getString(context, "value");

        if (!type.equals("whitelist") && !type.equals("blacklist")) {
            source.sendFailure(Component.translatable("tt20.command.mask.type.invalid", type));
            return 0;
        }

        TT20Forged.blockEntityMaskConfig.setType(type);
        TT20Forged.blockEntityMaskConfig.save();
        TT20Forged.blockEntityMaskConfig.reload(); // Reload to apply mask changes
        source.sendSystemMessage(Component.translatable("tt20.command.mask.type.feedback", type));
        return 1;
    }

    private static int executeMaskAdd(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String blockId = ResourceLocationArgument.getId(context, "block").toString();

        if (TT20Forged.blockEntityMaskConfig.addBlock(blockId)) {
            TT20Forged.blockEntityMaskConfig.save();
            TT20Forged.blockEntityMaskConfig.reload();
            source.sendSystemMessage(Component.translatable("tt20.command.mask.add.feedback", blockId));
        } else {
            source.sendFailure(Component.translatable("tt20.command.mask.add.exists", blockId));
        }
        return 1;
    }

    private static int executeMaskRemove(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String blockId = StringArgumentType.getString(context, "block");

        if (TT20Forged.blockEntityMaskConfig.removeBlock(blockId)) {
            TT20Forged.blockEntityMaskConfig.save();
            TT20Forged.blockEntityMaskConfig.reload();
            source.sendSystemMessage(Component.translatable("tt20.command.mask.remove.feedback", blockId));
        } else {
            source.sendFailure(Component.translatable("tt20.command.mask.remove.not_found", blockId));
        }
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestMaskBlocks(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        TT20Forged.blockEntityMaskConfig.getBlocks().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestConfigKeys(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        MainConfig.getBooleanOptions().stream().map(option -> option.getKey()).forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static int executeMain(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSystemMessage(Component.translatable("tt20.command.main.version", TT20Forged.VERSION));
        Component enabledText = Component.translatable(TT20Forged.config.enabled() ? "tt20.status.on" : "tt20.status.off");
        source.sendSystemMessage(Component.translatable("tt20.command.main.enabled", enabledText));
        return 1;
    }

    private static int executeTps(CommandContext<CommandSourceStack> context) {
        return MainCommand.executeTps(context, true);
    }

    private static int executeTps(CommandContext<CommandSourceStack> context, boolean missedTicks) {
        CommandSourceStack source = context.getSource();
        source.sendSystemMessage(Component.translatable("tt20.command.tps.line",
                TPSUtil.colorizeTPS(TT20Forged.TPS_CALCULATOR.getTPS(), true),
                TPSUtil.colorizeTPS(TT20Forged.TPS_CALCULATOR.getAverageTPS(), true),
                TPSUtil.colorizeTPS(TT20Forged.TPS_CALCULATOR.getMostAccurateTPS(), true)
        ));

        if (missedTicks) {
            source.sendSystemMessage(Component.translatable("tt20.command.tps.missed",
                    TPSUtil.formatMissedTicks(TT20Forged.TPS_CALCULATOR.getAllMissedTicks())
            ));
        }
        return 1;
    }

    private static int executeStatus(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSystemMessage(Component.translatable("tt20.command.status.header"));

        for (var option : MainConfig.getBooleanOptions()) {
            String key = option.getKey();
            boolean value = TT20Forged.config.getBoolean(key);

            String[] words = key.replace('-', ' ').split(" ");
            List<String> capitalizedWords = new ArrayList<>();
            for (String word : words) {
                if (word.length() > 0) {
                    capitalizedWords.add(Character.toUpperCase(word.charAt(0)) + word.substring(1));
                }
            }
            String formattedName = String.join(" ", capitalizedWords);
            Component statusText = Component.translatable(value ? "tt20.status.on" : "tt20.status.off");

            source.sendSystemMessage(Component.translatable("tt20.command.status.line", formattedName, statusText));
        }

        source.sendSystemMessage(Component.translatable("tt20.command.status.footer"));

        executeTps(context, false);
        source.sendSystemMessage(Component.translatable("tt20.command.status.version", TT20Forged.VERSION));
        source.sendSystemMessage(Component.translatable("tt20.command.status.mspt", TT20Forged.TPS_CALCULATOR.getMSPT()));
        source.sendSystemMessage(Component.translatable("tt20.command.tps.missed", TPSUtil.formatMissedTicks(TT20Forged.TPS_CALCULATOR.getAllMissedTicks())));

        return 1;
    }

    private static int executeToggle(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String key = StringArgumentType.getString(context, "key");

        if (MainConfig.getBooleanOptions().stream().noneMatch(o -> o.getKey().equals(key))) {
            source.sendFailure(Component.translatable("tt20.command.toggle.unknown_key", key));
            return 0;
        }

        boolean currentValue = TT20Forged.config.getBoolean(key);
        boolean newValue = !currentValue;
        TT20Forged.config.setBoolean(key, newValue);
        TT20Forged.config.save();

        Component enabledText = Component.translatable(newValue ? "tt20.status.enabled" : "tt20.status.disabled");
        source.sendSystemMessage(Component.translatable("tt20.command.toggle.feedback", key, enabledText));
        return 1;
    }

    private static int executeReload(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        TT20Forged.config.reload();
        source.sendSystemMessage(Component.translatable("tt20.command.reload.config"));
        TT20Forged.blockEntityMaskConfig.reload();
        source.sendSystemMessage(Component.translatable("tt20.command.reload.mask_config"));
        return 1;
    }
}
