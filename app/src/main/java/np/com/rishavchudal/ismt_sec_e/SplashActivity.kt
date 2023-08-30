package np.com.rishavchudal.ismt_sec_e

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import np.com.rishavchudal.ismt_sec_e.dashboard.DashboardActivity
import np.com.rishavchudal.ismt_sec_e.home.HomeActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(
            {
            //Code to be executed after the below delay time

            //Read from shared preferences
            val sharedPreferences = this.getSharedPreferences(
                AppConstants.FILE_SHARED_PREF_LOGIN,
                Context.MODE_PRIVATE
            )
                val defaultLoginState = false
                val isAlreadyLoggedIn = sharedPreferences.getBoolean(
                    AppConstants.KEY_IS_LOGGED_IN,
                    defaultLoginState
                )

                val intent: Intent
                if (isAlreadyLoggedIn) {
                    intent = Intent(
                        this,
                        DashboardActivity::class.java
                    )
                } else {
                    intent = Intent(
                        this,
                        HomeActivity::class.java
                    )
                }
                startActivity(intent)
                finish()
            },
            3000
        )
    }
}