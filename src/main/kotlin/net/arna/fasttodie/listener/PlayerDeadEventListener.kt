package net.arna.fasttodie.listener

import net.arna.fasttodie.FastToDie
import net.arna.fasttodie.PitchYawRotation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

object PlayerDeadEventListener : Listener
{
    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent)
    {
        val gameSession = FastToDie.gameSession!!
        if (gameSession.isStarted)
        {
            val player = gameSession.inGamePlayers!!.find { it.player == e.player }
            if (player == null) return
            player.coolTimeTick = 5
        }
    }

    @EventHandler
    fun onDead(e: PlayerDeathEvent)
    {
        val gameSession = FastToDie.gameSession!!
        if (gameSession.isStarted)
        {
            val player = gameSession.inGamePlayers!!.find { it.player == e.player }
            if (player == null) return

            if (gameSession.inPvping)
            {
                player.player.gameMode = GameMode.SPECTATOR
                e.isCancelled = true

                return
            }

            if (player.deathType == 0) return
            if (player.deathType == 1)
                e.deathMessage(Component.text("${e.player.name}は早く動きすぎた"))
            if (player.deathType == 2)
                e.deathMessage(Component.text("${e.player.name}は動いてしまった"))
            if (player.deathType == 3)
                e.deathMessage(Component.text("${e.player.name}の首の動きが早すぎた"))

            player.deathType = 0
        }
    }
}