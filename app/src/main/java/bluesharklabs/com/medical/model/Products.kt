package bluesharklabs.com.medical.model

class Products(var product_x_position: String,var product_y_position: String,var product_id:String,var color: String,var quantity:String,var quantity_type:String) {
   /* var product_x_position: String? = null
    var product_y_position: String? = null
    var product_id: String? = null
    var color: String? = null
    var quantity: String? = null
    var quantity_type: String? = null

    fun Products(product_x_position: String, product_y_position: String,product_id:String,color: String,quantity:String,quantity_type:String) {
        this.product_x_position = product_x_position
        this.product_y_position = product_y_position
        this.product_id = product_id
        this.color = color
        this.quantity = quantity
        this.quantity_type = quantity_type
    }*/
   init {
       println("Initialized a new Person object with firstName = $product_x_position and lastName = $product_y_position")
   }
}