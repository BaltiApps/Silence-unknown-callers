package baltiapps.callblock.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TIMESTAMP_FORMAT = "dd MMM yyyy, hh:mm:ss a"
private val formatter by lazy {
    SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault())
}

fun convertLongToTimestamp(epochMillis: Long): String {
    return formatter.format(Date(epochMillis))
}