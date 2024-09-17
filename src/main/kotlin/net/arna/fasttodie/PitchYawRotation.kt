package net.arna.fasttodie

import kotlin.math.pow
import kotlin.math.sqrt

class PitchYawRotation(val pitch: Float, val yaw: Float)
{
    fun delta(other: PitchYawRotation): Float
    {
        val dp = pitch - other.pitch
        val dy = yaw - other.yaw
        val dpSq = dp.pow(2)
        val dySq = dy.pow(2)

        return sqrt(dpSq + dySq)
    }
}