package com.example.aihousekeeper.views

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.aihousekeeper.R
import com.example.aihousekeeper.databinding.ActivityRegisterBinding
import com.example.aihousekeeper.datas.ValidateUsernameRequest
import com.example.aihousekeeper.repositories.AuthRepository
import com.example.aihousekeeper.utils.APIService
import com.example.aihousekeeper.view_models.RegisterActivityViewModel
import com.example.aihousekeeper.view_models.RegisterActivityViewModelFactory

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnKeyListener, View.OnFocusChangeListener {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mViewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create an instance of the binding class
        mBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.usernameEt.onFocusChangeListener = this
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.passwordEt.onFocusChangeListener = this
        mBinding.confirmPasswordEt.onFocusChangeListener = this
        mViewModel = ViewModelProvider(this, RegisterActivityViewModelFactory(AuthRepository(APIService.getService()), application))
            .get(RegisterActivityViewModel::class.java)
        setUpObservers()
    }

    private fun setUpObservers(){
        mViewModel.getIsLoading().observe(this){
            mBinding.progressBar.isVisible = it
        }
        mViewModel.getIsUsernameUnique().observe(this){
            if(it){
                mBinding.usernameTil.apply {
                    setStartIconDrawable(R.drawable.check_circle_24)
                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                    error = null
                }
            }
            else if(!it && mBinding.usernameEt.text.toString().isNotEmpty()){
                mBinding.usernameTil.apply {
                    error = "Username is already taken"
                    startIconDrawable = null
                }
            }
        }
        mViewModel.getErrorMessage().observe(this){
            if(it.contains("username")){
                mBinding.usernameTil.apply {
                    error = it
                }
            }
            else if(it.contains("email")){
                mBinding.emailTil.apply {
                    error = it
                }
            }
            else if(it.contains("password")){
                mBinding.passwordTil.apply {
                    error = it
                }
            }
        }
    }

    private fun validateUsername(): Boolean{
        var errorMsg: String? = null
        val value = mBinding.usernameEt.text.toString()
        if(value.isEmpty()){
            errorMsg = "Username is required"
        }

        if(errorMsg != null){
            mBinding.usernameEt.apply {
                error = errorMsg
            }
        }

        return errorMsg == null
    }

    private fun validateEmail(): Boolean{
        var errorMsg: String? = null
        val value = mBinding.emailEt.text.toString()
        if(value.isEmpty()){
            errorMsg = "Email is required"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMsg = "Invalid email address"
        }

        mBinding.emailTil.apply {
            error = errorMsg
        }

        return errorMsg == null
    }

    private fun validatePw(): Boolean{
        var errorMsg: String? = null
        val value = mBinding.passwordEt.text.toString()
        if(value.isEmpty()){
            errorMsg = "Password is required"
        }
        else if(value.length < 6){
            errorMsg = "Password must have length >= 6"
        }

        if(errorMsg != null){
            mBinding.passwordTil.apply {
                error = errorMsg
                startIconDrawable = null
            }
        }

        return errorMsg == null
    }

    private fun validateConfirmPw(): Boolean{
        var errorMsg: String? = null
        val value = mBinding.confirmPasswordEt.text.toString()
        val pw = mBinding.passwordEt.text.toString()
        if(value.isEmpty()){
            errorMsg = "This field is required"
        }
        else if(pw != value){
            errorMsg = "Not the same as password"
        }

        if(errorMsg != null){
            mBinding.confirmPasswordTil.apply {
                error = errorMsg
                startIconDrawable = null
            }
        }

        return errorMsg == null
    }

    override fun onClick(p0: View?) {
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        return false
    }

    override fun onFocusChange(view: View?, isFocused: Boolean) {
        if(view != null){
            when(view.id){
                R.id.usernameEt -> {
                    if(!isFocused){
                        if(validateUsername()){
                            mViewModel.validateUsername(ValidateUsernameRequest(username = mBinding.usernameEt.text.toString()))
                        }
                    }
                }
                R.id.emailEt -> {
                    if(!isFocused){
                        validateEmail()
                    }
                }
                R.id.passwordEt -> {
                    if(!isFocused
                        && validatePw()){
                        mBinding.passwordTil.apply {
                            setStartIconDrawable(R.drawable.check_circle_24)
                            setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            error = null
                        }
                    }
                }
                R.id.confirmPasswordEt -> {
                    if(!isFocused
                        && validatePw()
                        && mBinding.confirmPasswordEt.text!!.toString().isNotEmpty()
                        && validateConfirmPw()){
                        mBinding.confirmPasswordTil.apply {
                            setStartIconDrawable(R.drawable.check_circle_24)
                            setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            error = null
                        }
                    }
                }
            }
        }
    }
}