package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.ChatColor
import org.bukkit.Sound
import java.time.Duration

object StartUpTickHandler : Runnable
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    var count = 20 * 3

    override fun run()
    {
        if (count % 20 == 0 && count != 0)
        {
            for (player in gameSession.inGamePlayers!!)
            {
                player.player.sendTitlePart(TitlePart.TITLE, Component.text(count / 20))
                player.player.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(1000),
                        Duration.ZERO,
                    ))
                player.player.playSound(player.player, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 2.0f, 1.0f)
            }
        }

        count--

        if (count == 0)
        {
            gameSession.start()
            for (player in gameSession.inGamePlayers!!)
            {
                player.player.sendTitlePart(TitlePart.TITLE,
                    Component.text(
                        ChatColor.YELLOW.toString() + "スタート！！"))
                player.player.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ofMillis(0),
                        Duration.ofMillis(4000),
                        Duration.ofMillis(1000),
                    ))
                player.player.playSound(player.player, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f)
            }
            plugin.unregisterTickHandler(gameSession.eventTaskId)
            gameSession.eventTick = 0
            count = 20 * 3
            gameSession.bossBar = null
        }


    }
}