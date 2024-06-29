package brzzzn.fabadditions.item.phantomstaff.packets

import brzzzn.fabadditions.Constants
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class PhantomStaffItemUsagePayload(
    var players: String? = null
) : CustomPayload {

    override fun getId(): CustomPayload.Id<PhantomStaffItemUsagePayload> = ID

    companion object {
        val CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, PhantomStaffItemUsagePayload::players,
        ) {
            PhantomStaffItemUsagePayload()
        }
        val ID = CustomPayload.Id<PhantomStaffItemUsagePayload>(Constants.NetworkChannel.PhantomStaff.S2C_ITEM_USAGE_PACKET_ID)
    }
}