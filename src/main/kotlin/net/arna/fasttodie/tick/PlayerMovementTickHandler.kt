package net.arna.fasttodie.tick

import net.arna.fasttodie.FastToDie
import net.arna.fasttodie.GameSession
import net.arna.fasttodie.PitchYawRotation
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.roundToInt


//
// クラス名: ServerTickEventListener
//
// 概要: インゲームのプレイヤーの動き(座標移動、視点移動)を毎Tick監視し、
//　　　 一定条件で盲目or死亡相当ダメージを付与する。
//
object PlayerMovementTickHandler : Runnable
{
    private val plugin: JavaPlugin = FastToDie.javaPlugin!!
    private val gameSession: GameSession = FastToDie.gameSession!!

    override fun run()
    {
        if (!gameSession.isStarted) return

        for (player in gameSession.inGamePlayers!!)
        {
            val nowLoc = player.player.location
            val nowRot = PitchYawRotation(nowLoc.pitch, nowLoc.yaw)

            //リスポーン後、視点移動要因の即死を防ぐ
            if (player.coolTimeTick != 0)
            {
                player.coolTimeTick--
            }

            if (player.lastLocation != null)
            {
                val delta = ((nowLoc.distance(player.lastLocation!!) * 20) * 1000).roundToInt() / 1000.0

                if (delta > gameSession.playerMovementLimit &&
                    player.coolTimeTick == 0 &&
                    !gameSession.isInvincible &&
                    !player.invincible)
                {

                    if (gameSession.playerMovementLimit == 0.0)
                    {
                        player.deathType = 2
                        player.player.damage(4545.1919)
                    }

                    player.continuationLocationTick++
                    if (player.continuationLocationTick == plugin.config.getInt("location-blind-tick"))
                    {
                        player.player.addPotionEffect(
                            PotionEffect(
                                PotionEffectType.BLINDNESS,
                                20 * 3,
                                5,
                                true,
                                false))

                    }
                    else
                    if (player.continuationLocationTick > plugin.config.getInt("location-damage-tick"))
                    {
                        player.deathType = 1
                        player.player.damage(4545.1919)
                    }

                }
                else
                {
                    player.continuationLocationTick = 0
                }
            }

            if (player.lastRotation != null)
            {
                val delta = ((nowRot.delta(player.lastRotation!!) * 20) * 1000).roundToInt() / 1000.0

                if (delta > gameSession.playerRotationLimit &&
                    delta < plugin.config.getInt("rotation-immediate") &&
                    player.coolTimeTick == 0 &&
                    !gameSession.isInvincible &&
                    !player.invincible)
                {
                    if (gameSession.playerRotationLimit == 0)
                    {
                        player.deathType = 2
                        player.player.damage(4545.1919)
                    }

                    player.player.addPotionEffect(
                        PotionEffect(
                            PotionEffectType.BLINDNESS,
                            20 * 3,
                            5,
                            true,
                            false))
                }
                else
                if (delta > plugin.config.getInt("rotation-immediate") &&
                    player.coolTimeTick == 0 &&
                    !gameSession.isInvincible &&
                    !player.invincible)
                {
                    player.deathType = 3
                    player.player.damage(4545.1919)
                }
            }



            player.lastLocation = nowLoc
            player.lastRotation = nowRot
        }
    }

}