package net.arna.fasttodie.listener

import net.arna.fasttodie.FastToDie
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent

object PlayerMoveEventListener : Listener
{
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent)
    {
        val gameSession = FastToDie.gameSession!!
        if (!gameSession.isStarted) return
        val player = gameSession.inGamePlayers!!.find { it.player == e.player }
        if (player == null) return

        if (gameSession.isMoveable)
        {
            e.isCancelled = true
            return
        }

        val bb = e.player.location.subtract(0.0, 1.0, 0.0).block
        if (e.from.y > e.to.y && !bb.isEmpty)
        {
            if (player.boostJumping)
            {
                player.invincible = false
                player.boostJumping = false

            }
        }
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent)
    {
        val gameSession = FastToDie.gameSession!!
        if (!gameSession.isStarted) return
        if (e.entity !is Player) return
        val p = e.entity as Player
        val player = gameSession.inGamePlayers!!.find { it.player == p }
        if (player == null) return

        if (player.boostJumping && e.cause == EntityDamageEvent.DamageCause.FALL)
        {
            e.isCancelled = true
        }

    }
}