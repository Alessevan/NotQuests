package notquests.notquests.Structs.Objectives;

import notquests.notquests.NotQuests;
import notquests.notquests.Structs.Quest;
import org.bukkit.inventory.ItemStack;

public class DeliverItemsObjective extends Objective {

    private final NotQuests main;
    private final ItemStack itemToDeliver;
    private final int amountToDeliver;
    private final int recipientNPCID;

    public DeliverItemsObjective(NotQuests main, final Quest quest, final int objectiveID, final ItemStack itemToDeliver, final int amountToDeliver, final int recipientNPCID) {
        super(main, quest, objectiveID, ObjectiveType.DeliverItems, amountToDeliver);
        this.main = main;
        this.itemToDeliver = itemToDeliver;
        this.amountToDeliver = amountToDeliver;
        this.recipientNPCID = recipientNPCID;
    }

    public final ItemStack getItemToDeliver() {
        return itemToDeliver;
    }

    public final int getAmountToDeliver() {
        return amountToDeliver;
    }

    public final int getRecipientNPCID() {
        return recipientNPCID;
    }


}