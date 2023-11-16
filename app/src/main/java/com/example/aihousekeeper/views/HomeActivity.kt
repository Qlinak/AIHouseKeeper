package com.example.aihousekeeper.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.aihousekeeper.R
import com.example.aihousekeeper.databinding.ActivityHomeBinding
import com.example.aihousekeeper.datas.PromptRequest
import com.example.aihousekeeper.repositories.AiRepository
import com.example.aihousekeeper.utils.APIService
import com.example.aihousekeeper.view_models.HomeActivityViewModel
import com.example.aihousekeeper.view_models.HomeActivityViewModelFactory
import java.util.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var mViewModel: HomeActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.speechToTextBtn.setOnClickListener(this)
        mBinding.askAiBtn.setOnClickListener(this)
        mViewModel = ViewModelProvider(
            this,
            HomeActivityViewModelFactory(
                AiRepository(APIService.getService(), application),
                application
            )
        )[HomeActivityViewModel::class.java]
        setUpObservers()
    }

    private fun setUpObservers() {
        mViewModel.getIsLoading().observe(this) {
            // ignore for now
        }
        mViewModel.getDisplayMessage().observe(this) {
            mBinding.displayBox.setText(it)
        }
        mViewModel.getErrorMessage().observe(this) {
            if (it.isNotEmpty()) {
                mBinding.displayBox.error = it
            } else {
                mBinding.displayBox.error = null
            }
        }
    }

    override fun onClick(view: View?) {
        if (view == null) {
            return
        }

        when (view.id) {
            R.id.speechToTextBtn -> {
                mBinding.displayBox.text = null
                try {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault()
                    )
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something")
                    result.launch(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.askAiBtn -> {
                mViewModel.askAi(body = PromptRequest(userId = 5, content = mBinding.displayBox.text.toString()))
            }
        }
    }

    val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val results = result.data?.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                ) as ArrayList<String>

                mBinding.displayBox.setText(results[0])
            }
        }
}