import React, { useEffect, useRef, useState } from "react"
import {Link} from "react-router-dom"
import { FiChevronRight, FiChevronLeft } from "react-icons/fi";
import "./story_bar.css";

const users =[
    {id: 1, avatar: './2145.jpg', username: "moe_lester", story_content: "" },
    {id: 2, avatar: './avatar1.jpg', username: "diddler", story_content: "" },
    {id: 3, avatar: './void_vance.jpg', username: "void_vance", story_content: "" },
    {id: 4, avatar: './a78.jpg', username: "kirkjerk", story_content: ""},
    {id: 5, avatar: './kjasdkajsn.jpg', username: "aghartakirk", story_content: ""},
    {id: 6, avatar: './kjasdkajsn.jpg', username: "aghartakirk", story_content: ""},
    {id: 8, avatar: './kjasdkajsn.jpg', username: "aghartakirk", story_content: ""},
    {id: 9, avatar: './kjasdkajsn.jpg', username: "aghartakirk", story_content: ""},
    {id: 10, avatar: './kjasdkajsn.jpg', username: "aghartakirk", story_content: ""},
    {id: 11, avatar: './kjasdkajsn.jpg', username: "aghartakirk", story_content: ""},
];

export default function StoryBar(){
    const scrollRef = useRef(null);
    const [canScrollRight, setCanScrollRight] = useState(false);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    
    const updateScrollState = () => {
        const el = scrollRef.current; //Sta je el?
        if(!el) return;

        const maxScrollLeft = el.scrollWidth - el.clientWidth;
        const epsilon = 2; //?? koji kurac

        setCanScrollLeft(el.scrollLeft > epsilon);
        setCanScrollRight(el.scrollLeft < maxScrollLeft - epsilon);
    };

    useEffect(()=> {
        updateScrollState();

        const el = scrollRef.current;
        if (!el) return;

        const onScroll = () => updateScrollState();
        el.addEventListener("scroll", onScroll, {passive: true});

        window.addEventListener("resize", updateScrollState);

        return ()=> {
            el.removeEventListener("scroll", onScroll);
            window.removeEventListener("resize", updateScrollState);
        };
    }, []);

    const scrollByAmount = (dir)=> {
        const el = scrollRef.current;
        if (!el) return;
    
        const amount = Math.max(240, Math.floor(el.clientWidth * 0.8));
        el.scrollBy({ left: dir * amount, behavior: "smooth" });
      };

    return (
        <div className="ig-storybar">
            <div className="ig-storybar_scroll" ref={scrollRef}>
                <ol className="ig-storybar_list">
                    {users.map(user => (
                        <li key={user.id} className="ig-storybar_item">
                            <Link to={`/${user.username}`} className="ig-storybar_link">
                                <img src={user.avatar} 
                                     alt={user.username}
                                     className="ig-storybar_avatar"
                                />

                                <div className="ig-storybar_text">
                                    <span className="ig-storybar_username">
                                        {user.username}
                                    </span>

                                </div>


                            </Link>
                            
                        </li>
                    ))

                    }


                </ol>

            </div>

            {canScrollRight && (
                <button
                type="button"
                className="ig-storybar_arrow ig-storybar_arrow_right"
                onClick={()=>scrollByAmount(1)}> 
                    {<FiChevronRight/>}
                </button>
            )}

            {canScrollLeft && (
                <button
                type="button"
                className="ig-storybar_arrow ig-storybar_arrow_left"
                onClick={() => scrollByAmount(-1)}>
                    {<FiChevronLeft/>}
                </button>
            )}
        </div>
    );
}