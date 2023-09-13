package np.com.rishavchudal.ismt_sec_e.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import np.com.rishavchudal.ismt_sec_e.database.Product

@Dao
interface ProductDao {
    @Insert
    fun insertNewProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Query("Select * from product")
    fun getAllProducts(): List<Product>
}