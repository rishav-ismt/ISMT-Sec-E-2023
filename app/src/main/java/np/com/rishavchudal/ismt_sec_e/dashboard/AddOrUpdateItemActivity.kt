package np.com.rishavchudal.ismt_sec_e.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.databinding.ActivityAddOrUpdateItemBinding

class AddOrUpdateItemActivity : AppCompatActivity() {
    private lateinit var addOrUpdateItemBinding: ActivityAddOrUpdateItemBinding

    companion object {
        const val RESULT_CODE_COMPLETE = 1001
        const val RESULT_CODE_CANCEL = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addOrUpdateItemBinding = ActivityAddOrUpdateItemBinding.inflate(layoutInflater)
        setContentView(addOrUpdateItemBinding.root)
    }
}