package net.arna.fasttodie.command

import net.arna.fasttodie.FastToDie
import net.arna.fasttodie.Result
import net.arna.fasttodie.tick.ScheduledEventTickHandler
import net.arna.fasttodie.tick.StartUpTickHandler
import net.arna.fasttodie.tick.SuddenlyStopEventTickHandler
import org.bukkit.Location
import org.bukkit.block.CommandBlock
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object FastToDieCommandHandler : TabExecutor
{
    private val plugin = FastToDie.javaPlugin!!
    private val gameSession = FastToDie.gameSession!!

    override fun onCommand(
        sender : CommandSender,
        command : Command,
        label: String,
        args: Array<out String>?)
    : Boolean
    {
        if (args?.size == 0) return false;
        if (args?.size == 1)
        {
            val option = args[0]
            if (option == "start")
            {
                gameSession.preStart()
                gameSession.eventTaskId = plugin.registerTickHandler(StartUpTickHandler)
            }
            else
            if (option == "end")
            {
                val result = FastToDie.gameSession?.end()
                if (result!!.isSuccess) return true
                sender.sendMessage(result.message)
            }
            else
            if (option == "pvp")
            {
                gameSession.startPvp()
            }
            else
            if (option == "center")
            {
                val loc = (sender as Player).location
                plugin.config.set("pvp-center", arrayListOf(loc.x, loc.y, loc.z))
                plugin.saveConfig()
                sender.sendMessage("set center: $loc")
            }
            else
            if (option == "radius")
            {
                val center = plugin.config.getList("pvp-center")!!
                val loc = (sender as Player).location

                val radius = loc.distance(Location(
                    loc.world,
                    center[0] as Double,
                    center[1] as Double,
                    center[2] as Double))

                plugin.config.set("pvp-radius", radius)
                plugin.saveConfig()
                sender.sendMessage("set radius: $radius")
            }
            else
            if (option == "goal")
            {
                if (!gameSession.isStarted) return false
                if (sender is BlockCommandSender)
                {
                    val loc = sender.block.location
                    val np = gameSession.inGamePlayers!!
                        .minByOrNull { it.player.location.distance(loc) }

                    if (np == null) return false

                    gameSession.goal(np)
                }

            }
        }

        return true;
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String>
    {
        if (args?.size == 1)
        {
            return arrayListOf("start", "end", "pvp", "center", "radius", "goal")
        }

        return arrayListOf()
    }
}