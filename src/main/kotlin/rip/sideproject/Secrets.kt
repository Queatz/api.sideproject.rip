package rip.sideproject

import com.google.gson.Gson
import java.io.File

class Secrets {
    val config: SecretsConfig? by lazy {
        Gson().fromJson(File("./secrets.json").reader(), SecretsConfig::class.java)
    }
}

data class SecretsConfig(
    val ssl: SslSecretsConfig?
)

data class SslSecretsConfig(
    val keyStorePath: String,
    val keyAlias: String,
    val keyStorePassword: String,
    val privateKeyPassword: String,
)
