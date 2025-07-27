package remainder.chronos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import remainder.chronos.presentation.auth.screen.LoginSignUpScreen
import remainder.chronos.presentation.auth.viewmodel.AuthViewmodel
import remainder.chronos.presentation.navigation.authGraph
import remainder.chronos.presentation.navigation.dashboardGraph
import remainder.chronos.ui.theme.ChronosTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChronosTheme(dynamicColor =  false) {
                val navigationController = rememberNavController()
                NavHost(navController = navigationController , startDestination = "auth") {
                    authGraph(navigationController)
                    dashboardGraph(navigationController)
                }
            }
        }
    }
}

