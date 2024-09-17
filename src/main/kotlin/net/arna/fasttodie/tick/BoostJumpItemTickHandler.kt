package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack


object BoostJumpItemTickHandler : Runnable
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    override fun run()
    {
        if (!gameSession.isStarted) return
        for (player in gameSession.inGamePlayers!!)
        {
            val mainHandItem = player.player.inventory.itemInMainHand

            if (mainHandItem.type != Material.CARROT_ON_A_STICK)
            {
                player.boostCharge = 0
                return
            }

            if (!player.player.isSneaking)
            {
                if (player.boostCharge != 40)
                {
                    player.player.sendActionBar(
                        Component.text(
                            ChatColor.WHITE.toString() + "[Shift]" +
                                    ChatColor.GRAY.toString() + " : チャージする"))
                    player.boostCharge = 0
                    return
                }
                else
                {
                    val jmp = player.player.location.direction.multiply(
                        plugin.config.getDouble("boost-jump-vector-multiply-value"))
                    jmp.setY(plugin.config.getDouble("boost-jump-vector-y-value"))

                    player.player.velocity = jmp
                    player.player.playSound(player.player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)

                    player.player.sendActionBar(Component.text(" "))
                    player.boostCharge = 0
                    player.player.inventory.setItemInMainHand(ItemStack(Material.AIR))
                    player.boostJumping = true
                    player.invincible = true
                    return
                }
            }

            if (player.boostCharge != 40)
            {
                player.boostCharge++
            }

            val actionBar = "[" +
                    (if (player.boostCharge != 40) ChatColor.RED else ChatColor.GREEN) +
                    "■".repeat(player.boostCharge / 4) +
                    ChatColor.GRAY + "■".repeat(10 - (player.boostCharge / 4)) +
                    ChatColor.WHITE +
                        if (player.boostCharge == 40) "] : チャージ完了" else "] : チャージ中"

            player.player.sendActionBar(Component.text(actionBar))

        }
    }
}