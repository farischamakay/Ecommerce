package com.example.ecommerce.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ecommerce.MainActivity
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.utils.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogout.setOnClickListener {
            firebaseAnalytics.logEvent(Constants.BUTTON_CLICK) {
                param(Constants.BUTTON_NAME, "btn_logout")
            }
            (requireActivity() as MainActivity).logOut()
        }

        val currentLangage = AppCompatDelegate.getApplicationLocales()
        binding.switchLanguage.isChecked = currentLangage == LocaleListCompat.forLanguageTags("ID")
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            firebaseAnalytics.logEvent(Constants.BUTTON_CLICK) {
                param(Constants.BUTTON_NAME, "switch_language")
            }

            if (isChecked) {
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("ID")
                AppCompatDelegate.setApplicationLocales(appLocale)
            } else {
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("EN")
                AppCompatDelegate.setApplicationLocales(appLocale)
            }

        }


        binding.switchTheme.isChecked = viewModel.isDarkThemeMode()
        if (viewModel.isDarkThemeMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            firebaseAnalytics.logEvent(Constants.BUTTON_CLICK) {
                param(Constants.BUTTON_NAME, "switch_theme")
            }

            viewModel.saveTheme(isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding != null
    }

}