package brzzzn.fabadditions.data

import brzzzn.fabadditions.framework.Vector2
import brzzzn.fabadditions.framework.Vector3
import brzzzn.fabadditions.framework.WorldRef
import java.util.UUID

data class WarpPosition (
    val position: Vector3,
    val rotation: Vector2,
    val name: String,
    val worldIdentifier: WorldRef,
    val uniqueId: String = UUID.randomUUID().toString()
)