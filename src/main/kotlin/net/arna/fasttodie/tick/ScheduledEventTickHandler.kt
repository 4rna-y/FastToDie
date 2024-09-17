package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import kotlin.random.Random

object ScheduledEventTickHandler : Runnable
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    override fun run()
    {
        if (!gameSession.isStarted) return
        if (gameSession.inPvping) return

        gameSession.eventTick++
        if (gameSession.eventTick == plugin.config.getInt("event-interval-tick"))
        {
            val rnd = Random.nextInt(0, 3)

            when (rnd)
            {
                0 -> gameSession.eventTaskId = plugin.registerTickHandler(SuddenlyStopEventTickHandler)
                1 -> gameSession.eventTaskId = plugin.registerTickHandler(InvincibleEventTickHandler)
                2 -> gameSession.eventTaskId = plugin.registerTickHandler(FallingTntEventTickHandler)
            }
        }

    }
}