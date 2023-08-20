package brzzzn.fabadditions.framework

import net.minecraft.registry.RegistryKey
import net.minecraft.world.World

data class WorldRef(
    var env: String,
    val dimension: String
) {
    constructor(reg: RegistryKey<World>): this(reg.value.namespace, reg.value.path)
}
