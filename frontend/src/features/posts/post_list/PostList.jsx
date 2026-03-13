import React from "react"
import {Link} from "react-router-dom"
import "./post_list.css"

import PostCard from "../post_card/PostCard";

import posts from "../../../data/posts.mock"


export default function PostList(){
    
    return(
        <div className="post-list">
            {posts.map((post) =>(
                <PostCard
                    key={post.id}
                    author = {post.author}
                    content = {post.content}        
                    avatar={post.avatar}
                    images={post.images}
                    likes={post.likes}
                    comments={post.comments}
                    likedBy={post.likedBy}
                
                            
                />
            ))}
            
        </div>
    );
}