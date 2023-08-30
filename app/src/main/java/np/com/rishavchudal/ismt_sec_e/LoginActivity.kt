package np.com.rishavchudal.ismt_sec_e

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import np.com.rishavchudal.ismt_sec_e.dashboard.DashboardActivity
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private val tag = "LoginActivity"
    private lateinit var loginButton: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.i(tag, "onCreate Called...")

        loginButton = findViewById(R.id.btn_login)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        loginButton.text = "Sign In"

        loginButton.setOnClickListener {
            Log.i(tag, "Login Button Clicked...")

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            Log.i(tag, "Email ::: ".plus(email))
            Log.i(tag, "Password ::: ".plus(password))

            if (email.isBlank() ||
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    this,
                    "Enter a valid email",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password.isBlank()) {
                Toast.makeText(
                    this,
                    "Enter a password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //TODO Do remote authentication
                val testDatabase = TestDatabase.getInstance(applicationContext)
                val userDao = testDatabase.getUserDao()

                Thread {
                    try {
                        val userInDb = userDao.getValidUser(email, password)
                        if (userInDb == null) {
                            runOnUiThread {
                                showToast("Email or Password is incorrect...")
                            }
                        } else {
                            runOnUiThread {
                                showToast("Logged In Successfully...")
                                onSuccessfulLogin(userInDb)
                            }
                        }
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        runOnUiThread {
                            showToast("Couldn't Login. Please try again...")
                        }
                    }
                }.start()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "onStart Called...")
    }

    override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume Called...")
    }

    override fun onPause() {
        super.onPause()
        Log.i(tag, "onPause Called...")
    }

    override fun onStop() {
        super.onStop()
        Log.i(tag, "onStop Called...")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "onDestroy Called...")
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun onSuccessfulLogin(userInDb: User) {
        //Writing to shared preferences
        val sharedPreferences = this.getSharedPreferences(
            AppConstants.FILE_SHARED_PREF_LOGIN,
            Context.MODE_PRIVATE
        )
        val sharedPrefEditor = sharedPreferences.edit()
        sharedPrefEditor.putBoolean(
            AppConstants.KEY_IS_LOGGED_IN,
            true
        )
        sharedPrefEditor.apply()

        val intent = Intent(
            this,
            DashboardActivity::class.java
        )
        intent.putExtra(AppConstants.KEY_LOGIN_DATA, userInDb)
        startActivity(intent)
        finish()
    }
}