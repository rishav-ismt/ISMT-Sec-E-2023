package np.com.rishavchudal.ismt_sec_e.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("Select * from user_data where email = :email")
    fun checkUserExist(email: String): User?

    @Query("Select * from user_data where email = :email and password = :password ")
    fun getValidUser(email: String, password: String): User?
}