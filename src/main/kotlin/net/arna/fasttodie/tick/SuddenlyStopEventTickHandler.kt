package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import java.time.Duration

object SuddenlyStopEventTickHandler : Runnable
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    private var countDown: Int = 20 * 5
    private var stopCount: Int = 20 * 5


    override fun run()
    {
        if (countDown % 20 == 0 && countDown != 0)
        {
            for (player in gameSession.inGamePlayers!!)
            {
                player.player.sendTitlePart(TitlePart.TITLE, Component.text(countDown / 20))
                player.player.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(1000),
                        Duration.ZERO,
                    ))
            }
        }

        if (countDown == 0)
        {
            gameSession.playerMovementLimit = 0.0
            gameSession.playerRotationLimit = 0

            if (stopCount == 20 * 5)
            {
                for (player in gameSession.inGamePlayers!!)
                {
                    player.player.sendTitlePart(TitlePart.TITLE, Component.text("5秒間動くな！！"))
                    player.player.sendTitlePart(
                        TitlePart.TIMES,
                        Title.Times.times(
                            Duration.ZERO,
                            Duration.ofMillis(5000),
                            Duration.ZERO,
                        ))

                    gameSession.bossBar = BossBar.bossBar(
                        Component.text("動くと死んでしまう"),
                        stopCount / 100f,
                        BossBar.Color.RED,
                        BossBar.Overlay.PROGRESS)

                    player.player.showBossBar(gameSession.bossBar!!)
                }
            }

            gameSession.bossBar!!.progress(stopCount / 100f)

            if (stopCount == 0)
            {
                for (player in gameSession.inGamePlayers!!)
                {
                    player.player.hideBossBar(gameSession.bossBar!!)
                }

                gameSession.playerMovementLimit = 1.3
                gameSession.playerRotationLimit = 150
                plugin.unregisterTickHandler(gameSession.eventTaskId)
                gameSession.eventTick = 0
                countDown = 20 * 5
                stopCount = 20 * 5
                gameSession.bossBar = null
            }
            else
            {
                stopCount--
            }
        }
        else
        {
            countDown--
        }
    }
}