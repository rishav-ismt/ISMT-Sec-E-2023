package np.com.rishavchudal.ismt_sec_e.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.SmsManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import np.com.rishavchudal.ismt_sec_e.AppConstants
import np.com.rishavchudal.ismt_sec_e.BitmapScalar
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.UiUtility
import np.com.rishavchudal.ismt_sec_e.database.Product
import np.com.rishavchudal.ismt_sec_e.database.TestDatabase
import np.com.rishavchudal.ismt_sec_e.databinding.ActivityDetailViewBinding
import java.io.IOException


class DetailViewActivity : AppCompatActivity() {
    private lateinit var detailViewBinding: ActivityDetailViewBinding
    private var product: Product? = null
    private var position: Int = -1

    private val startAddItemActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AddOrUpdateItemActivity.RESULT_CODE_COMPLETE) {
            val product = it.data?.getParcelableExtra<Product>(AppConstants.KEY_PRODUCT)
            populateDataToTheViews(product)
        } else {
            // TODO do nothing
        }
    }

    companion object {
        const val RESULT_CODE_REFRESH = 2001
        const val SMS_PERMISSIONS_REQUEST_CODE = 111
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewBinding = ActivityDetailViewBinding.inflate(layoutInflater)
        setContentView(detailViewBinding.root)

        //Receiving Intent Data
        product = intent.getParcelableExtra(AppConstants.KEY_PRODUCT)
        position = intent.getIntExtra(AppConstants.KEY_PRODUCT_POSITION, -1)

        //Populate Data to the views
        populateDataToTheViews(product)
        setUpButtons()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSIONS_REQUEST_CODE && areSmsPermissionsGranted(this)) {
            showSmsBottomSheetDialog()
        } else {
            UiUtility.showToast(this, "Please provide permission for SMS")
        }
    }

    private fun populateDataToTheViews(product: Product?) {
        product?.apply {
            detailViewBinding.productTitle.text = this.title
            detailViewBinding.productPrice.text = this.price
            detailViewBinding.productDescription.text = this.description
            detailViewBinding.productImage.post {
                var bitmap: Bitmap?
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        applicationContext.contentResolver,
                        Uri.parse(product.image)
                    )
                    bitmap = BitmapScalar.stretchToFill(
                        bitmap,
                        detailViewBinding.productImage.width,
                        detailViewBinding.productImage.height
                    )
                    detailViewBinding.productImage.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            //TODO populate location
        }
    }

    private fun setUpButtons() {
        setUpBackButton()
        setUpEditButton()
        setUpDeleteButton()
        setUpShareButton()
    }

    private fun setUpBackButton() {
        detailViewBinding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun setUpEditButton() {
        detailViewBinding.ibEdit.setOnClickListener {
            val intent = Intent(this, AddOrUpdateItemActivity::class.java).apply {
                this.putExtra(AppConstants.KEY_PRODUCT, product)
            }
            startAddItemActivity.launch(intent)
        }
    }

    private fun setUpDeleteButton() {
        //TODO
        detailViewBinding.ibDelete.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Alert")
                .setMessage("Do you want to delete this product?")
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener {
                            dialogInterface,
                            i -> deleteProduct()
                    })
                .setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener {
                            dialogInterface,
                            i ->  dialogInterface.dismiss()

                    })
                .show()
        }
    }

    private fun setUpShareButton() {
        //TODO
        detailViewBinding.ibShare.setOnClickListener {
            if (areSmsPermissionsGranted(this)) {
                showSmsBottomSheetDialog()
            } else {
                requestPermissions(
                    smsPermissionsList()!!.toTypedArray(),
                    SMS_PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    private fun deleteProduct() {
        val testDatabase = TestDatabase.getInstance(this.applicationContext)
        val productDao = testDatabase.getProductDao()

        Thread {
            try {
                product?.apply {
                    productDao.deleteProduct(this)
                    runOnUiThread {
                        UiUtility.showToast(
                            this@DetailViewActivity,
                            "Product deleted successfully"
                        )
                        setResultWithFinish(RESULT_CODE_REFRESH)
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                runOnUiThread {
                    UiUtility.showToast(
                        this@DetailViewActivity,
                        "Cannot delete product."
                    )
                }
            }
        }.start()
    }

    private fun areSmsPermissionsGranted(context: Context): Boolean {
        var areAllPermissionGranted = false
        for (permission in smsPermissionsList()!!) {
            if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                areAllPermissionGranted = true
            } else {
                areAllPermissionGranted = false
                break
            }
        }
        return areAllPermissionGranted
    }
    private fun smsPermissionsList(): List<String>? {
        val smsPermissions: MutableList<String> = ArrayList()
        smsPermissions.add(Manifest.permission.READ_SMS)
        smsPermissions.add(Manifest.permission.SEND_SMS)
        return smsPermissions
    }

    private fun showSmsBottomSheetDialog() {
        //TODO
        val smsBottomSheetDialog = BottomSheetDialog(this)
        smsBottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_send_sms)

        val tilContact: TextInputLayout? = smsBottomSheetDialog.findViewById(R.id.til_contact)
        val tieContact: TextInputEditText? = smsBottomSheetDialog.findViewById(R.id.tie_contact)
        val sendSmsButton: MaterialButton? = smsBottomSheetDialog.findViewById(R.id.mb_send_sms)

        tilContact?.setEndIconOnClickListener {
            //TODO open Contact Activity
        }

        sendSmsButton?.setOnClickListener {
            val contact = tieContact?.text.toString()
            //TODO validation
            if (contact.isBlank()) {
                tilContact?.error = "Enter Contact"
            } else {
                sendSms(contact)
            }
        }
    }

    private fun sendSms(contact: String) {
        val smsManager: SmsManager = SmsManager.getDefault()
        val message = """
            Item: ${product!!.title}
            Price: ${product!!.price}
            Description: ${product!!.description.substring(50)}
            """.trimIndent()
        smsManager.sendTextMessage(
            contact,
            null,
            message,
            null,
            null
        )

        //If the above SMS manager didn't work
        openSmsAppToSendMessage(contact, message)
    }

    private fun openSmsAppToSendMessage(contact: String, message: String) {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("smsto:$contact")
        sendIntent.putExtra("sms_body", message)
        startActivity(intent)
    }

    private fun setResultWithFinish(resultCode: Int) {
        setResult(resultCode)
        finish()
    }
}