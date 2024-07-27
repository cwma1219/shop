package com.example.rd.service.impl;

import com.example.rd.dto.Result;
import com.example.rd.dto.UserDto;
import com.example.rd.entity.Blog;
import com.example.rd.entity.User;
import com.example.rd.repository.BlogRepository;
import com.example.rd.repository.UserRepository;
import com.example.rd.service.BlogService;
import com.example.rd.service.UserService;
import com.example.rd.util.SystemConstants;
import com.example.rd.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    public Result queryMyBolg(int page) {
        UserDto dto = UserHolder.getUser();
        Page<Blog> blogs = blogRepository.searchBlog(dto.getId().toString(), PageRequest.of(page, SystemConstants.MAX_PAGE_SIZE));
        return Result.ok(blogs.getContent());
    }

    @Override
    public Result hot(int page) {
        Page<Blog> blogs = blogRepository.findAll(PageRequest.of(page, SystemConstants.MAX_PAGE_SIZE));
        List<Blog> records = blogs.getContent();
        return Result.ok(records);
    }

}
