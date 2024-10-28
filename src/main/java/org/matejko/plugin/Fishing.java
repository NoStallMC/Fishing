package main.java.org.matejko.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Fishing extends JavaPlugin implements Listener {

    private Logger logger;  // Declare the logger field
    private Map<Integer, Integer> itemChances = new HashMap<>();  // Map to store item ID and their chances

    @Override
    public void onEnable() {
        // Initialize the logger field
        this.logger = Logger.getLogger("FishingPlugin");

        // Log a message when the plugin is enabled
        this.logger.info("Fishing Plugin is now enabled!");

        // Register the plugin as an event listener
        getServer().getPluginManager().registerEvents(this, this);

        // Set item chances (only Sponge now)
        setItemChances();
    }

    @Override
    public void onDisable() {
        // Log a message when the plugin is disabled
        this.logger.info("Fishing Plugin is now disabled!");
    }

    /**
     * Hard-code the item chances (only Sponge, removed Apple)
     */
    private void setItemChances() {
        // Hard-coded values
        itemChances.put(19, 50);  // Sponge (ID 19) with a 5% chance
        this.logger.info("Item chances have been set.");
    }

    /**
     * Handle the fishing event.
     */
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            // Log that the fishing event is triggered
            this.logger.info("Fishing event triggered!");

            // Normal fishing logic
            Random random = new Random();
            for (Map.Entry<Integer, Integer> entry : itemChances.entrySet()) {
                int itemId = entry.getKey();
                int chance = entry.getValue();

                // Generate a random number between 0 and 999
                int randomNumber = random.nextInt(1000);

                // If the random number is less than the chance, give the item to the player
                if (randomNumber < chance) {
                    // Create the item using the item ID
                    ItemStack itemStack = new ItemStack(itemId, 1);

                    // Remove the caught fish and give the item
                    event.getCaught().remove();
                    event.getPlayer().getInventory().addItem(itemStack);

                    // Force an update to the player's inventory to show the new item immediately
                    updatePlayerInventory(event.getPlayer());

                    // Log the event
                    this.logger.info("Player " + event.getPlayer().getName() + " caught item with ID: " + itemId);
                    break;
                }
            }
        }
    }

    private void updatePlayerInventory(org.bukkit.entity.Player player) {
        // Update the player's inventory by sending the updated packet to the client
        player.updateInventory();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fishing")) {
            sender.sendMessage("Fishing Plugin is active and working!");
            return true;
        }
        return false;
    }
}
