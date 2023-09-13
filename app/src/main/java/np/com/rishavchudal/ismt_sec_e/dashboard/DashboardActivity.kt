package np.com.rishavchudal.ismt_sec_e.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import np.com.rishavchudal.ismt_sec_e.AppConstants
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.database.User
import np.com.rishavchudal.ismt_sec_e.dashboard.fragments.HomeFragment
import np.com.rishavchudal.ismt_sec_e.dashboard.fragments.ProfileFragment
import np.com.rishavchudal.ismt_sec_e.dashboard.fragments.ShopFragment
import np.com.rishavchudal.ismt_sec_e.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private val tag = "DashboardActivity"
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private val homeFragment = HomeFragment.newInstance()
    private val shopFragment = ShopFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)

        val receivedIntent = intent

        val receivedLoginData = receivedIntent
            .getParcelableExtra<User>(AppConstants.KEY_LOGIN_DATA)

        Log.i(tag, "Received Email ::: "
            .plus(receivedLoginData?.email))
        Log.i(tag, "Received Password ::: "
            .plus(receivedLoginData?.password))
        setUpViews()
    }

    private fun setUpViews() {
        setupFragmentContainerView()
        setUpBottomNavigationView()
    }

    private fun setupFragmentContainerView() {
        loadFragmentInFcv(homeFragment)
    }

    private fun setUpBottomNavigationView() {
        dashboardBinding.bnvDashboard.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    loadFragmentInFcv(homeFragment)
                    true
                }

                R.id.shop -> {
                    loadFragmentInFcv(shopFragment)
                    true
                }

                else -> {
                    loadFragmentInFcv(profileFragment)
                    true
                }
            }
        }
    }

    private fun loadFragmentInFcv(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(dashboardBinding.fcvDashboard.id, fragment)
            .commit()
    }
}