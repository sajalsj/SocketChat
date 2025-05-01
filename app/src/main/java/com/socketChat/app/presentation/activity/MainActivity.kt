package com.socketChat.app.presentation.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.socketChat.app.databinding.ActivityMainBinding
import com.socketChat.app.presentation.fragment.ConversationFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, ConversationFragment())
            .commit()
    }


}