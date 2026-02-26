import Suggestions from "./suggestions/Suggestions";
import PostList from "./post_list/PostList";
import "./feed.css"



export default function Feed() {
  return (
    <div className="feed">
      <section className="feed-posts">
        <PostList/>
      </section>
      <aside className="feed-suggestions">
        <Suggestions />
      </aside>
      
    </div>
  );
}
