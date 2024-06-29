package brzzzn.fabadditions

import io.netty.buffer.ByteBuf


fun ByteBuf.toByteArray(): ByteArray {
    return this.array()
}