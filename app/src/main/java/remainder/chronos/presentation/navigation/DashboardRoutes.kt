package remainder.chronos.presentation.navigation

sealed class DashboardRoutes(val route : String){
    object Dashboard : DashboardRoutes("dashboard")
    object AddReminder : DashboardRoutes("addReminder")
    object CreateAiMessage : DashboardRoutes("aiMessage")
}
