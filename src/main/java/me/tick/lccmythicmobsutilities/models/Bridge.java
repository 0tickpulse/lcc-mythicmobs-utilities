package me.tick.lccmythicmobsutilities.models;

import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;

/**
 * An interface that represents a bridge between the plugin and another.
 * Classes that implement this interface should usually be registered via {@link LccMythicmobsUtilities#registerBridge(Bridge)}.
 * @author 0TickPulse
 */
public interface Bridge {
    /**
     * A method that returns a boolean based on whether the bridge can be enabled or not.
     * Typically, this method should check if the plugin that the bridge is for is enabled/exists on the server.
     */
    boolean canEnable();
    /**
     * A method that is called when the bridge is enabled.
     * You do not need to send a message to the console or log anything, as this is done automatically.
     */
    void start();
}
