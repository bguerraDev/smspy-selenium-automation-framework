package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private int id;
    private int sender;
    private String sender_username;
    private String sender_avatar_url;
    private int receiver;
    private String receiver_username;
    private String content;
    private String image;
    private String sent_at;
}
