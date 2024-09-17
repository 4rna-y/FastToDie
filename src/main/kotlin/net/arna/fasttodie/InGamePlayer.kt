package net.arna.fasttodie

import net.kyori.adventure.bossbar.BossBar
import org.bukkit.Location
import org.bukkit.entity.Player

class InGamePlayer(val player: Player)
{
    var lastRotation: PitchYawRotation? = null
    var lastLocation: Location? = null
    var continuationLocationTick: Int = 0
    var coolTimeTick: Int = 0
    var deathType = 0
    var boostCharge = 0
    var boostJumping = false
    var invincible = false
    var goaled = false
    var place = 0
    var bossBar: BossBar? = null
}