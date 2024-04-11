package brzzzn.fabadditions.quests.datastructure

data class Quest(
    val name: String,
    val description: String,
    val objectives: Set<Objective>,
)