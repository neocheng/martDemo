package tw.com.nsy.demo1.connectNetFunc.dataModel

data class ShopCarModel(
    var data: ArrayList<Data2>
)

data class Data2(
    var finalPrice: Int,
    var imageUrl: String,
    var martId: Int,
    var martName: String,
    var martShortName: String,
    var price: Int,
    var stockAvailable: Int
)