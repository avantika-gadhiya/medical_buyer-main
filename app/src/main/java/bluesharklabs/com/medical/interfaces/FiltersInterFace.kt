package bluesharklabs.com.medical.interfaces

interface FiltersInterFace {
    fun onClickType(type: String)
    fun onClickStatus(status: String)
    fun onClickStoreName(status: String)
    fun onClickCity(city: String)
    fun onClickArea(area: String)
    fun onClickZipcode(zipcode: String)
    fun onClickOffer(offer: String)
}