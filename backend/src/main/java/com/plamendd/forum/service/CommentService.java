package com.plamendd.forum.service;

import com.plamendd.forum.dto.CommentsDTO;
import com.plamendd.forum.exeptions.PostNotFoundException;
import com.plamendd.forum.mapper.CommentMapper;
import com.plamendd.forum.model.Comment;
import com.plamendd.forum.model.NotificationEmail;
import com.plamendd.forum.model.Post;
import com.plamendd.forum.model.User;
import com.plamendd.forum.repository.CommentRepository;
import com.plamendd.forum.repository.PostRepository;
import com.plamendd.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

     private static final String COMMENT_URL = "";
     private final PostRepository postRepository;
     private final CommentMapper commentMapper;
     private final CommentRepository commentRepository;
     private final AuthService authService;
     private final UserRepository userRepository;
     private final MailService mailService;
     private final MailContentBuilder mailContentBuilder;


     public void save(CommentsDTO commentsDTO){
          Post post = postRepository.findById(commentsDTO.getPostId())
                  .orElseThrow(() -> new PostNotFoundException(commentsDTO.getPostId().toString()));

          Comment comment = commentMapper.map(commentsDTO, post, authService.getCurrentUser());
          commentRepository.save(comment);

          String message = mailContentBuilder.build(post.getUser() + " posted a  comment on your post" + COMMENT_URL);
          sendMailForCommentNotification(message, post.getUser());
     }

     private void sendMailForCommentNotification(String message, User user) {
          mailService.sendMail(new NotificationEmail(user.getUsername() + "posted a reply on your commend", user.getEmail(),  message));
     }

     public List<CommentsDTO> getAllCommentsForPost(Long postId) {
          Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
          return commentRepository.findByPost(post)
                  .stream()
                  .map(commentMapper::mapToDto).collect(toList());
     }

     public List<CommentsDTO> getAllCommentsForUser(String userName) {
          User user = userRepository.findByUsername(userName)
                  .orElseThrow(() -> new UsernameNotFoundException(userName));
          return commentRepository.findAllByUser(user)
                  .stream()
                  .map(commentMapper::mapToDto)
                  .collect(toList());
     }

}
