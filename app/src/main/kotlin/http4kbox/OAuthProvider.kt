package http4kbox

import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.security.OAuthPersistence
import org.http4k.security.OAuthProvider
import org.http4k.security.OAuthProviderConfig

fun OAuthProvider(baseUri: Uri, serverUri: Uri, http: HttpHandler, oauth: OAuthPersistence, client: Credentials) =
    OAuthProvider(
        OAuthProviderConfig(baseUri, "/login/oauth/authorize", "/login/oauth/access_token", client, baseUri),
        http,
        serverUri,
        listOf(),
        oauth
    )