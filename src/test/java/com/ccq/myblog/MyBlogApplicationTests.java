package com.ccq.myblog;

import com.ccq.myblog.domain.RestBoard;
import com.ccq.myblog.domain.RestComment;
import com.ccq.myblog.domain.RestCommentReply;
import com.ccq.myblog.repository.RestBoardRepository;
import com.ccq.myblog.repository.RestCommentReplyRepository;
import com.ccq.myblog.repository.RestCommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBlogApplicationTests {
    @Autowired
    private RestCommentRepository restCommentRepository;

    @Autowired
    private RestCommentReplyRepository restCommentReplyRepository;

    @Autowired
    private RestBoardRepository restBoardRepository;

    @Test
    public void contextLoads() {
        RestComment comment = new RestComment();
        comment.setUserName("abc");
        comment.setBlogId((long) 5);
        comment.setEmail("123@qq.com");
        comment.setContent("123@qq.com");
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        restCommentRepository.save(comment);
//        RestCommentReply reply = new RestCommentReply();
//        reply.setUserName("abc");
//        reply.setEmail("123@qq.com");
//        reply.setContent("123@qq.com");
//        reply.setCreateTime(new Timestamp(System.currentTimeMillis()));
//        RestCommentReply reply1 = new RestCommentReply();
        restCommentRepository.findAll().forEach(System.out::println);

        RestBoard restBoard = new RestBoard();
        restBoard.setUserName("abc");
        restBoard.setEmail("123@qq.com");
        restBoard.setContent("123@qq.com");
        restBoard.setCreateTime(new Timestamp(System.currentTimeMillis()));
        restBoard.setReplyContent("hello");
        restBoard.setReplyTime(new Timestamp(System.currentTimeMillis()));
        restBoardRepository.save(restBoard);
        restBoardRepository.findAll().forEach(System.out::println);
    }

}

