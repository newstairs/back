package project.back.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@Setter
public class ChatMessage {
    //메시지 타임 : 입장(ENTER) , 채팅(TALK)
    public enum MessageType{
        ENTER,TALK,JOIN,QUIT
    }
    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
    }

    private MessageType type; //메시지 타입
    private String roomId;    //방번호
    private String sender;    //보내는이
    private String message;   //메시지 내용
    private long userCount;   // 채팅방 인원수, 채팅방 내에서 메시지가 전달될떄 인원수 갱신시 사용
}
