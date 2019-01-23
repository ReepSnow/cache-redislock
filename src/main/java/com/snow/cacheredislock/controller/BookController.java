package com.snow.cacheredislock.controller;

import com.snow.cacheredislock.annotation.CacheLock;
import com.snow.cacheredislock.annotation.CacheParam;
import org.springframework.web.bind.annotation.*;

/**
 * BookController
 *
 * @author 王鹏涛
 * @since 2019年1月23日
 */
@RestController
public class BookController {

    @CacheLock(prefix = "books")
    @GetMapping("booksGet")
    public String queryGet(@CacheParam(name = "token") String token,String name)   {
        return "success - " + token;
    }
    @CacheLock(prefix = "books")
    @PostMapping("booksPost")
    public String queryPost(@CacheParam(name = "token") String token,String name)   {
        return "success - " + token;
    }

}