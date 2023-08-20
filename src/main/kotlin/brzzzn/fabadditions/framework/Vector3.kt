package brzzzn.fabadditions.framework

import net.minecraft.util.math.Vec3d

data class Vector3(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    constructor(vec3d: Vec3d) : this(vec3d.x, vec3d.y, vec3d.z)
}