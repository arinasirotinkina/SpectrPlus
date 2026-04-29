package com.example.spectrplus.service.social


import com.example.spectrplus.dto.social.*
import com.example.spectrplus.entity.profile.AccountRole
import com.example.spectrplus.entity.social.*
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.repository.social.*
import org.springframework.stereotype.Service


@Service
class ChatService(
    private val chatRepo: ChatRepository,
    private val messageRepo: MessageRepository,
    private val userRepo: UserRepository
) {

    fun getOrCreateChat(userId: Long, otherUserId: Long): ChatEntity {

        val existing = chatRepo.findByUser1IdAndUser2Id(userId, otherUserId)
            ?: chatRepo.findByUser2IdAndUser1Id(userId, otherUserId)

        return existing ?: chatRepo.save(
            ChatEntity(
                user1Id = userId,
                user2Id = otherUserId
            )
        )
    }

    fun getChats(userId: Long): List<ChatResponse> {

        return chatRepo.findAllByUser(userId).map { chat ->

            val otherUserId =
                if (chat.user1Id == userId) chat.user2Id else chat.user1Id

            val otherUser = userRepo.findById(otherUserId).orElseThrow()

            val lastMessage = messageRepo
                .findByChatIdOrderByCreatedAtAsc(chat.id)
                .lastOrNull()?.content

            ChatResponse(
                id = chat.id,
                otherUserId = otherUserId,
                otherUserName = "${otherUser.firstName} ${otherUser.lastName}",
                lastMessage = lastMessage
            )
        }
    }

    fun getMessages(chatId: Long): List<MessageResponse> {
        return messageRepo.findByChatIdOrderByCreatedAtAsc(chatId)
            .map {
                MessageResponse(
                    id = it.id,
                    chatId = it.chatId,
                    senderId = it.senderId,
                    senderProfessionalLabel = it.senderProfessionalLabel,
                    content = it.content,
                    createdAt = it.createdAt.toString()
                )
            }
    }

    fun sendMessage(
        userId: Long,
        chatId: Long,
        req: SendMessageRequest
    ): MessageEntity {

        val sender = userRepo.findById(userId).orElseThrow()
        val label = if (sender.accountRole == AccountRole.SPECIALIST) sender.specialistProfession else null

        val saved = messageRepo.save(
            MessageEntity(
                chatId = chatId,
                senderId = userId,
                senderProfessionalLabel = label,
                content = req.content
            )
        )

        return saved
    }
}
