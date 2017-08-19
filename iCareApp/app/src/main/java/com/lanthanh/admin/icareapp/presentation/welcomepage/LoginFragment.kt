package com.lanthanh.admin.icareapp.presentation.welcomepage

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Typeface
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.lanthanh.admin.icareapp.R
import com.lanthanh.admin.icareapp.core.app.BaseFragment
import com.lanthanh.admin.icareapp.core.mvvm.MVVMFragment


import com.lanthanh.admin.icareapp.utils.GraphicUtils

import com.lanthanh.admin.icareapp.databinding.FragmentWelcomeLoginBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by ADMIN on 17-Oct-16.
 */

class LoginFragment : MVVMFragment<WelcomeActivity, LoginViewModel>() {

    private lateinit var binding: FragmentWelcomeLoginBinding

    override lateinit var viewModel: LoginViewModel
        @Inject set

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWelcomeLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.viewModel.navigator = hostActivity
        return binding.root
    }

    override fun initView() {
        val font = Typeface.createFromAsset(activity.assets, GraphicUtils.FONT_LIGHT)
        listOfNotNull<TextView>(binding.inputUsername, binding.inputPassword, binding.loginButton).forEach { it.typeface = font }
        hostActivity.supportActionBar?.setHomeButtonEnabled(true)
        hostActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

