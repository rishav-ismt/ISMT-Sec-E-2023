package np.com.rishavchudal.ismt_sec_e

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class DashboardActivity : AppCompatActivity() {
    private val tag = "DashboardActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val receivedIntent = intent

        val receivedLoginData = receivedIntent
            .getParcelableExtra<User>(AppConstants.KEY_LOGIN_DATA)

        Log.i(tag, "Received Email ::: "
            .plus(receivedLoginData?.email))
        Log.i(tag, "Received Password ::: "
            .plus(receivedLoginData?.password))
    }
}