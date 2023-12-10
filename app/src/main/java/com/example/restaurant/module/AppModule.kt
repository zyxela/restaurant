package com.example.restaurant.module

import com.example.restaurant.repository.AdminPanelRepository
import com.example.restaurant.repository.CartRepository
import com.example.restaurant.repository.EditMenuRepository
import com.example.restaurant.repository.EnterRepository
import com.example.restaurant.repository.MenuRepository
import com.example.restaurant.repositoryImpl.AdminPanelRepositoryImpl
import com.example.restaurant.repositoryImpl.CartRepositoryImpl
import com.example.restaurant.repositoryImpl.EditMenuRepositoryImpl
import com.example.restaurant.repositoryImpl.EnterRepositoryImpl
import com.example.restaurant.repositoryImpl.MenuRepositoryImpl
import com.example.restaurant.viewModels.AdminPanelViewModel
import com.example.restaurant.viewModels.CartViewModel
import com.example.restaurant.viewModels.EditMenuViewModel
import com.example.restaurant.viewModels.EnterViewModel
import com.example.restaurant.viewModels.MenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    factory<EnterRepository> { EnterRepositoryImpl() }
    viewModel { EnterViewModel(get()) }

    factory<MenuRepository> { MenuRepositoryImpl() }
    viewModel { MenuViewModel(get()) }

    factory<CartRepository> { CartRepositoryImpl() }
    viewModel { CartViewModel(get()) }

    factory<AdminPanelRepository> { AdminPanelRepositoryImpl() }
    viewModel { AdminPanelViewModel(get()) }

    factory<EditMenuRepository> { EditMenuRepositoryImpl() }
    viewModel{EditMenuViewModel(get())}
}