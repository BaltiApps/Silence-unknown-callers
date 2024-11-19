package baltiapps.callblock.domain.models

data class CallInformation(
    val displayName: String,
    val callTimeStamp: Long,
    val callType: Int,
    val filterVerdict: FilterVerdict = FilterVerdict.UNFILTERED
) {
    fun isFiltered(): Boolean = filterVerdict != FilterVerdict.UNFILTERED
}
