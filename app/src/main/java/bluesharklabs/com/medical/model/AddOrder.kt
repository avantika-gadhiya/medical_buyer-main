package bluesharklabs.com.medical.model

import java.util.ArrayList


class AddOrder {
    var user_id: String? = null
    var order_for: String? = null
    var order_preference: String? = null
    var buying_preference: String? = null
    var delivery_type: String? = null
    var delivery_city: String? = null
    var location: String? = null
    var block_no: String? = null
    var building_name: String? = null
    var street: String? = null
    var area: String? = null
    var landmark: String? = null
    var zipcode: String? = null
    var delivery_date: String? = null
    var to_delivery_date: String? = null
    var start_time: String? = null
    var end_time: String? = null
    var prescription_image: String? = null
    var photo_id: String? = null
    var order_latitude: String? = null
    var order_longitude: String? = null
    var delivery_latitude: String? = null
    var delivery_longitude: String? = null
    var turn_on_location: String? = null
    var store_criteria_store_type_id: String? = null
    var store_criteria_city: String? = null
    var store_criteria_area: String? = null
    var store_criteria_zipcode: String? = null
    var store_criteria_store_md_id: String? = null
    var location_criteria_location: String? = null
    var location_criteria_delivery_location: String? = null
    var order_platform: String? = null //1; platform , 2 store
    var store_id: String? = null
    var store_latitude: String? = null
    var store_longitude: String? = null
    var select_by_location_criteria: String? = null
    var select_by_store_criteria: String? = null


    var products = ArrayList<Products>()
    var custom_products = ArrayList<customProducts>()

    class Products {
        var product_x_position: String? = null
        var product_y_position: String? = null
        var product_id: String? = null
        var color: String? = null
        var quantity: String? = null
        var quantity_type: String? = null
    }

    class customProducts {
        var product_name: String? = null
        var product_category: String? = null
        var product_brand: String? = null
        var product_content: String? = null
        var product_id: String? = null
        var product_unit: String? = null
        var product_unit_name: String? = null
        var product_pack: String? = null
        var product_pack_name: String? = null
        var quantity: String? = null
        var color: String? = null
    }

}