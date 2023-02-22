package brzzzn.fabadditions

import net.minecraft.util.Identifier

object Constants {
    object NetworkChannel {
        private const val NETWORK_CHANNEL_BASE = "network_channel"
        val PHANTOM_STAFF_S2C_PACKET_ID = Identifier("$NETWORK_CHANNEL_BASE.phantom_staff_s2c")
        val PHANTOM_STAFF_C2S_PACKET_ID = Identifier("$NETWORK_CHANNEL_BASE.phantom_staff_c2s")
    }
}