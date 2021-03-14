package com.mywork.demoTwitterTheThird.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.mywork.demoTwitterTheThird.model.Tweet;
import com.mywork.demoTwitterTheThird.model.TweetDisplay;
import com.mywork.demoTwitterTheThird.model.User;
import com.mywork.demoTwitterTheThird.service.TweetService;
import com.mywork.demoTwitterTheThird.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class TweetController {
    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;



    @GetMapping(value = "/tweets/{tag}")
    public String getTweetsByTag(@PathVariable(value = "tag") String tag, Model model) {
        List<TweetDisplay> tweets = tweetService.findAllWithTag(tag);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("tag", tag);
        return "taggedTweets";
    }

    @GetMapping(value = "/tweets/new")
    public String creatingMethodName(Model model) {
        model.addAttribute("tweet", new Tweet());
        model.addAttribute("tagsAsString", new String());
        return "newTweet";
    }

    @PostMapping(value = "/tweets")
    public String submitTweetForm(@Valid Tweet tweet, BindingResult bindingResult, Model model, String tagsAsString) {
        User user = userService.getLoggedInUser();
        if (!bindingResult.hasErrors()) {
            tweet.setUser(user);
            tweetService.save(tweet);
            model.addAttribute("successMessage", "Tweet successfully created!");
            model.addAttribute("tweet", new Tweet());
        }
        return "newTweet";
    }
    @GetMapping(value = { "/tweets", "/" })
    public String getFeed(@RequestParam(value = "filter", required = false) String filter, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        List<TweetDisplay> tweets = new ArrayList<>();
        if (filter == null) {
            filter = "all";
        }
        if (filter.equalsIgnoreCase("following")) {
            List<User> following = loggedInUser.getFollowing();
            tweets = tweetService.findAllByUsers(following);
            model.addAttribute("filter", "following");
        } else {
            tweets = tweetService.findAll();
            model.addAttribute("filter", "all");
        }
        model.addAttribute("tweetList", tweets);
        return "feed";
    }
}