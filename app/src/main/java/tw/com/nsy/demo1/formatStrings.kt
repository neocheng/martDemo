package tw.com.nsy.demo1


import java.text.DecimalFormat

object formatStrings {


    fun toPrice(iPayment:String):String{
//        val df = DecimalFormat("$#,##0.00")
        val df = DecimalFormat("$#,###")
//         return df.format(1234567.2)
         return df.format(iPayment.toInt())
    }

    fun toPriceTotal(iPayment1:String,iPayment2:String ,iPayment3:String):String{
//        val df = DecimalFormat("$#,##0.00")
        var _total = iPayment1.toInt ()+iPayment2.toInt()+iPayment3.toInt ()
        val df = DecimalFormat("$#,###")
//         return df.format(1234567.2)
        return df.format(_total)
    }
}