package np.com.rishavchudal.ismt_sec_e.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "product")
@Parcelize
data class Product(
    val title: String,
    val price: String,
    val description: String,
    val image: String?,
    val location: String?
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
