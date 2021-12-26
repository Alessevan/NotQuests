/*
 * NotQuests - A Questing plugin for Minecraft Servers
 * Copyright (C) 2021 Alessio Gravili
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package rocks.gravili.notquests.paper.structs.actions;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import rocks.gravili.notquests.paper.NotQuests;
import rocks.gravili.notquests.paper.commands.arguments.MiniMessageSelector;
import rocks.gravili.notquests.paper.structs.Quest;


public class SendMessageAction extends Action {

    private String messageToSend = "";


    public SendMessageAction(final NotQuests main) {
        super(main);
    }

    public static void handleCommands(NotQuests main, PaperCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder, ActionFor actionFor) {
        manager.command(builder.literal("SendMessage")
                .argument(MiniMessageSelector.<CommandSender>newBuilder("Sending Message", main).withPlaceholders().build(), ArgumentDescription.of("Message to broadcast"))
                .meta(CommandMeta.DESCRIPTION, "Creates a new SendMessage Action")
                .handler((context) -> {
                    final String messageToSend = String.join(" ", (String[]) context.get("Sending Message"));

                    SendMessageAction sendMessageAction = new SendMessageAction(main);
                    sendMessageAction.setMessageToSend(messageToSend);

                    main.getActionManager().addAction(sendMessageAction, context);
                }));
    }

    public final String getMessageToSend() {
        return messageToSend;
    }

    public void setMessageToSend(final String messageToSend) {
        this.messageToSend = messageToSend;
    }


    @Override
    public void execute(final Player player, Object... objects) {
        if (getMessageToSend().isBlank()) {
            main.getLogManager().warn("Tried to execute SendMessage action with empty message.");
            return;
        }

        Quest quest = getQuest();
        if (quest == null && objects.length > 0) {
            for (Object object : objects) {
                if (object instanceof Quest quest1) {
                    quest = quest1;
                }
            }
        }

        String message = getMessageToSend().replace("{PLAYER}", player.getName()).replace("{PLAYERUUID}", player.getUniqueId().toString())
                .replace("{PLAYERX}", "" + player.getLocation().getX())
                .replace("{PLAYERY}", "" + player.getLocation().getY())
                .replace("{PLAYERZ}", "" + player.getLocation().getZ())
                .replace("{WORLD}", "" + player.getWorld().getName());
        if(quest != null){
            message = message.replace("{QUEST}", "" + quest.getQuestName());
        }

        player.sendMessage(main.parse(
                message
        ));
    }

    @Override
    public void save(FileConfiguration configuration, String initialPath) {
        configuration.set(initialPath + ".specifics.message", getMessageToSend());
    }

    @Override
    public void load(final FileConfiguration configuration, String initialPath) {
        this.messageToSend = configuration.getString(initialPath + ".specifics.message", "");
    }


    @Override
    public String getActionDescription() {
        return "Sends Message: " + getMessageToSend();
    }
}
