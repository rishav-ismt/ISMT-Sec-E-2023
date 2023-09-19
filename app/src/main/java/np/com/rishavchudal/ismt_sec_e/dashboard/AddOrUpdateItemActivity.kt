package np.com.rishavchudal.ismt_sec_e.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import np.com.rishavchudal.ismt_sec_e.AppConstants
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.database.Product
import np.com.rishavchudal.ismt_sec_e.database.TestDatabase
import np.com.rishavchudal.ismt_sec_e.databinding.ActivityAddOrUpdateItemBinding
import java.lang.Exception

class AddOrUpdateItemActivity : AppCompatActivity() {
    private lateinit var addOrUpdateItemBinding: ActivityAddOrUpdateItemBinding
    private var receivedProduct: Product? = null
    private var isForUpdate = false

    companion object {
        const val RESULT_CODE_COMPLETE = 1001
        const val RESULT_CODE_CANCEL = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addOrUpdateItemBinding = ActivityAddOrUpdateItemBinding.inflate(layoutInflater)
        setContentView(addOrUpdateItemBinding.root)

        receivedProduct = intent.getParcelableExtra<Product>(AppConstants.KEY_PRODUCT)
        receivedProduct?.apply {
            isForUpdate = true
            addOrUpdateItemBinding.tieTitle.setText(this.title)
            addOrUpdateItemBinding.tiePrice.setText(this.price)
            addOrUpdateItemBinding.tieDescription.setText(this.description)
            //TODO for image and location
        }

        addOrUpdateItemBinding.ibBack.setOnClickListener {
            setResultWithFinish(RESULT_CODE_CANCEL)
        }

        addOrUpdateItemBinding.mbSubmit.setOnClickListener {
            val title = addOrUpdateItemBinding.tieTitle.text.toString().trim()
            val price = addOrUpdateItemBinding.tiePrice.text.toString().trim()
            val description = addOrUpdateItemBinding.tieDescription.text.toString().trim()
            /**
             * TODO Validation
             * Check fields are empty
             * Check if image or location data is empty
             */

            val product = Product(
                title,
                price,
                description,
                "",
                ""
            )

            if (isForUpdate) {
                product.id = receivedProduct!!.id
            }

            val testDatabase = TestDatabase.getInstance(applicationContext)
            val productDao = testDatabase.getProductDao()

            Thread {
                try {
                    if (isForUpdate) {
                        productDao.updateProduct(product)
                        runOnUiThread {
                            clearFieldsData()
                            showToast("Product updated successfully...")
                            setResultWithFinish(RESULT_CODE_COMPLETE)
                        }
                    } else {
                        productDao.insertNewProduct(product)
                        runOnUiThread {
                            clearFieldsData()
                            showToast("Product added successfully...")
                            setResultWithFinish(RESULT_CODE_COMPLETE)
                        }
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    runOnUiThread {
                        showToast("Couldn't add or update product. Try again...")
                    }
                }
            }.start()
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun clearFieldsData() {
        addOrUpdateItemBinding.tieTitle.text?.clear()
        addOrUpdateItemBinding.tiePrice.text?.clear()
        addOrUpdateItemBinding.tieDescription.text?.clear()
    }

    private fun setResultWithFinish(resultCode: Int) {
        setResult(resultCode)
        finish()
    }
    override fun onBackPressed() {
        setResultWithFinish(RESULT_CODE_CANCEL)
    }
}