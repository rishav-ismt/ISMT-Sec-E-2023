package np.com.rishavchudal.ismt_sec_e.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import np.com.rishavchudal.ismt_sec_e.LoginActivity
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.databinding.ActivityHomeBinding
import np.com.rishavchudal.ismt_sec_e.signup.SignUpActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewBinding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}