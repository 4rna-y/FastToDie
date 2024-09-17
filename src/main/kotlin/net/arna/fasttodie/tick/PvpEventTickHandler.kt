package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.*
import org.bukkit.inventory.ItemStack
import java.time.Duration

object PvpEventTickHandler : Runnable
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    private var count = 120

    override fun run()
    {
        if (!gameSession.isStarted) return

        if (count <= 20 * 3)
        {
            if (count % 20 == 0)
            {
                gameSession.pvpPlayer!!.forEach {
                    it.player.sendTitlePart(
                        TitlePart.TITLE, Component.text(count / 20))
                    it.player.sendTitlePart(
                        TitlePart.TIMES,
                        Title.Times.times(
                            Duration.ZERO,
                            Duration.ofMillis(1000),
                            Duration.ZERO))
                    it.player.playSound(it.player, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 2.0f, 1.0f)
                }
            }
        }

        if (count != 0 && count != -1) count--
        else
        {
            if (count == 0)
            {
                gameSession.pvpPlayer!!.forEach {
                    it.player.sendTitlePart(
                        TitlePart.TITLE, Component.text(
                            ChatColor.GOLD.toString() + "スタート！！"))
                    it.player.sendTitlePart(
                        TitlePart.TIMES,
                        Title.Times.times(
                            Duration.ZERO,
                            Duration.ofMillis(1000),
                            Duration.ZERO))
                    it.player.playSound(it.player, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f)
                }
                gameSession.isMoveable = false
                count = -1

            }

            if (gameSession.pvpPlayer!!.size == 1)
            {
                val first = gameSession.pvpPlayer!!.first()
                first.player.sendTitlePart(
                    TitlePart.TITLE, Component.text("勝利！！"))
                first.player.sendTitlePart(
                    TitlePart.TIMES, Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofMillis(2000),
                        Duration.ofMillis(500)))

                val item = ItemStack(Material.CARROT_ON_A_STICK)
                item.itemMeta.displayName(
                    Component.text("ジャンプ棒").decorate(TextDecoration.BOLD))
                first.player.inventory.addItem(item)
                first.player.sendMessage("[Info] ジャンプ棒を付与しました")


                gameSession.inGamePlayers!!.forEach {
                    if (it != first)
                    {
                        it.player.sendTitlePart(
                            TitlePart.TITLE, Component.text("敗北..."))
                        it.player.sendTitlePart(
                            TitlePart.TIMES, Title.Times.times(
                                Duration.ofMillis(500),
                                Duration.ofMillis(2000),
                                Duration.ofMillis(500)))

                        val center = plugin.config.getList("pvp-center")!!
                        it.player.teleport(
                            Location(
                                it.player.location.world,
                                center[0] as Double,
                                center[1] as Double,
                                center[2] as Double))

                        it.player.gameMode = GameMode.SURVIVAL
                    }
                    it.coolTimeTick = 5
                    it.player.playSound(it.player, Sound.BLOCK_ANVIL_USE, 1f, 1f)
                }

                gameSession.inPvping = false
                gameSession.isInvincible = false
                plugin.unregisterTickHandler(gameSession.eventTaskId)
                count = 120
            }
        }
    }

}