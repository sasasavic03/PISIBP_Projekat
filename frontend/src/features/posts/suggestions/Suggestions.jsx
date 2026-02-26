import React from "react"
import {Link} from "react-router-dom"
import "./suggestions.css"


const users =[
  {id: 1, avatar: './2145.jpg', username: "moe_lester", followedBy: "marko.galetin"},
  {id: 2, avatar: './avatar1.jpg', username: "diddler", followedBy: "epstein1312"},
  {id: 3, avatar: './void_vance.jpg', username: "void_vance", followedBy: "lildTrump"},
  {id: 4, avatar: './a78.jpg', username: "kirkjerk", followedBy: "nikola.tesla"},
  {id: 5, avatar: './kjasdkajsn.jpg', username: "aghartakirk", followedBy: "volodymir.zelenski"},
];

export default function Suggestions() {
  return (
    <aside className="ig-suggestions">
      <div className="ig-suggestions_inner">
        <div className="ig-suggestions_header">
          <p>Suggestions for you</p>
          <button className="ig-suggestions_seeall">See all</button>
        </div>
        

        <ol className="ig-suggestions_list">
          {users.map(user => (
            <li key={user.id} className="ig-suggestions_item">
                <Link to={`/${user.username}`} className="ig-suggestions_link">
    
        <img
          src={user.avatar}
          alt={user.username}
          className="ig-suggestions_avatar"
        />

        <div className="ig-suggestions_text">
          <span className="ig-suggestions_username">
            {user.username}
          </span>
          <span className="ig-suggestions_followed">
            Followed by {user.followedBy}
          </span>
        </div>


  </Link>

  <button className="ig-suggestions_follow">
    Follow
  </button>
            </li>
          ))}
        </ol>


        <div>
        <span className="ig-suggestions_more">
              <Link> About</Link> &middot;
              <Link> Home</Link> &middot;
              <Link> Press</Link> &middot;
              <Link> API</Link> &middot;
              <Link> Jobs</Link> &middot;
              <Link> Privacy</Link> &middot;
              <Link> Terms</Link> &middot;
              <Link> Locations</Link> &middot;
              <Link> Languages</Link>     
        </span>
        </div>

        <div>
          <span className="ig-suggestions_more">
            <p>&copy; 2026 Instagram from ERDOGLIJA KINGDOM</p>
          </span>
        </div>

      </div>



        
    </aside>
    

    
  );

  
}


