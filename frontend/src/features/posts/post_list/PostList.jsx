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
                    image={post.image}
                    likes={post.likes}
                
                            
                />
            ))}
            
        </div>
    );
}