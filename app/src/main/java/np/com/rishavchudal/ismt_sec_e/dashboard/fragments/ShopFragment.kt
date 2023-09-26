package np.com.rishavchudal.ismt_sec_e.dashboard.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import np.com.rishavchudal.ismt_sec_e.AppConstants
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.UiUtility
import np.com.rishavchudal.ismt_sec_e.dashboard.AddOrUpdateItemActivity
import np.com.rishavchudal.ismt_sec_e.dashboard.DetailViewActivity
import np.com.rishavchudal.ismt_sec_e.dashboard.adapters.ProductRecyclerAdapter
import np.com.rishavchudal.ismt_sec_e.database.Product
import np.com.rishavchudal.ismt_sec_e.database.TestDatabase
import np.com.rishavchudal.ismt_sec_e.databinding.FragmentShopBinding

class ShopFragment : Fragment(), ProductRecyclerAdapter.ProductAdapterListener {
    private lateinit var shopBinding: FragmentShopBinding
    private lateinit var productRecyclerAdapter: ProductRecyclerAdapter

    private val startAddOrUpdateActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AddOrUpdateItemActivity.RESULT_CODE_COMPLETE) {
            setUpRecyclerView()
        } else {
            //TODO Do nothing
        }
    }

    private val startDetailViewActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == DetailViewActivity.RESULT_CODE_REFRESH) {
            setUpRecyclerView()
        } else {
            //Do Nothing
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        shopBinding = FragmentShopBinding.inflate(layoutInflater, container, false)
        setUpViews()
        return shopBinding.root
    }

    private fun setUpViews() {
        setUpFloatingActionButton()
        setUpRecyclerView()
    }

    private fun setUpFloatingActionButton() {
        shopBinding.fabAdd.setOnClickListener {
            val intent = Intent(requireActivity(), AddOrUpdateItemActivity::class.java)
            startAddOrUpdateActivityForResult.launch(intent)
        }
    }

    private fun setUpRecyclerView() {
        //TODO fetch data from source (remote server)
        val testDatabase = TestDatabase.getInstance(requireActivity().applicationContext)
        val productDao = testDatabase.getProductDao()

        Thread {
            try {
                val products = productDao.getAllProducts()
                if (products.isEmpty()) {
                    requireActivity().runOnUiThread {
                        UiUtility.showToast(requireActivity(), "No Items Added...")
                    }
                }
                requireActivity().runOnUiThread {
                    populateRecyclerView(products)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                requireActivity().runOnUiThread {
                    UiUtility.showToast(requireActivity(), "Couldn't load items.")
                }
            }
        }.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopFragment()
    }

    private fun populateRecyclerView(products: List<Product>) {
        productRecyclerAdapter = ProductRecyclerAdapter(
            products,
            this,
            requireActivity().applicationContext
        )
        shopBinding.rvShop.adapter = productRecyclerAdapter
        shopBinding.rvShop.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onItemClicked(product: Product, position: Int) {
        val intent = Intent(requireActivity(), DetailViewActivity::class.java)
        intent.putExtra(AppConstants.KEY_PRODUCT, product)
        intent.putExtra(AppConstants.KEY_PRODUCT_POSITION, position)
        startDetailViewActivity.launch(intent)
    }
}