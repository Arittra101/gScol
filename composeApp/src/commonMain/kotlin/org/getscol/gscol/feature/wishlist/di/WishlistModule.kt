package org.getscol.gscol.feature.wishlist.di

import org.getscol.gscol.feature.compare.presentation.CompareViewModel
import org.getscol.gscol.feature.wishlist.data.api_service.WishlistApiService
import org.getscol.gscol.feature.wishlist.data.api_service.WishlistApiServiceImpl
import org.getscol.gscol.feature.wishlist.data.repository.WishlistRepositoryImpl
import org.getscol.gscol.feature.wishlist.domain.repository.WishlistRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wishlistModule = module {
    singleOf(::WishlistApiServiceImpl).bind<WishlistApiService>()
    singleOf(::WishlistRepositoryImpl).bind<WishlistRepository>()
    viewModelOf(::CompareViewModel)
}
