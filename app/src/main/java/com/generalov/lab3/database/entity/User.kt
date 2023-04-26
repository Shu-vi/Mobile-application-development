package com.generalov.lab3.database.entity

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Entity(tableName = "users", indices = [Index(value = ["phone"], unique = true)])
data class User(
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int?
) : java.io.Serializable


@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE phone = :phone")
    fun getUserByPhone(phone: String): Observable<List<User>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user: User): Completable

    @Update
    fun updateUser(user: User): Completable

    @Delete
    fun deleteUser(user: User): Completable
}