package baltiapps.callblock.di

import androidx.room.Room
import baltiapps.callblock.data.sources.CallFilterApi
import baltiapps.callblock.data.sources.CallLogSource
import baltiapps.callblock.data.sources.DBSource
import baltiapps.callblock.data.sources.NotificationsApi
import baltiapps.callblock.data.sources.Preferences
import baltiapps.callblock.data.sources.db.AppDb
import baltiapps.callblock.data.sources.db.FilterDataDao
import baltiapps.callblock.domain.repository.Repository
import baltiapps.callblock.domain.sources.PlatformCallFilterApi
import baltiapps.callblock.domain.sources.PlatformCallLogSource
import baltiapps.callblock.domain.sources.PlatformDBSource
import baltiapps.callblock.domain.sources.PlatformNotificationsApi
import baltiapps.callblock.domain.sources.PlatformPreferences
import baltiapps.callblock.domain.useCases.DisplayNotificationsUseCase
import baltiapps.callblock.domain.useCases.PerformCallFilterUseCase
import baltiapps.callblock.domain.useCases.ReplaceCallLogWithDbEntriesUseCase
import baltiapps.callblock.domain.useCases.ShouldBlockCallUseCase
import baltiapps.callblock.domain.useCases.ShouldSilenceCallUseCase
import baltiapps.callblock.domain.useCases.ToggleBlockCallUseCase
import baltiapps.callblock.domain.useCases.ToggleNotificationsUseCase
import baltiapps.callblock.domain.useCases.ToggleSilenceCallUseCase
import baltiapps.callblock.ui.screens.appScreen.AppScreenViewModel
import baltiapps.callblock.ui.screens.appScreen.callLogs.CallLogsViewModel
import baltiapps.callblock.ui.screens.appScreen.homeSettings.HomeSettingsViewModel
import baltiapps.callblock.ui.screens.setup.FirstSetupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<AppDb> {
        Room.databaseBuilder(
            get(),
            AppDb::class.java,
            "AppDb",
        ).build()
    }

    single<FilterDataDao> {
        get<AppDb>().filterDataDao()
    }

    single {
        Repository(
            dbSource = get(),
        )
    }

    single<PlatformCallFilterApi<*,*>> {
        CallFilterApi(
            context = get(),
            preferences = get(),
        )
    }

    single<PlatformCallLogSource> {
        CallLogSource(
            context = get()
        )
    }

    single<PlatformDBSource> {
        DBSource(
            filterDataDao = get()
        )
    }

    single<PlatformNotificationsApi> {
        NotificationsApi(
            context = get(),
        )
    }

    single<PlatformPreferences> {
        Preferences(
            context = get()
        )
    }

    single {
        DisplayNotificationsUseCase(
            notificationsApi = get(),
            preferences = get(),
        )
    }

    single {
        PerformCallFilterUseCase(
            callFilterApi = get()
        )
    }

    single {
        ReplaceCallLogWithDbEntriesUseCase()
    }

    single {
        ShouldBlockCallUseCase(
            callFilterApi = get(),
            preferences = get(),
        )
    }

    single {
        ShouldSilenceCallUseCase(
            callFilterApi = get(),
            preferences = get(),
        )
    }

    single {
        ToggleBlockCallUseCase(
            callFilterApi = get(),
            preferences = get(),
        )
    }

    single {
        ToggleNotificationsUseCase(
            notificationApi = get(),
            preferences = get(),
        )
    }

    single {
        ToggleSilenceCallUseCase(
            callFilterApi = get(),
            preferences = get(),
        )
    }

    viewModel<AppScreenViewModel> {
        AppScreenViewModel(
            preferences = get()
        )
    }

    viewModel<HomeSettingsViewModel> {
        HomeSettingsViewModel(
            toggleSilenceCallUseCase = get(),
            toggleBlockCallUseCase = get(),
            toggleNotificationsUseCase = get(),
            preferences = get(),
            callFilterApi = get(),
            notificationsApi = get(),
        )
    }

    viewModel<CallLogsViewModel> {
        CallLogsViewModel(
            repository = get(),
            callLogSource = get(),
            replaceCallLogWithDbEntriesUseCase = get(),
        )
    }

    viewModel<FirstSetupViewModel> {
        FirstSetupViewModel(
            preferences = get(),
            callFilterApi = get(),
            callLogSource = get(),
        )
    }
}