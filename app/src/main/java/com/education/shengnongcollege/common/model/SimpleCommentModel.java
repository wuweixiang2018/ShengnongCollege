package com.education.shengnongcollege.common.model;

import java.io.Serializable;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class SimpleCommentModel implements Serializable {
    public String nickname;
    public String content;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
