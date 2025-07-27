package remainder.chronos.presentation.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import remainder.chronos.R
import remainder.chronos.presentation.auth.viewmodel.AuthViewmodel
import remainder.chronos.presentation.dashboard.screen.DashboardScreen
import remainder.chronos.presentation.navigation.AuthRoutes
import remainder.chronos.presentation.navigation.DashboardRoutes


@Composable
fun SplashScreen(navController: NavController) {

    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("auth")
    }
    val authViewmodel: AuthViewmodel = hiltViewModel(parentEntry)

    LaunchedEffect(Unit) {
        delay(1000L)
        if (authViewmodel.isLoggedIn()) {
            navController.navigate("home") {
                popUpTo("auth") { inclusive = true }
            }
        } else {
            navController.navigate(AuthRoutes.LoginSignUp.route)
        }
    }



        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Text(
                stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleLarge
            )

        }

}