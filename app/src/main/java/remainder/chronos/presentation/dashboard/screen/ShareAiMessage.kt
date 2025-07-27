package remainder.chronos.presentation.dashboard.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import remainder.chronos.R
import remainder.chronos.core.composable.ReusableScaffold
import remainder.chronos.presentation.dashboard.state.ReminderUiState
import remainder.chronos.presentation.dashboard.viewmodel.DashboardViewModel

@Composable
fun CreateAndShareAiMessage(navController: NavController) {

    val context = LocalContext.current
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("home")
    }

    val dashboardViewModel: DashboardViewModel = hiltViewModel(parentEntry)

    val aiMessageUiState by dashboardViewModel.aiResponseUiState.collectAsState()


    var userPrompt by remember { mutableStateOf("") }


    when (val state = aiMessageUiState) {
        is ReminderUiState.SuccessMessage -> {
            OpenShareSheet(state.message, context)
            userPrompt = ""
            dashboardViewModel.resetAiMessageUiState()
        }
        is ReminderUiState.ErrorMessage ->{
            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            dashboardViewModel.resetAiMessageUiState()
        }
        else -> {}
    }

    ReusableScaffold(title = stringResource(R.string.ai_message)) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedInputField(
                    value = userPrompt,
                    onValueChange = {
                        userPrompt = it
                    },

                    label = stringResource(R.string.enter_prompt),
                    icon = R.drawable.ai_logo,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (userPrompt.isNotEmpty())
                                dashboardViewModel.createAiMessage(userPrompt)
                            else
                                Toast.makeText(context, context.getString(R.string.enter_valid_prompt), Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center,
                ) {

                        if(aiMessageUiState is ReminderUiState.Loading){
                            CircularProgressIndicator(modifier = Modifier.size(32.dp), color = MaterialTheme.colorScheme.onPrimary)
                        }
                        else {
                            Text(
                                stringResource(R.string.create),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(10.dp),
                            )
                    }
                }

            }
        }
    }
}

@Composable
fun OpenShareSheet(message: String, context : Context) {

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(intent, "Share")
    context.startActivity(shareIntent)
}

