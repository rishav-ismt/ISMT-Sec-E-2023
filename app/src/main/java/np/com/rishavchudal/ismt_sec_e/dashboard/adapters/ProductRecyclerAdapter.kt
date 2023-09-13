package np.com.rishavchudal.ismt_sec_e.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import np.com.rishavchudal.ismt_sec_e.R
import np.com.rishavchudal.ismt_sec_e.database.Product

class ProductRecyclerAdapter(
    private val products: List<Product>,
    private val listener: ProductAdapterListener
): RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var itemRootLayout: ConstraintLayout
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDescription: TextView

        init {
            itemRootLayout = view.findViewById(R.id.item_root_layout)
            itemImage = view.findViewById(R.id.iv_item_image)
            itemTitle = view.findViewById(R.id.tv_item_title)
            itemDescription = view.findViewById(R.id.tv_item_description)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        //TODO to populate image
        holder.itemTitle.text = products[position].title
        holder.itemDescription.text = products[position].description
        holder.itemRootLayout.setOnClickListener {
            listener.onItemClicked(products[position], position)
        }
    }

    interface ProductAdapterListener {
        fun onItemClicked(product: Product, position: Int)
    }
}