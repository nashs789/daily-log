package com.nashs.daily_log.domain.post.repository;

import com.nashs.daily_log.domain.post.info.PostInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository {
    PostInfo findPostById(Long postId);
    List<PostInfo> findAllPost();
    List<PostInfo> findMyAllPost(String userSub);
    PostInfo savePost(PostInfo postInfo);
    boolean updatePostById(PostInfo postInfo);
    boolean deletePostById(PostInfo postInfo);
}
