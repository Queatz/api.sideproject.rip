package rip.sideproject.db

import com.arangodb.entity.DocumentField
import com.google.gson.annotations.SerializedName
import java.time.Instant

open class Model {
    @DocumentField(DocumentField.Type.ID)
    @SerializedName("id", alternate = ["_id"])
    var id: String? = null
    var created: Instant = Instant.now()
}

class Project : Model() {
    var name: String = ""
    var link: String = ""
    var purpose: String = ""
}

class Comment : Model() {
    var text: String = ""
    var projectId: String = ""
}
