package kz.busjol.data.remote

class RestorePasswordPost(
    val password: String,
    val secretCode: String,
    val loginInfo: String
)