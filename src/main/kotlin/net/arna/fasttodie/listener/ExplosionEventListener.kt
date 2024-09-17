package net.arna.fasttodie.listener

import net.arna.fasttodie.FastToDie
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

object ExplosionEventListener : Listener
{
    @EventHandler
    fun onEntityExplode(e: EntityExplodeEvent)
    {
        if (e.entity is TNTPrimed && FastToDie.gameSession!!.isStarted)
        {
            e.blockList().clear()
        }
    }
}