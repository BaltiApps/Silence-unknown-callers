package baltiapps.callblock.domain.models

interface PlatformCallData {
    fun toCallInformation(): CallInformation
}