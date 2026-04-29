package com.example.spectrplus

import android.app.Application
import com.example.spectrplus.core.network.AuthInterceptor
import com.example.spectrplus.data.api.education.ArticleApi
import com.example.spectrplus.data.api.auth.AuthApi
import com.example.spectrplus.data.api.education.FileUploadApi
import com.example.spectrplus.data.api.profile.SpecialistApi
import com.example.spectrplus.data.api.social.ChatApi
import com.example.spectrplus.data.api.profile.ChildApi
import com.example.spectrplus.data.api.social.ForumApi
import com.example.spectrplus.data.api.education.MaterialApi
import com.example.spectrplus.data.api.profile.PlanApi
import com.example.spectrplus.data.api.profile.ProfileApi
import com.example.spectrplus.data.api.profile.UserApi
import com.example.spectrplus.data.api.education.VideoApi
import com.example.spectrplus.data.repository.education.ArticleRepositoryImpl
import com.example.spectrplus.data.repository.auth.AuthRepositoryImpl
import com.example.spectrplus.data.repository.profile.SpecialistRepositoryImpl
import com.example.spectrplus.data.repository.social.ChatRepositoryImpl
import com.example.spectrplus.data.repository.profile.ChildRepositoryImpl
import com.example.spectrplus.data.repository.social.ForumRepositoryImpl
import com.example.spectrplus.data.repository.education.MaterialRepositoryImpl
import com.example.spectrplus.data.repository.profile.PlanRepositoryImpl
import com.example.spectrplus.data.repository.profile.ProfileRepositoryImpl
import com.example.spectrplus.data.repository.education.VideoRepositoryImpl
import com.example.spectrplus.data.repository.social.UserRepositoryImpl
import com.example.spectrplus.domain.repository.education.ArticleRepository
import com.example.spectrplus.domain.repository.auth.AuthRepository
import com.example.spectrplus.domain.repository.profile.SpecialistRepository
import com.example.spectrplus.domain.repository.social.ChatRepository
import com.example.spectrplus.domain.repository.profile.ChildRepository
import com.example.spectrplus.domain.repository.social.ForumRepository
import com.example.spectrplus.domain.repository.education.MaterialRepository
import com.example.spectrplus.domain.repository.profile.PlanRepository
import com.example.spectrplus.domain.repository.profile.ProfileRepository
import com.example.spectrplus.domain.repository.education.VideoRepository
import com.example.spectrplus.domain.repository.social.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        System.setProperty("java.net.preferIPv4Stack", "true")
    }
}
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttp(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }
    @Provides
    @Singleton
    fun provideChildApi(retrofit: Retrofit): ChildApi {
        return retrofit.create(ChildApi::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleApi(retrofit: Retrofit): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMaterialApi(retrofit: Retrofit): MaterialApi {
        return retrofit.create(MaterialApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVideoApi(retrofit: Retrofit): VideoApi {
        return retrofit.create(VideoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideForumApi(retrofit: Retrofit): ForumApi {
        return retrofit.create(ForumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSpecialistApi(retrofit: Retrofit): SpecialistApi {
        return retrofit.create(SpecialistApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFileUploadApi(retrofit: Retrofit): FileUploadApi {
        return retrofit.create(FileUploadApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSpecialistRepository(
        specialistApi: SpecialistApi,
        fileUploadApi: FileUploadApi
    ): SpecialistRepository {
        return SpecialistRepositoryImpl(specialistApi, fileUploadApi)
    }

    @Provides
    @Singleton
    fun providePlanApi(retrofit: Retrofit): PlanApi {
        return retrofit.create(PlanApi::class.java)
    }

    @Provides
    fun providePlanRepository(api: PlanApi): PlanRepository {
        return PlanRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(api: ProfileApi): ProfileRepository {
        return ProfileRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideChildRepository(api: ChildApi): ChildRepository {
        return ChildRepositoryImpl(api)
    }

    @Provides
    fun provideArticleRepository(
        api: ArticleApi,
    ): ArticleRepository {
        return ArticleRepositoryImpl(api)
    }

    @Provides
    fun provideMaterialRepository(
        api: MaterialApi,
    ): MaterialRepository {
        return MaterialRepositoryImpl(api)
    }

    @Provides
    fun provideVideoRepository(
        api: VideoApi,
    ): VideoRepository {
        return VideoRepositoryImpl(api)
    }

    @Provides
    fun provideForumRepository(
        api: ForumApi,
    ): ForumRepository {
        return ForumRepositoryImpl(api)
    }

    @Provides
    fun provideChatRepository(
        api: ChatApi,
    ): ChatRepository {
        return ChatRepositoryImpl(api)
    }

    @Provides
    fun provideUserRepository(
        api: UserApi
    ) : UserRepository {
        return UserRepositoryImpl(api)
    }
}
//string@gmail.com