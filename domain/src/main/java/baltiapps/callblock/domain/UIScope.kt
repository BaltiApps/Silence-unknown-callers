package baltiapps.callblock.domain

interface UIScope {
    fun requestNotificationsPermission(callback: (isGranted: Boolean) -> Unit)
    fun requestCallFilterPermission(callback: (isGranted: Boolean) -> Unit)
    fun requestCallLogPermission(callback: (isGranted: Boolean) -> Unit)
}