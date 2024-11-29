package com.varad.callbackflowsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.varad.callbackflowsample.ui.theme.CallbackFlowSampleTheme
import com.varad.core.navigation.Route
import com.varad.core.util.UiEvent
import com.varad.network_connectivity_domain.AndroidConnectivityObserver
import com.varad.network_connectivity_presentation.ConnectivityViewModel
import com.varad.network_connectivity_presentation.NetworkConnectivityScreen

class MainActivity : ComponentActivity() {

    private val connectivityViewModel: ConnectivityViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return ConnectivityViewModel(
                    AndroidConnectivityObserver(context = applicationContext)
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallbackFlowSampleTheme {

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                val isConnected = connectivityViewModel.isConnected.collectAsStateWithLifecycle()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Route.HOME,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Route.HOME) {
                            HomeScreen(onNavigate = {
                                navController.navigate(it.route)
                            })
                        }

                        composable(Route.NETWORK) {
                            NetworkConnectivityScreen(isConnected.value)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                onNavigate(UiEvent.Navigate(Route.NETWORK))
            }
        ) {
            Text(text = stringResource(R.string.network_connectivity_title))
        }
    }
}
