package remainder.chronos.presentation.dashboard.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import remainder.chronos.R
import remainder.chronos.core.composable.Scaffold
import remainder.chronos.core.util.NotificationUtils.HandleNotificationPermission
import remainder.chronos.core.util.UiMessage
import remainder.chronos.presentation.dashboard.component.CustomDialog
import remainder.chronos.presentation.dashboard.component.DeleteReminderDialog
import remainder.chronos.presentation.dashboard.component.SingleReminder
import remainder.chronos.presentation.dashboard.state.FetchReminderUiState
import remainder.chronos.presentation.dashboard.state.ReminderUiState
import remainder.chronos.presentation.dashboard.viewmodel.DashboardViewModel
import remainder.chronos.presentation.navigation.AuthRoutes
import remainder.chronos.presentation.navigation.DashboardRoutes

@Composable
fun DashboardScreen(
    navController: NavController,
    ) {



    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("home")
    }
    val dashboardViewModel: DashboardViewModel = hiltViewModel(parentEntry)

    val fetchReminderUiState by dashboardViewModel.fetchReminderUiState.collectAsState()
    val deleteReminderUiState by dashboardViewModel.deleteReminderUiState.collectAsState()

    var showDeleteReminderDialog by remember { mutableStateOf(false) }




    if (showDeleteReminderDialog) {
        DeleteReminderDialog(
            visible = showDeleteReminderDialog,
            isLoading = deleteReminderUiState is ReminderUiState.Loading,
            onDismiss = { showDeleteReminderDialog = false },
            onConfirm = {
                dashboardViewModel.selectedReminder.value?.let { reminder ->
                    dashboardViewModel.deleteReminder(reminder.reminderId)
                    showDeleteReminderDialog = false
                    dashboardViewModel.resetSelectedReminder()
                }
            }
        )
    }




    HandleDeleteReminderUiState(deleteReminderUiState) {
        dashboardViewModel.resetDeleteReminderState()
    }

    Scaffold(
        title = stringResource(R.string.reminders),
        showLogout = true,
        showFloatingActionButton = true,
        onFloatingActionButtonClick = {
            navController.navigate(DashboardRoutes.AddReminder.route)
        },
        onLogoutClick = {

            dashboardViewModel.logout()

            navController.navigate("auth") {
                popUpTo("home") {
                    inclusive = true
                }
            }
        }
    ) { paddingValues ->

        when (val state = fetchReminderUiState) {
            is FetchReminderUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is FetchReminderUiState.ErrorMessage -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.try_again_later))
                }
            }

            is FetchReminderUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        CreateAndShareAiImageButton{
                          navController.navigate(DashboardRoutes.CreateAiMessage.route)
                      }
                    }
                    items(state.reminders) {
                        SingleReminder(
                            it, onEdit = {
                                // keeping track of selected reminder so that user can edit it in addReminder section
                                dashboardViewModel.setSelectedReminder(it)
                                navController.navigate(DashboardRoutes.AddReminder.route)
                            },
                            onDelete = {
                                dashboardViewModel.setSelectedReminder(it)
                                showDeleteReminderDialog = true
                            })
                    }
                }
            }

            else -> {}
        }


    }


}

@Composable
fun CreateAndShareAiImageButton(onClick : () -> Unit ) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically,

    ) {

        Icon(
            painter = painterResource(R.drawable.ai_logo),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.create_share_ai_message),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(10.dp)
        )
    }
}


@Composable
fun HandleDeleteReminderUiState(deleteReminderUiState: ReminderUiState, onStateHandled: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(deleteReminderUiState) {
        when (val state = deleteReminderUiState) {
            is ReminderUiState.SuccessMessage -> {
                UiMessage.showToast(context , state.message)
                onStateHandled()
            }

            is ReminderUiState.ErrorMessage -> {
                UiMessage.showToast(context , state.message)
                onStateHandled()
            }
            else -> {}
        }
    }
}