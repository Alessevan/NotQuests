package rocks.gravili.notquests.paper.structs.variables;

import cloud.commandframework.arguments.standard.StringArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rocks.gravili.notquests.paper.NotQuests;
import rocks.gravili.notquests.paper.structs.CompletedQuest;
import rocks.gravili.notquests.paper.structs.Quest;
import rocks.gravili.notquests.paper.structs.QuestPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * This variable is true if the amount of times the player has previously completed this Quest is equal or higher than the Quests max accepts
 */
public class QuestReachedMaxAcceptsVariable extends Variable<Boolean>{
    public QuestReachedMaxAcceptsVariable(NotQuests main) {
        super(main);
        addRequiredString(
                StringArgument.<CommandSender>newBuilder("Quest to check").withSuggestionsProvider(
                        (context, lastString) -> {
                            final List<String> allArgs = context.getRawInput();
                            main.getUtilManager().sendFancyCommandCompletion(context.getSender(), allArgs.toArray(new String[0]), "[Quest Name]", "[...]");

                            ArrayList<String> suggestions = new ArrayList<>();
                            for(Quest quest : main.getQuestManager().getAllQuests()){
                                suggestions.add(quest.getQuestName());
                            }
                            return suggestions;

                        }
                ).single().build()
        );
    }

    @Override
    public Boolean getValue(Player player, Object... objects) {
        final Quest quest = main.getQuestManager().getQuest(getRequiredStringValue("Quest to check"));
        final QuestPlayer questPlayer = main.getQuestPlayerManager().getQuestPlayer(player.getUniqueId());

        if(quest == null || questPlayer == null){
            return false;
        }

        if(quest.getMaxAccepts() <= -1){
            return false;
        }else if(quest.getMaxAccepts() == 0){
            return true;
        }

        int completedAmount = 0; //only needed for maxAccepts

        for (CompletedQuest completedQuest : questPlayer.getCompletedQuests()) {
            if (completedQuest.getQuest().equals(quest)) {
                completedAmount += 1;
            }
        }

        if(completedAmount >= quest.getMaxAccepts()){
            return true;
        }

        return false;
    }

    @Override
    public boolean setValueInternally(Boolean newValue, Player player, Object... objects) {
        return false;
    }


    @Override
    public List<String> getPossibleValues(Player player, Object... objects) {
        return null;
    }

    @Override
    public String getPlural() {
        return "Quest reached max accepts";
    }

    @Override
    public String getSingular() {
        return "Quest reached max accepts";
    }
}