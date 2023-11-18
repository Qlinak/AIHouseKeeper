package com.example.aihousekeeper.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
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
    private val chatMessages: MutableList<String> = mutableListOf()

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
        mViewModel.getDisplayMessage().observe(this) { message ->
            val aiResponse = message
            val userMessage = mBinding.displayBox.text.toString()

            if (userMessage.isNotEmpty()) {
                chatMessages.add(userMessage)
            }

            if (aiResponse.isNotEmpty()) {
                chatMessages.add(aiResponse)
            }



            updateChatHistory()
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
                val userMessage = mBinding.displayBox.text.toString()
                mViewModel.askAi(
                    PromptRequest(
                        userId = 2,
                        content = userMessage
                    )
                )
                mBinding.displayBox.text = null
            }
        }
    }

    private fun updateChatHistory() {
        val layoutInflater = LayoutInflater.from(this)
        val chatMessagesLayout = mBinding.chatMessagesLayout
        chatMessagesLayout.removeAllViews()

        for (i in chatMessages.indices) {
            val message = chatMessages[i]
            val messageView =
                layoutInflater.inflate(R.layout.message_item, chatMessagesLayout, false)
            val messageTextView = messageView.findViewById<TextView>(R.id.messageTextView)
            messageTextView.text = message

            // Set alignment based on message type
            val alignment = if (i % 3 != 0) {
                Gravity.START // AI response alignment (left)
            } else {
                Gravity.END // User message alignment (right)
            }
            messageTextView.gravity = alignment

            // Set background drawable based on message type
            val backgroundDrawable = if (i % 3 != 0) {
                R.drawable.bubble_green // AI response background
            } else {
                R.drawable.bubble_blue // User message background
            }
            messageTextView.setBackgroundResource(backgroundDrawable)


            // Set head icon based on message type
            val headIconDrawable = if (i % 3 != 0) {
                R.drawable.ai // AI response head icon
            } else {
                R.drawable.user // User message head icon
            }
            headIconImageView.setImageResource(headIconDrawable)

            chatMessagesLayout.addView(messageView)
        }

        chatMessagesLayout.post {
            val scrollView = mBinding.scrollView
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
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