package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

object FallingTntEventTickHandler : Runnable
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    var count = 20 * 20

    override fun run()
    {
        if (count == 20 * 20)
        {
            gameSession.isInvincible = true
            for (player in gameSession.inGamePlayers!!)
            {
                player.player.sendTitlePart(TitlePart.TITLE, Component.text("上から来るぞ！気をつけろ！！"))
                player.player.sendTitlePart(TitlePart.SUBTITLE, Component.text("走って逃げろ！！"))
                player.player.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ofMillis(200),
                        Duration.ofMillis(3000),
                        Duration.ofMillis(200),
                    ))

                gameSession.bossBar = BossBar.bossBar(
                    Component.text("上から来るぞ！気をつけろ！！"),
                    count / 400f,
                    BossBar.Color.GREEN,
                    BossBar.Overlay.PROGRESS)
                player.player.showBossBar(gameSession.bossBar!!)

                player.player.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.SLOW,
                        20 * 20,
                        2,
                        true,
                        false)
                )
            }
        }

        if (count % (20 * 4) == 0 && count != 0)
        {
            for (player in gameSession.inGamePlayers!!)
            {
                val location = player.player.location
                val spawnLocation = Location(
                    location.world, location.x, location.y + 5, location.z)

                val tnt = location.world.spawnEntity(spawnLocation, EntityType.PRIMED_TNT) as TNTPrimed
                tnt.fuseTicks = 40
            }
        }

        gameSession.bossBar!!.progress(count / 400f)

        if (count == 0)
        {
            gameSession.isInvincible = false
            for (player in gameSession.inGamePlayers!!)
            {
                player.player.hideBossBar(gameSession.bossBar!!)
            }

            plugin.unregisterTickHandler(gameSession.eventTaskId)
            gameSession.eventTick = 0
            count = 20 * 20
            gameSession.bossBar = null
        }
        else
        {
            count--
        }


    }
}