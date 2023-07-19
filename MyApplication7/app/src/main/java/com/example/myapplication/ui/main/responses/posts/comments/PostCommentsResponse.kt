package com.example.myapplication.ui.main.responses.posts.comments

import com.example.myapplication.ui.main.responses.posts.SubredditPostsResponse

class PostCommentsResponse(
    val subredditPostsResponse: SubredditPostsResponse,
    val commentsResponse: CommentsResponse
)