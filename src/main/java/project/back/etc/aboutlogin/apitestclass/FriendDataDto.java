package project.back.etc.aboutlogin.apitestclass;

import lombok.Data;
import project.back.etc.aboutlogin.Item;

import java.util.List;

@Data
public class FriendDataDto {


    private List<String> friend_uuid;
    private List<Item> item_list;

}
