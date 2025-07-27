package remainder.chronos.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import remainder.chronos.presentation.auth.screen.LoginSignUpScreen
import remainder.chronos.presentation.auth.screen.SplashScreen
import remainder.chronos.presentation.dashboard.screen.AddReminderScreen
import remainder.chronos.presentation.dashboard.screen.CreateAndShareAiMessage
import remainder.chronos.presentation.dashboard.screen.DashboardScreen

fun NavGraphBuilder.authGraph(navController: NavController){
    navigation(startDestination = AuthRoutes.SplashScreen.route, route = "auth"){

        composable(AuthRoutes.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(AuthRoutes.LoginSignUp.route){
            LoginSignUpScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.dashboardGraph(navController: NavController){

    navigation(startDestination = DashboardRoutes.Dashboard.route , route = "home"){
        composable(DashboardRoutes.Dashboard.route){
            DashboardScreen(navController)
        }
        composable(DashboardRoutes.AddReminder.route){
            AddReminderScreen(navController)
        }
        composable(DashboardRoutes.CreateAiMessage.route){
            CreateAndShareAiMessage(navController)
        }

    }
}