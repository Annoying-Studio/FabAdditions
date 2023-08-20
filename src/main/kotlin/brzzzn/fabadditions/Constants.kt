package brzzzn.fabadditions

import net.minecraft.util.Identifier

object Constants {
    object NetworkChannel {
        private const val NETWORK_CHANNEL_BASE = "network_channel"
        object PhantomStaff {
            private const val PHANTOM_STAFF_CHANNEL = "$NETWORK_CHANNEL_BASE.phantom_staff"

            val S2C_ITEM_USAGE_PACKET_ID = Identifier("$PHANTOM_STAFF_CHANNEL.item_usage_s2c")
            val C2S_TARGET_PLAYER_SELECTED_PACKET_ID = Identifier("$NETWORK_CHANNEL_BASE.target_player_selected_c2s")
        }

        object Warp {
            private const val WARP_CHANNEL = "$NETWORK_CHANNEL_BASE.warp"

            val S2C_STAFF_ITEM_USAGE_PACKET_ID = Identifier("$WARP_CHANNEL.staff_usage_s2c")

            val C2S_WARP_REQUEST_PACKET_ID = Identifier("$WARP_CHANNEL.warp_request_c2s")
            val C2S_WARP_ADD_PACKET_ID = Identifier("$WARP_CHANNEL.warp_add_c2s")
            val C2S_WARP_DELETE_PACKET_ID = Identifier("$WARP_CHANNEL.warp_delete_c2s")
        }
    }
}