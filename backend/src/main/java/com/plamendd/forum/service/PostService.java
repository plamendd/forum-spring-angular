package com.plamendd.forum.service;


import com.plamendd.forum.dto.PostRequest;
import com.plamendd.forum.dto.PostResponse;
import com.plamendd.forum.exeptions.PostNotFoundException;
import com.plamendd.forum.exeptions.ThemeNotFoundException;
import com.plamendd.forum.mapper.PostMapper;
import com.plamendd.forum.model.Post;
import com.plamendd.forum.model.Theme;
import com.plamendd.forum.model.User;
import com.plamendd.forum.repository.PostRepository;
import com.plamendd.forum.repository.ThemeRepository;
import com.plamendd.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    private final ThemeRepository themeRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Transactional
    public void save(PostRequest postRequest) {
        Theme theme = themeRepository.findByName(postRequest.getThemeName())
                .orElseThrow(() -> new ThemeNotFoundException(postRequest.getThemeName()));
        postRepository.save(postMapper.map(postRequest, theme, authService.getCurrentUser()));
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }


    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    public List<PostResponse> getPostsByTheme(Long subredditId) {
        Theme theme = themeRepository.findById(subredditId)
                .orElseThrow(() -> new ThemeNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllByTheme(theme);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }


    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
