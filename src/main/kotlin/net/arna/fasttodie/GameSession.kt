package net.arna.fasttodie

import net.arna.fasttodie.tick.PvpEventTickHandler
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound
import java.time.Duration
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class GameSession(javaPlugin: FastToDie)
{
    private val plugin = javaPlugin

    var isStarted: Boolean = false
        private set

    var inGamePlayers: MutableList<InGamePlayer>? = null
        private set

    var playerMovementLimit = plugin.config.getDouble("location-delta")
    var playerRotationLimit = plugin.config.getInt("rotation-delta")

    var eventTick: Int = 0
    var inPvping = false
    var eventTaskId: Int = 0

    var bossBar: BossBar? = null

    var isInvincible = false
    var isMoveable = false
    var goaledPlayer: MutableList<InGamePlayer>? = null

    fun preStart()
    {
        inGamePlayers = mutableListOf()
        goaledPlayer = mutableListOf()
        for (player in plugin.server.onlinePlayers)
        {
            inGamePlayers!!.add(InGamePlayer(player))
        }
    }

    fun start(): Result
    {
        if (isStarted) return Result("The game session has already been started.")

        eventTick = 0
        isStarted = true

        return Result()
    }

    fun end(): Result
    {
        if (!isStarted) return Result("The game session has not been started.")

        inGamePlayers = null
        isStarted = false

        return Result()
    }

    fun goal(p: InGamePlayer)
    {
        goaledPlayer!!.add(p)
        p.goaled = true
        p.place = goaledPlayer!!.size
        p.player.gameMode = GameMode.SPECTATOR
        p.invincible = true

        p.player.sendTitlePart(
            TitlePart.TITLE, Component.text(ChatColor.GOLD.toString() + "${p.place}位 !!"))
        p.player.sendTitlePart(
            TitlePart.TIMES, Title.Times.times(
                Duration.ofMillis(500),
                Duration.ofMillis(3000),
                Duration.ofMillis(500)
            ))
        p.player.playSound(p.player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)

        if (goaledPlayer!!.size == inGamePlayers!!.size)
        {
            inGamePlayers!!.forEach {
                it.player.sendTitlePart(
                    TitlePart.TITLE,
                    Component.text(ChatColor.YELLOW.toString() + "ゲーム終了！！")
                )
                it.player.sendTitlePart(
                    TitlePart.SUBTITLE,
                    Component.text("あなたは${ChatColor.BOLD}${it.place}位${ChatColor.WHITE}でゴールした")
                )
                it.player.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofMillis(5000),
                        Duration.ofMillis(500)
                    )
                )
                it.player.playSound(it.player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f)
            }

            isStarted = false
        }
    }

    fun startPvp()
    {
        isInvincible = true
        inPvping = true

        arrangePlayerLocation()
        isMoveable = true

        inGamePlayers!!.forEach {
            it.player.sendTitlePart(
                TitlePart.TITLE,
                Component.text(
                    ChatColor.GOLD.toString() + "逆転チャンス！！"))
            it.player.sendTitlePart(
                TitlePart.SUBTITLE, Component.text("PVPに勝利してジャンプ棒を獲得しよう"))
            it.player.sendTitlePart(
                TitlePart.TIMES,
                Title.Times.times(
                    Duration.ofMillis(500),
                    Duration.ofMillis(2000),
                    Duration.ofMillis(500)))
        }

        eventTaskId = plugin.registerTickHandler(PvpEventTickHandler)
    }

    private fun arrangePlayerLocation()
    {
        if (!isStarted) return
        val center = plugin.config.getList("pvp-center")!!
        val radius = plugin.config.getInt("pvp-radius")
        val step = 2 * PI / inGamePlayers!!.size

        inGamePlayers!!.forEachIndexed { idx, player ->
            val angle = idx * step
            val x = center[0] as Double + radius * cos(angle)
            val y = center[1] as Double
            val z = center[2] as Double + radius * sin(angle)
            val yaw = Math.toDegrees(atan2(center[2] as Double - z, center[0] as Double - x)) - 90

            player.player.teleport(
                Location(player.player.location.world, x, y, z, yaw.toFloat(), 0f))

        }


    }
}