package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * applying redis
 */
@Getter
@Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}

/**
 * before applying redis
 */
//@Getter
//@Setter
//public class ChatRoom {
//    private String roomId;
//    private String name;
//
//    public static ChatRoom create(String name){
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.roomId = UUID.randomUUID().toString();
//        chatRoom.name = name;
//        return chatRoom;
//    }
//}

/**
 *  <stomp로 구현전>
 */
//    private Set<WebSocketSession> sessions = new HashSet<>();
//
//    @Builder
//    public ChatRoom(String roomId, String name) {
//        this.roomId = roomId;
//        this.name = name;
//    }
//
//    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
//        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
//            sessions.add(session);
//            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
//        }
//        sendMessage(chatMessage, chatService);
//    }
//
//    public <T> void sendMessage(T message, ChatService chatService) {
//        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
//    }



////채팅방 구현위한 dto
//@Getter
//public class ChatRoom {
//    private String roomId;
//    private String roomName;
//    //입장한 클라이언트들의 정보 저장
//    private Set<WebSocketSession> sessions = new HashSet<>();
//
//    @Builder
//    public ChatRoom(String roomId, String roomName) {
//        this.roomId = roomId;
//        this.roomName = roomName;
//    }
//
//    //채팅방에는 입장, 대화하기의 기능이 있다
//    public void handleActions
//}
