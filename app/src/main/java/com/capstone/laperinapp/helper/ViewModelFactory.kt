package com.capstone.laperinapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.SettingPreferences
import com.capstone.laperinapp.di.Injection
import com.capstone.laperinapp.ui.detail.DetailViewModel
import com.capstone.laperinapp.ui.donasi.DonasiViewModel
import com.capstone.laperinapp.ui.donasi.add.AddDonationViewModel
import com.capstone.laperinapp.ui.donasi.detail.DetailDonationViewModel
import com.capstone.laperinapp.ui.donasi.saya.DonasiSayaViewModel
import com.capstone.laperinapp.ui.koleksi.KoleksiViewModel
import com.capstone.laperinapp.ui.profile.editProfile.EditViewModel
import com.capstone.laperinapp.ui.home.HomeViewModel
import com.capstone.laperinapp.ui.home.all_recipes.AllRecipesViewModel
import com.capstone.laperinapp.ui.home.search.SearchRecipesViewModel
import com.capstone.laperinapp.ui.login.LoginViewModel
import com.capstone.laperinapp.ui.profile.ProfileViewModel
import com.capstone.laperinapp.ui.profile.setting.SettingViewModel
import com.capstone.laperinapp.ui.register.RegisterViewModel
import com.capstone.laperinapp.ui.scan.preview.PreviewViewModel
import com.capstone.laperinapp.ui.scan.recommendation.RecommendationViewModel
import com.capstone.laperinapp.ui.scan.result.ResultViewModel
import com.capstone.laperinapp.ui.splashScreen.SplashViewModel

class ViewModelFactory private constructor(private val repository: Repository, private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->{
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository, pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditViewModel::class.java) -> {
                EditViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repository, pref) as T
            }
            modelClass.isAssignableFrom(DonasiViewModel::class.java) -> {
                DonasiViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PreviewViewModel::class.java) -> {
                PreviewViewModel(repository) as T
            }
            modelClass.isAssignableFrom(KoleksiViewModel::class.java) -> {
                KoleksiViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddDonationViewModel::class.java) -> {
                AddDonationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DonasiSayaViewModel::class.java) -> {
                DonasiSayaViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchRecipesViewModel::class.java) -> {
                SearchRecipesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RecommendationViewModel::class.java) -> {
                RecommendationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailDonationViewModel::class.java) -> {
                DetailDonationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AllRecipesViewModel::class.java) -> {
                AllRecipesViewModel(repository) as T
            }
             else ->    throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), Injection.provideSettingPref(context))
            }.also { instance = it }

        fun clearInstance(){
            Repository.clearInstance()
            instance = null
        }
    }
}