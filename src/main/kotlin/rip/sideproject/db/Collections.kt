package rip.sideproject.db

import com.arangodb.model.FulltextIndexOptions
import com.arangodb.model.PersistentIndexOptions

fun collections() = listOf(
    Comment::class.db {
        ensurePersistentIndex(listOf("created"), PersistentIndexOptions())
    },
    Project::class.db {
        ensurePersistentIndex(listOf("created"), PersistentIndexOptions())
        ensurePersistentIndex(listOf("name"), PersistentIndexOptions())
        ensurePersistentIndex(listOf("purpose"), PersistentIndexOptions())
        ensurePersistentIndex(listOf("link"), PersistentIndexOptions())
    }
)
