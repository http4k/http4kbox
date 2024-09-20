package env

import org.http4k.security.oauth.testing.FakeOAuthServer
import java.time.Clock

fun FakeGitHub(clock: Clock) = FakeOAuthServer("/login/oauth/authorize", "/login/oauth/access_token", clock)
