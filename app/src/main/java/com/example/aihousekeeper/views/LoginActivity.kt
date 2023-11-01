package com.example.aihousekeeper.views

import LoginActivityViewModelFactory
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.aihousekeeper.R
import com.example.aihousekeeper.databinding.ActivityLoginBinding
import com.example.aihousekeeper.datas.LoginUserRequest
import com.example.aihousekeeper.repositories.AuthRepository
import com.example.aihousekeeper.utils.APIService
import com.example.aihousekeeper.view_models.LoginActivityViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.toRegisterText.setOnClickListener(this)
        mBinding.loginBtn.setOnClickListener(this)
        mViewModel = ViewModelProvider(this, LoginActivityViewModelFactory(AuthRepository(APIService.getService()), application))[LoginActivityViewModel::class.java]
        setUpObservers()
    }

    private fun setUpObservers(){
        mViewModel.getIsLoading().observe(this){
            mBinding.progressBar.isVisible = it
        }
        mViewModel.getIsLoginCompleted().observe(this){
            if(it){
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
        mViewModel.getErrorMessage().observe(this){
            if(it.isNotEmpty()){
                mBinding.usernameTil.error = it
                mBinding.passwordTil.error = it
            }
            else{
                mBinding.usernameTil.error = null
                mBinding.passwordTil.error = null
            }
        }
    }

    private fun validateB4Submission(): Boolean{
        return mBinding.usernameTil.error == null
                && mBinding.passwordTil.error == null
                && mBinding.usernameEt.text!!.isNotEmpty()
                && mBinding.passwordEt.text!!.isNotEmpty()
    }

    override fun onClick(view: View?) {
        if(view == null){
            return
        }

        when(view.id){
            R.id.toRegisterText -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            R.id.loginBtn -> {
                if(validateB4Submission()){
                    mViewModel.loginUser(LoginUserRequest(username = mBinding.usernameEt.text.toString(), password = mBinding.passwordEt.text.toString()))
                }
            }
        }
    }
}