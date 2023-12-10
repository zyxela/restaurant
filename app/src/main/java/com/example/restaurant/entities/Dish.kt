package com.example.restaurant.entities

data class Dish(
    val id:Int,
    val name:String,
    val description:String,
    val price:Int,
    val image:ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Dish

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (price != other.price) return false
        return image.contentEquals(other.image)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price
        result = 31 * result + image.contentHashCode()
        return result
    }
}