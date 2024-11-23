package ir.hadiagdamapps.blackboxchat

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class MessengerApp : Application() {

    companion object {
        var queue: RequestQueue? = null
    }

    override fun onCreate() {
        super.onCreate()
        setupBouncyCastle()
        queue = Volley.newRequestQueue(this)
    }

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) ?: return
        if (provider::class.java == BouncyCastleProvider::class.java) {
            return
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

    override fun onTerminate() {
        super.onTerminate()
        queue?.cancelAll { true }
        queue = null
    }
}