package net.arna.fasttodie

import net.arna.fasttodie.command.FastToDieCommandHandler
import net.arna.fasttodie.listener.ExplosionEventListener
import net.arna.fasttodie.listener.PlayerDeadEventListener
import net.arna.fasttodie.listener.PlayerJoinEventListener
import net.arna.fasttodie.listener.PlayerMoveEventListener
import net.arna.fasttodie.tick.BoostJumpItemTickHandler
import net.arna.fasttodie.tick.PlayerMovementTickHandler
import net.arna.fasttodie.tick.ScheduledEventTickHandler
import org.bukkit.plugin.java.JavaPlugin

class FastToDie : JavaPlugin()
{
    companion object
    {
        var gameSession: GameSession? = null
            private set

        var javaPlugin: FastToDie? = null
            private set
    }

    override fun onEnable()
    {
        javaPlugin = this
        gameSession = GameSession(this)

        registerEventListeners()
        registerTickHandlers()
        registerCommandHandlers()
        setupConfig()
    }

    override fun onDisable()
    {

    }

    fun registerTickHandler(runnable: Runnable) : Int
    {
        return server.scheduler.scheduleSyncRepeatingTask(this, runnable, 0, 1)
    }

    fun unregisterTickHandler(id: Int)
    {
        server.scheduler.cancelTask(id)
    }

    private fun registerEventListeners()
    {
        server.pluginManager.registerEvents(PlayerJoinEventListener, this)
        server.pluginManager.registerEvents(PlayerDeadEventListener, this)
        server.pluginManager.registerEvents(ExplosionEventListener, this)
        server.pluginManager.registerEvents(PlayerMoveEventListener, this)
    }

    private fun registerTickHandlers()
    {
        server.scheduler.scheduleSyncRepeatingTask(this, PlayerMovementTickHandler, 0, 1)
        server.scheduler.scheduleSyncRepeatingTask(this, ScheduledEventTickHandler, 0, 1)
        server.scheduler.scheduleSyncRepeatingTask(this, BoostJumpItemTickHandler, 0, 1)
    }

    private fun registerCommandHandlers()
    {
        getCommand("ftd")?.setExecutor(FastToDieCommandHandler)
    }

    private fun setupConfig()
    {
        config.addDefault("location-delta", 1.3)
        config.addDefault("rotation-delta", 150)
        config.addDefault("rotation-immediate", 500)
        config.addDefault("location-blind-tick", 5)
        config.addDefault("location-damage-tick", 20)
        config.addDefault("event-interval-tick", 20 * 60 * 3)
        config.addDefault("boost-jump-vector-multiply-value", 3.5)
        config.addDefault("boost-jump-vector-y-value", 1.5)
        config.addDefault("pvp-center", arrayListOf(0, 0, 0))
        config.addDefault("pvp-radius", 5)

        config.options().copyDefaults(true)
        saveConfig()
    }
}
