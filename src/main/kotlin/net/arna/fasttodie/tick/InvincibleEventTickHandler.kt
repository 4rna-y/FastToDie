package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import java.time.Duration

object InvincibleEventTickHandler : Runnable
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    private var count = 20 * 10

    override fun run()
    {
        if (count == 20 * 10)
        {
            gameSession.isInvincible = true
            for (player in gameSession.inGamePlayers!!)
            {
                player.player.sendTitlePart(TitlePart.TITLE, Component.text("10秒間無敵タイム！！"))
                player.player.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofMillis(5000),
                        Duration.ofMillis(500),
                    ))

                player.bossBar = BossBar.bossBar(
                    Component.text("無敵タイム"),
                    count / 200f,
                    BossBar.Color.GREEN,
                    BossBar.Overlay.PROGRESS)
                player.player.showBossBar(player.bossBar!!)
            }
        }

        gameSession.inGamePlayers!!.forEach {
            it.bossBar!!.progress(count / 200f)
        }

        count--

        if (count == 0)
        {
            gameSession.isInvincible = false
            for (player in gameSession.inGamePlayers!!)
            {
                player.player.hideBossBar(player.bossBar!!)
                player.bossBar = null
            }

            plugin.unregisterTickHandler(gameSession.eventTaskId)
            gameSession.eventTick = 0
            count = 20 * 10
            gameSession.bossBar = null
        }
    }

}