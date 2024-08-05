package com.team6.connectbca.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun getFormattedAccountNo(accountNo: Double) : String {
    val format = DecimalFormat("###,###,####", DecimalFormatSymbols(Locale.FRENCH))
    return format.format(accountNo)
}

fun getFormattedBalance(balance: Int) : String {
    val formatter = DecimalFormat("#,###,###")
    return formatter.format(balance.toDouble())
}