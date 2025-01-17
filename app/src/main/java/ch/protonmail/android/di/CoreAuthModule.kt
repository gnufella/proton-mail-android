/*
 * Copyright (c) 2022 Proton AG
 *
 * This file is part of Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see https://www.gnu.org/licenses/.
 */

package ch.protonmail.android.di

import android.content.Context
import ch.protonmail.android.feature.account.SetupAccountUserCheck
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.proton.core.accountmanager.domain.AccountManager
import me.proton.core.auth.data.MissingScopeListenerImpl
import me.proton.core.auth.data.repository.AuthRepositoryImpl
import me.proton.core.auth.domain.repository.AuthRepository
import me.proton.core.auth.domain.usecase.LoginChallengeConfig
import me.proton.core.auth.domain.usecase.PostLoginAccountSetup
import me.proton.core.auth.domain.usecase.signup.SignupChallengeConfig
import me.proton.core.auth.presentation.AuthOrchestrator
import me.proton.core.auth.presentation.ui.LoginActivity
import me.proton.core.crypto.android.srp.GOpenPGPSrpCrypto
import me.proton.core.crypto.common.srp.SrpCrypto
import me.proton.core.domain.entity.Product
import me.proton.core.network.data.ApiProvider
import me.proton.core.network.domain.scopes.MissingScopeListener
import me.proton.core.user.domain.UserManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreAuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiProvider: ApiProvider,
        @ApplicationContext context: Context,
        product: Product
    ): AuthRepository =
        AuthRepositoryImpl(apiProvider, context, product)

    @Provides
    fun provideAuthOrchestrator(): AuthOrchestrator =
        AuthOrchestrator()

    @Provides
    @Singleton
    fun provideSrpCrypto(): SrpCrypto =
        GOpenPGPSrpCrypto()

    @Provides
    @Singleton
    fun provideUserCheck(
        @ApplicationContext context: Context,
        accountManager: AccountManager,
        userManager: UserManager,
        oldUserManager: ch.protonmail.android.core.UserManager,
    ): PostLoginAccountSetup.UserCheck = SetupAccountUserCheck(context, accountManager, userManager, oldUserManager)

    // region missing scopes
    @Provides
    @Singleton
    fun provideMissingScopeListener(): MissingScopeListener = MissingScopeListenerImpl()
    // endregion

    @Provides
    @Singleton
    fun provideLoginBlockingHelp(): LoginActivity.BlockingHelp? = null

    @Provides
    @Singleton
    fun provideChallengeConfig(): SignupChallengeConfig = SignupChallengeConfig()

    @Provides
    @Singleton
    fun provideLoginChallengeConfig(): LoginChallengeConfig = LoginChallengeConfig()
}
