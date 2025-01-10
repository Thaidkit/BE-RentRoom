package com.n3c3.rentroom.dto;

import com.n3c3.rentroom.entity.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDTO {
    private Post post;
    private List<CommentPostDetailDTO> commentPostDetaiDTOList;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<CommentPostDetailDTO> getCommentPostDetaiDTOList() {
        return commentPostDetaiDTOList;
    }

    public void setCommentPostDetaiDTOList(List<CommentPostDetailDTO> commentPostDetaiDTOList) {
        this.commentPostDetaiDTOList = commentPostDetaiDTOList;
    }
}
