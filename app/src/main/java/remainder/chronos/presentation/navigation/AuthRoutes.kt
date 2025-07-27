package remainder.chronos.presentation.navigation

sealed class AuthRoutes (val route : String ){
    object LoginSignUp : AuthRoutes("loginsignup")
    object SplashScreen : AuthRoutes("splash")
}