package np.com.rishavchudal.ismt_sec_e.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.TestDatabase
import np.com.rishavchudal.ismt_sec_e.User
import np.com.rishavchudal.ismt_sec_e.databinding.ActivitySignUpBinding
import np.com.rishavchudal.ismt_sec_e.home.HomeActivity
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.ibBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewBinding.btnSignUp.setOnClickListener {
            //TODO Validation
            //Check if fields are empty
            //Check email structure
            //Check full name has at least two words
            //Check if password matches with confirm password

            val inputFullName = viewBinding.etFullName.text.toString()
            val inputEmail = viewBinding.etEmail.text.toString()
            val inputPassword = viewBinding.etPassword.text.toString()

            val user = User(inputFullName, inputEmail, inputPassword)

            //Inserting into database
            val testDatabase = TestDatabase.getInstance(applicationContext)
            val userDao = testDatabase.getUserDao()

            Thread {
                try {
                    val userInDb = userDao.checkUserExist(inputEmail)
                    if (userInDb == null) {
                        userDao.insertUser(user)
                        runOnUiThread {
                            showToast("Congratulations!! New User Added...")
                            clearInputFieldsData()
                        }
                    } else {
                        runOnUiThread {
                           showToast("User already exist...")
                        }
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    runOnUiThread {
                        showToast("Couldn't insert new user. Please try again...")
                    }
                }
            }.start()
        }
    }

    private fun clearInputFieldsData() {
        viewBinding.etFullName.text.clear()
        viewBinding.etEmail.text.clear()
        viewBinding.etPassword.text.clear()
        viewBinding.etConfirmPassword.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}