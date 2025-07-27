package remainder.chronos.presentation.dashboard.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import remainder.chronos.R
import remainder.chronos.core.composable.ReusableScaffold
import remainder.chronos.core.util.DateAndTimeUtil
import remainder.chronos.domain.model.Reminder
import remainder.chronos.presentation.dashboard.component.DateTimePickers
import remainder.chronos.presentation.dashboard.component.ImagePicker
import remainder.chronos.presentation.dashboard.state.ReminderUiState
import remainder.chronos.presentation.dashboard.viewmodel.DashboardViewModel
import remainder.chronos.presentation.navigation.AuthRoutes
import remainder.chronos.presentation.navigation.DashboardRoutes
import java.util.Calendar

@Composable
fun AddReminderScreen(
    navController: NavController,

) {

    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("home")
    }
    val dashboardViewModel: DashboardViewModel = hiltViewModel(parentEntry)
    val context = LocalContext.current
    val addUpdateReminderUiState by dashboardViewModel.addUpdateReminderUiState.collectAsState()


    // Case when user trying to edit reminder
    val editReminder = dashboardViewModel.selectedReminder.value


    var reminderTitle by remember { mutableStateOf(editReminder?.reminderTitle ?: "") }
    var selectedDate by remember { mutableStateOf(editReminder?.reminderDate ?: "") }
    var selectedTime by remember { mutableStateOf(editReminder?.reminderTime ?: "") }
    var selectedReminderImage by remember { mutableStateOf<Uri?>(editReminder?.reminderImage?.toUri()) }
    var reminderOptionalNote by remember { mutableStateOf(editReminder?.reminderNote ?: "") }

    val calendar = remember { Calendar.getInstance() }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        selectedReminderImage = it
    }

    val showDatePicker = {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, y, m, d -> selectedDate = "$d/${m + 1}/$y" },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    val showTimePicker = {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val amPm = if (hourOfDay >= 12) "PM" else "AM"
                val hour =
                    if (hourOfDay == 0) 12 else if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
                selectedTime = String.format("%02d:%02d %s", hour, minute, amPm)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }


    LaunchedEffect(addUpdateReminderUiState) {
        when (val state = addUpdateReminderUiState) {
            is ReminderUiState.ErrorMessage -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.try_again_later),
                    Toast.LENGTH_SHORT
                ).show()
                dashboardViewModel.resetAddUpdateReminderUiState()
                dashboardViewModel.resetSelectedReminder()
            }


            is ReminderUiState.SuccessMessage -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                dashboardViewModel.resetAddUpdateReminderUiState()
                dashboardViewModel.resetSelectedReminder()
                navController.popBackStack()
            }


            else -> {}
        }
    }

    ReusableScaffold(
        title = if (editReminder?.reminderId.isNullOrEmpty()) stringResource(R.string.add_remainder) else stringResource(
            R.string.update_reminder
        ),
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .fillMaxWidth(0.95f),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (editReminder?.reminderId.isNullOrEmpty())
                            stringResource(R.string.add_remainder)
                        else
                            stringResource(R.string.update_reminder),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    ImagePicker(selectedReminderImage) { launcher.launch("image/*") }

                    OutlinedInputField(
                        label = stringResource(R.string.reminder_title),
                        value = reminderTitle,
                        onValueChange = { reminderTitle = it },
                        icon = R.drawable.title_icon
                    )

                    DateTimePickers(
                        selectedDate, selectedTime,
                        onDateClick = showDatePicker,
                        onTimeClick = showTimePicker,
                    )

                    OutlinedInputField(
                        label = stringResource(R.string.reminder_note),
                        value = reminderOptionalNote,
                        onValueChange = { reminderOptionalNote = it },
                        singleLine = false,
                        height = 120.dp,
                        icon = R.drawable.note_icon
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { navController.popBackStack() }) {
                            Text(stringResource(R.string.cancel))
                        }
                        Button(onClick = {
                            if (DateAndTimeUtil.isTimeInPast(selectedTime, selectedDate)) {
                                Toast.makeText(
                                    context,
                                    "Please choose a valid time.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (reminderTitle.trim().isEmpty())
                                Toast.makeText(
                                    context,
                                    "Please enter reminder title.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            else {
                                val reminder = Reminder(
                                    "",
                                    reminderTitle,
                                    selectedDate,
                                    selectedTime,
                                    selectedReminderImage.toString(),
                                    reminderOptionalNote
                                )

                                if (editReminder?.reminderId?.isNotEmpty() == true) {
                                    reminder.reminderId = editReminder.reminderId
                                    dashboardViewModel.updateReminder(reminder)
                                } else {
                                    dashboardViewModel.addReminder(reminder)
                                }
                            }
                        }) {
                            if (addUpdateReminderUiState is ReminderUiState.Loading)
                                CircularProgressIndicator(
                                    Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            else
                                Text(stringResource(R.string.save))
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun OutlinedInputField(
    label: String,
    value: String,
    icon : Int ,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    height: Dp = Dp.Unspecified
) {
    Column(modifier = Modifier.fillMaxWidth(0.9f)) {
        Text(label, modifier = Modifier.padding(start = 6.dp, bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = { Image(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(30.dp)) },
            placeholder = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .then(if (height != Dp.Unspecified) Modifier.height(height) else Modifier),
            singleLine = singleLine
        )
    }
}
