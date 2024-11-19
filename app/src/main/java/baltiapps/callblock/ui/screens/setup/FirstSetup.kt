package baltiapps.callblock.ui.screens.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DoNotDisturbOnTotalSilence
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PermPhoneMsg
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import baltiapps.callblock.MainActivity
import baltiapps.callblock.R
import baltiapps.callblock.domain.UIScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FirstStep(
    navigateToAppScreen: () -> Unit
) {
    val viewModel = koinViewModel<FirstSetupViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner
    )

    val uiScope = LocalContext.current as? MainActivity

    Content(
        state = { state },
        uiScope = uiScope,
        performAction = viewModel::performAction,
        navigateToAppScreen = navigateToAppScreen
    )
}

@Composable
private fun Content(
    state: () -> FirstSetupState,
    uiScope: UIScope?,
    performAction: (FirstSetupAction) -> Unit,
    navigateToAppScreen: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Button (
                enabled = pagerState.currentPage != 1
                        || state().isCallFilterPermissionGranted,
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier.defaultMinSize(minWidth = 64.dp, minHeight = 64.dp),
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < pagerState.pageCount-1) {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )
                        } else {
                            performAction(FirstSetupAction.FinishFirstStep)
                            navigateToAppScreen()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = stringResource(R.string.next)
                )
            }
        }
    ) { innerPadding ->
        HorizontalPager(pagerState) { count ->
            when(count) {
                0 -> IntroScreen1(
                    modifier = Modifier.padding(innerPadding),
                )
                1 -> IntroScreen2(
                    modifier = Modifier.padding(innerPadding),
                    uiScope = uiScope,
                    performAction = performAction,
                    state = state,
                )
            }
        }
    }
}

@Preview
@Composable
private fun IntroScreen1(
    modifier: Modifier = Modifier,
    iconSize: Int = 200,
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier.size(iconSize.dp),
                imageVector = Icons.Default.DoNotDisturbOnTotalSilence,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Column {
                Text(
                    text = stringResource(R.string.intro1_text1),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = stringResource(R.string.intro1_text2),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun IntroScreen2(
    state: () -> FirstSetupState,
    modifier: Modifier = Modifier,
    uiScope: UIScope?,
    performAction: (FirstSetupAction) -> Unit,
    iconSize: Int = 200,
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier.size(iconSize.dp),
                imageVector = Icons.Default.PermPhoneMsg,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.intro1_text3),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(32.dp))
                Button(
                    onClick = {
                        uiScope?.let {
                            performAction(FirstSetupAction.RequestCallFilterPermission(it))
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.set_as_default_call_filter))
                    if (state().isCallFilterPermissionGranted) {
                        Spacer(
                            modifier = Modifier.size(width = 8.dp, height = 0.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Done, contentDescription = null
                        )
                    }
                }
                FilledTonalButton(
                    onClick = {
                        uiScope?.let {
                            performAction(FirstSetupAction.RequestCallLogPermission(it))
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.allow_call_log_optional))
                    if (state().isCallLogPermissionGranted) {
                        Spacer(
                            modifier = Modifier.size(width = 8.dp, height = 0.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Done, contentDescription = null
                        )
                    }
                }
            }
        }
    }
}