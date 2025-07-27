package remainder.chronos.core.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import remainder.chronos.data.repoimpl.AiMessageRepositoryImpl
import remainder.chronos.data.repoimpl.AuthRepositoryImpl
import remainder.chronos.data.repoimpl.DashboardRepositoryImpl
import remainder.chronos.data.repoimpl.ScheduleRepositoryImpl
import remainder.chronos.domain.repository.AiMessageRepository
import remainder.chronos.domain.repository.DashboardRepository
import remainder.chronos.domain.repository.AuthRepository
import remainder.chronos.domain.repository.ScheduleRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
   abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ) : AuthRepository

   @Binds
   @Singleton

    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ) : DashboardRepository

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl
    ):ScheduleRepository

    @Binds
    @Singleton
    abstract fun bindAiMessageRepository(
        aiMessageRepositoryImpl: AiMessageRepositoryImpl
    ) : AiMessageRepository
}