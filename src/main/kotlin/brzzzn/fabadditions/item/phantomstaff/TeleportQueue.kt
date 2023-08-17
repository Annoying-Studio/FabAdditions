package brzzzn.fabadditions.item.phantomstaff

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.data.PlayerRef
import org.joda.time.DateTime
import org.joda.time.Duration

object TeleportQueue {
    // TODO: Make configurable
    val timeout = Duration.standardSeconds(10)

    /**
     * Queue for requests:
     *
     * Consisting of:
     * Key: [PlayerRef] of the targetPlayer
     * Value: Set of [Pair]s that consist of a requesting [PlayerRef] and a [DateTime] when the request was enqueued
     */
    private val queue: HashMap<PlayerRef, HashSet<Pair<PlayerRef, DateTime>>> = hashMapOf()

    fun enqueueRequest(requestingPlayer: PlayerRef, targetPlayer: PlayerRef) {
        cleanupQueue()
        queue.computeIfAbsent(targetPlayer) { hashSetOf() }.let { requestQueue ->
            if (requestQueue.none { it.first == requestingPlayer }) {
                requestQueue.add(Pair(requestingPlayer, DateTime.now()))
            } else {
                FabAdditions.logger.info("Player: ${requestingPlayer.name} tried to teleport to player: ${targetPlayer.name} multiple times within: ${timeout.standardSeconds} seconds")
            }
        }
    }

    /**
     * Gets the next valid player request from the queue
     */
    fun getNextValidRequestTarget(target: PlayerRef): PlayerRef? {
        cleanupQueue()
        val fetchedSource = queue[target]?.minByOrNull {
            it.second // Sorted by timestamp
        }?.first

        // Remove fetched player from queue
        fetchedSource?.let { sourcePlayer ->
            queue[target]?.removeIf {
                it.first == sourcePlayer
            }
        }

        return fetchedSource
    }

    /**
     * Removes all outdated Requests
     */
    private fun cleanupQueue() {
        FabAdditions.logger.info("Current Queue: $queue")
        queue.forEach { target ->
            target.value.removeIf { enqueuedRequest ->
                enqueuedRequest.second
                    .plus(timeout)
                    .isBeforeNow // Old time + timeout is older than current time -> remove from queue
            }
        }
    }
}