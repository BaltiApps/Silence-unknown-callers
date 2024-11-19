package baltiapps.callblock

import android.Manifest
import android.app.role.RoleManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import baltiapps.callblock.domain.UIScope
import baltiapps.callblock.ui.screens.appScreen.AppScreen
import baltiapps.callblock.ui.screens.setup.FirstStep
import baltiapps.callblock.ui.theme.SilenceUnknownCallersTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity(), UIScope {

    private val roleManager by lazy {
        getSystemService(RoleManager::class.java)
    }

    private var onNotificationPermissionListener: ((isGranted: Boolean) -> Unit)? = null
    private var onCallLogPermissionListener: ((isGranted: Boolean) -> Unit)? = null
    private var onCallScreenPermissionListener: ((isGranted: Boolean) -> Unit)? = null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onNotificationPermissionListener?.invoke(isGranted)
    }

    private val callScreenPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        onCallScreenPermissionListener?.invoke(
            roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
        )
    }

    private val callLogPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onCallLogPermissionListener?.invoke(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            SilenceUnknownCallersTheme {
                NavHost(
                    navController = navController,
                    startDestination = RouteAppScreen
                ) {
                    composable<RouteFirstSetup> {
                        FirstStep() {
                            navController.navigateUp()
                        }
                    }
                    composable<RouteAppScreen> {
                        AppScreen() {
                            navController.navigate(RouteFirstSetup) {
                                popUpTo(navController.graph.id)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun requestNotificationsPermission(callback: (isGranted: Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onNotificationPermissionListener = callback
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun requestCallFilterPermission(callback: (isGranted: Boolean) -> Unit) {
        val requestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        onCallScreenPermissionListener = callback
        callScreenPermissionLauncher.launch(requestIntent)
    }

    override fun requestCallLogPermission(callback: (isGranted: Boolean) -> Unit) {
        onCallLogPermissionListener = callback
        callLogPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
    }
}

@Serializable
object RouteFirstSetup

@Serializable
object RouteAppScreen