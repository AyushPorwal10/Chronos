package remainder.chronos.core.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import remainder.chronos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    showLogout: Boolean = false,
    showFloatingActionButton: Boolean = false,
    onFloatingActionButtonClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            title?.let {
                TopAppBar(
                    title = { Text(it, fontWeight = FontWeight.SemiBold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    actions = {
                        if (showLogout) {
                            TextButton(onClick = {
                                onLogoutClick()
                            }, modifier = Modifier.size(42.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.logout),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                    }
                )
            }

        },

        floatingActionButton = {
            if (showFloatingActionButton) {
                FloatingActionButton(
                    onClick = onFloatingActionButtonClick,
                    modifier = Modifier
                        .clip(
                            CircleShape
                        ),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                    )
                }
            }

        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
