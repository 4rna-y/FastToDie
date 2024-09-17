package net.arna.fasttodie.listener

import net.arna.fasttodie.FastToDie
import net.arna.fasttodie.InGamePlayer
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinEventListener : Listener
{
    @EventHandler
    fun onJoin(e : PlayerJoinEvent)
    {
        val gameSession = FastToDie.gameSession!!
        if (!gameSession.isStarted) return

        val player = gameSession.inGamePlayers!!.find { it.player == e.player }
        if (player == null)
        {
            e.player.gameMode = GameMode.SPECTATOR
        }
    }
}