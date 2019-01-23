package com.snow.cacheredislock.controller;

import com.snow.cacheredislock.annotation.CacheLock;
import com.snow.cacheredislock.annotation.CacheParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BookController
 *
 * @author 王鹏涛
 * @since 2019年1月23日
 */
@RestController
@RequestMapping("/books")
public class BookController {

    @CacheLock(prefix = "books")
    @GetMapping
    public String query(@CacheParam(name = "token") String token,String name)   {
        return "success - " + token;
    }

}