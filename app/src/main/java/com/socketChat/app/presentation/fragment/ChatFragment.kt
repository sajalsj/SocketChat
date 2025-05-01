package com.socketChat.app.presentation.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.socketChat.app.presentation.adapter.ChatAdapter
import com.socketChat.app.utils.Status
import com.socketChat.app.utils.WebSocketListener
import com.socketChat.app.data.repository.ChatRepositoryImpl
import com.socketChat.app.databinding.FragmentChatBinding
import com.socketChat.app.presentation.viewmodel.ChatViewModel
import com.socketChat.app.presentation.viewmodel.ChatViewModelFactory
import com.socketChat.app.utils.SocketUtil
import okhttp3.OkHttpClient
import okhttp3.WebSocket

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var webSocket: WebSocket
    private val adapter = ChatAdapter()
    private var isConnected = false
    private var botName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botName = arguments?.getString("botName") ?: "UnknownBot"

        val repository = ChatRepositoryImpl(requireContext())
        val factory = ChatViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]
        binding.toolbarBotName.title = botName

        setupRecyclerView()
        val listener = WebSocketListener(viewModel, botName)
        val client = OkHttpClient()
        webSocket = client.newWebSocket(SocketUtil.createSocketRequest(), listener)
        repository.setWebSocket(webSocket)
        viewModel.loadChatHistory(botName)
        setupClickListeners()
        observeLiveData()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.imgSend.setOnClickListener {
            val message = binding.edtText.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message, botName, isOnline = isConnected)
                binding.edtText.text.clear()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.status.observe(viewLifecycleOwner) {
            val statusMessage = when (it) {
                Status.CONNECTED -> {
                    isConnected = true
                    viewModel.retryQueuedMessages(botName)
                    "Channel Connected"
                }
                Status.DISCONNECTED -> {
                    isConnected = false
                    "Channel Disconnected"
                }
                else -> "Channel Disconnected"
            }
            Toast.makeText(requireContext(), statusMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.recyclerView.scrollToPosition(it.size - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webSocket.close(1001, "No longer needed")
        _binding = null
    }
}

