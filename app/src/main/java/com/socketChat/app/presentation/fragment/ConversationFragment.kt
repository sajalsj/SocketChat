package com.socketChat.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.socketChat.app.presentation.adapter.ConversationAdapter
import com.socketChat.app.R
import com.socketChat.app.data.repository.ChatRepositoryImpl
import com.socketChat.app.databinding.FragmentConversationBinding
import com.socketChat.app.presentation.viewmodel.ConversationViewModel
import com.socketChat.app.presentation.viewmodel.ConversationViewModelFactory

class ConversationFragment : Fragment() {

    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ConversationViewModel
    private lateinit var adapter: ConversationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ConversationAdapter(emptyList()) {
            openChatFragment(it.botName)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        val repository = ChatRepositoryImpl(requireContext())
        val factory = ConversationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ConversationViewModel::class.java]
        viewModel.conversations.observe(viewLifecycleOwner) { conversations ->
            adapter.updateList(conversations)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getLatestMessage()
    }

    private fun openChatFragment(botName: String) {
        val chatFragment = ChatFragment()
        val bundle = Bundle()
        bundle.putString("botName", botName)
        chatFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, chatFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
