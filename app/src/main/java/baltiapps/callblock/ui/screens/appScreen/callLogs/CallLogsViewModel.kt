package baltiapps.callblock.ui.screens.appScreen.callLogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.repository.Repository
import baltiapps.callblock.domain.sources.PlatformCallLogSource
import baltiapps.callblock.domain.useCases.ReplaceCallLogWithDbEntriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CallLogsViewModel(
    private val repository: Repository,
    private val callLogSource: PlatformCallLogSource,
    private val replaceCallLogWithDbEntriesUseCase: ReplaceCallLogWithDbEntriesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        CallLogsState(
            isCallLogPermissionGranted = callLogSource.isCallLogPermissionGranted(),
            isLoadingCallLogs = false,
            callInformationList = emptyList()
        )
    )
    val state = _state.asStateFlow()

    private val dbEntries = mutableListOf<CallInformation>()

    init {
        viewModelScope.launch {
            repository.getFilteredCallsHistory().collect {
                dbEntries.clear()
                dbEntries.addAll(it)
                withContext(Dispatchers.Default) {
                    populateCallLogs()
                }
            }
        }
    }

    private suspend fun populateCallLogs() {
        _state.update {
            it.copy(
                isLoadingCallLogs = true,
            )
        }
        val deviceCallLogs = callLogSource.getCallLogsList()
        val replacedLogs = replaceCallLogWithDbEntriesUseCase.invoke(
            callLogs = deviceCallLogs,
            logsFromDb = dbEntries,
        )
        _state.update {
            it.copy(
                isLoadingCallLogs = false,
                callInformationList = replacedLogs,
            )
        }
    }

    fun performAction(action: CallLogsAction) {
        when (action) {
            is CallLogsAction.RequestCallLogPermission ->
                action.uiScope.requestCallLogPermission { isGranted ->
                    _state.update {
                        it.copy(
                            isCallLogPermissionGranted = isGranted
                        )
                    }
                    viewModelScope.launch {
                        populateCallLogs()
                    }
                }
        }
    }
}