package com.muiezarif.kidsfitness.di.modules

import androidx.lifecycle.ViewModel
import com.muiezarif.kidsfitness.activities.viewmodels.*
import com.muiezarif.kidsfitness.di.keys.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLogInViewModel(viewModel: LoginViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(ChildHomeViewModel::class)
    abstract fun bindChildHomeViewModel(viewModel: ChildHomeViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(ChildLessonPartsViewModel::class)
    abstract fun bindChildLessonPartsViewModel(viewModel: ChildLessonPartsViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel
}