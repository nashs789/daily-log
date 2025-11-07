package com.nashs.daily_log.domain.post.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.exception.PostDomainException;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nashs.daily_log.domain.post.exception.PostDomainException.PostDomainExceptionCode.NOT_POST_OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostInfo findPostById(Long postId) {
        return postRepository.findPostById(postId);
    }

    public List<PostInfo> findAllPost() {
        return postRepository.findAllPost()
                             .stream()
                             .filter(e -> e.getStatus().isNormal())
                             .toList();
    }

    public List<PostInfo> findMyAllPost(LifeLogUser lifeLogUser) {
        return postRepository.findMyAllPost(lifeLogUser.sub())
                             .stream()
                             .filter(e -> e.getStatus().isNormal())
                             .toList();
    }

    public PostInfo savePost(PostInfo postInfo) {
        return postRepository.savePost(postInfo);
    }

    public boolean updatePostById(LifeLogUser lifeLogUser, Long postId) {
        PostInfo postInfo = checkIsOwner(lifeLogUser, postId);

        return postRepository.updatePostById(postInfo);
    }

    public boolean deletePostById(LifeLogUser lifeLogUser, Long postId) {
        PostInfo postInfo = checkIsOwner(lifeLogUser, postId);

        return postRepository.deletePostById(postInfo);
    }

    /**
     * 요청을 보낸 주체가 게시글의 소유주인지 확인
     * @param lifeLogUser 현재 로그인 유저
     * @param postId 게시글 아이디
     * @return 게시글 Info 객체
     */
    private PostInfo checkIsOwner(LifeLogUser lifeLogUser, Long postId) {
        PostInfo postInfo = findPostById(postId);
        String postOwner = postInfo.getUserInfo().getSub();

        if (!postOwner.equals(lifeLogUser.sub())) {
            throw new PostDomainException(NOT_POST_OWNER);
        }

        return postInfo;
    }
}
