package project.back.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import project.back.chat.JwtTokenProvider;
import project.back.chat.dto.ChatMessage;
import project.back.chat.repository.ChatRoomRepository;
import project.back.chat.service.ChatService;

//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/chat")
//public class ChatController {
//
//    private final ChatService chatService;
//
//    @PostMapping
//    public ChatRoom createRoom(@RequestParam String name) {
//        return chatService.createRoom(name);
//    }
//
//    @GetMapping
//    public List<ChatRoom> findAllRoom() {
//        return chatService.findAllRoom();
//    }
//}


/**
 * before applying redis
 */
//@RequiredArgsConstructor
//@RestController
//public class ChatController {
//
//    private final SimpMessageSendingOperations messagingTemplate;
//
//    @MessageMapping("/chat/message")
//    public void message(ChatMessage message) {
//        if (ChatMessage.MessageType.JOIN.equals(message.getType()))
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }
//}

/**
 * applying redis version
 */
//@RequiredArgsConstructor
//@Controller
//public class ChatController {
//
//    private final RedisPublisher redisPublisher;
//    private final ChatRoomRepository chatRoomRepository;
//
//    /**
//     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
//     */
//    @MessageMapping("/chat/message")
//    public void message(ChatMessage message) {
//        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//            chatRoomRepository.enterChatRoom(message.getRoomId());
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//        }
//        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
//        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
//    }
//}

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        message.setSender(nickname);
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
        chatService.sendChatMessage(message);
    }
}