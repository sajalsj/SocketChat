package com.socketChat.app.presentation.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socketChat.app.domain.model.ConversationModel
import com.socketChat.app.databinding.ItemConversationBinding

class ConversationAdapter(
    private var conversations: List<ConversationModel>,
    private val onItemClick: (ConversationModel) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    fun updateList(newList: List<ConversationModel>) {
        conversations = newList
        notifyDataSetChanged()
    }

    inner class ConversationViewHolder(private val binding: ItemConversationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: ConversationModel) {
            binding.botName.text = conversation.botName
            binding.lastMessage.text = conversation.lastMessage
            binding.unreadCount.text =
                if (conversation.unreadCount > 0) conversation.unreadCount.toString() else ""

            binding.root.setOnClickListener {
                onItemClick(conversation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationViewHolder(binding)
    }

    override fun getItemCount() = conversations.size

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(conversations[position])
    }
}

