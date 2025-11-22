package org.speed.realestate.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "real_estate_table")
data class RealEstate (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    val name : String,
    val tradeType : String,
    val dong : String,
    val ho : String,
    val type : String?,
    val price : String?,
    val option : String?,
    val phoneNumber : String?,
    val description : String?,
)