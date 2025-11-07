package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.repository.PostRepository;
import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.post.exception.PostInfraException;
import com.nashs.daily_log.infra.post.repository.PostJpaRepository;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.DELETED;
import static com.nashs.daily_log.infra.post.exception.PostInfraException.PostInfraExceptionCode.NO_SUCH_POST;

@Repository
@RequiredArgsConstructor
@Transactional
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public PostInfo findPostById(Long postId) {
        return postJpaRepository.findPostById(postId)
                                .orElseThrow(() -> new PostInfraException(NO_SUCH_POST))
                                .toInfo();
    }

    @Override
    public List<PostInfo> findAllPost() {
        return postJpaRepository.findAll()
                                .stream()
                                .map(Post::toInfo)
                                .toList();
    }

    @Override
    public List<PostInfo> findMyAllPost(String userSub) {
        return postJpaRepository.findByUser(User.builder()
                                                .sub(userSub)
                                                .build())
                                .stream()
                                .map(Post::toInfo)
                                .toList();
    }

    @Override
    public PostInfo savePost(PostInfo postInfo) {
        return postJpaRepository.save(Post.fromInfo(postInfo))
                                .toInfo();
    }

    @Override
    public boolean updatePostById(PostInfo postInfo) {
        return postJpaRepository.updatePostById(
                postInfo.getId(),
                Template.fromInfo(postInfo.getTemplateInfo()),
                postInfo.getTitle(),
                postInfo.getContent()
        ) > 0;
    }

    @Override
    public boolean deletePostById(PostInfo postInfo) {
        return postJpaRepository.deletePostById(
                postInfo.getId(),
                DELETED
        ) > 0;
    }
}
