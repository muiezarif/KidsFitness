package com.muiezarif.kidsfitness.di.component

import com.muiezarif.kidsfitness.activities.*
import com.muiezarif.kidsfitness.di.modules.AppModule
import com.muiezarif.kidsfitness.di.modules.UtilsModule
import com.muiezarif.kidsfitness.di.modules.ViewModelsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class, ViewModelsModule::class, AppModule::class])
interface AppComponent {

    fun doInjection(activity: LoginActivity)
    fun doInjection(activity: RegisterActivity)
    fun doInjection(activity: ChildLoginRegisterActivity)
    fun doInjection(activity: CoachHomeActivity)
    fun doInjection(activity: StudentOnboardingActivity)
    fun doInjection(activity: ChildHomeActivity)
    fun doInjection(activity: ChildSettingsActivity)
    fun doInjection(activity: ChildLessonPartsActivity)
    fun doInjection(activity: CoachLoginRegisterActivity)
    fun doInjection(activity: ChildPartLessonFullViewActivity)
    fun doInjection(activity: SelectCategoryActivity)
    fun doInjection(activity: SplashActivity)
    fun doInjection(activity: SelectUserTypeActivity)
    fun doInjection(activity: ChapterVideosActivity)
    fun doInjection(activity: SelectCoachCategoriesActivity)
    fun doInjection(activity: CoachOnBoardingActivity)
//    fun doInjection(fragment: MoreFragment)
}