/* export const profileMock = {

    user:{
      id:2,
      username:"john_doe",
      fullName:"John Doe",
      bio:"Frontend developer",
      avatar:"https://i.pravatar.cc/150?img=3",
  
      followedBy:[
        {
          id:11,
          username:"anna",
          avatar:"https://i.pravatar.cc/40?img=5"
        },
        {
          id:12,
          username:"mark",
          avatar:"https://i.pravatar.cc/40?img=6"
        },
        {
          id:13,
          username:"lucas",
          avatar:"https://i.pravatar.cc/40?img=7"
        }
      ]
    },
  
    stats:{
      posts:24,
      followers:120,
      following:80
    }, 
  
    posts:[
      {
        id:1,
        username:"john_doe",
        image:"https://picsum.photos/500?1",
        likes:34,
        comments:[]
      },
      {
        id:2,
        username:"john_doe",
        image:"https://picsum.photos/500?2",
        likes:21,
        comments:[]
      },
      {
        id:3,
        username:"john_doe",
        image:"https://picsum.photos/500?3",
        likes:15,
        comments:[]
      },
      {
        id:4,
        username:"john_doe",
        image:"https://picsum.photos/500?4",
        likes:10,
        comments:[]
      },
      {
        id:5,
        username:"john_doe",
        image:"https://picsum.photos/500?5",
        likes:8,
        comments:[]
      },
      {
        id:6,
        username:"john_doe",
        image:"https://picsum.photos/500?6",
        likes:19,
        comments:[]
      },
      {
        id:7,
        username:"john_doe",
        image:"https://picsum.photos/500?7",
        likes:42,
        comments:[]
      },
      {
        id:8,
        username:"john_doe",
        image:"https://picsum.photos/500?8",
        likes:17,
        comments:[]
      },
      {
        id:9,
        username:"john_doe",
        image:"https://picsum.photos/500?9",
        likes:29,
        comments:[]
      }
    ]
  
  }; */

  export const profileMock = {
    user: {
      id: 1,
      username: "john.doe",
      fullName: "John Doe",
      avatar: "https://i.pravatar.cc/150?img=1",
      bio: "📸 Photography enthusiast | 🌍 Traveler | ☕ Coffee addict",
      followedBy: [
        { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
        { id: 4, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=4" },
      ]
    },
  
    stats: {
      posts: 9,
      followers: 1284,
      following: 347
    },
  
    posts: [
      {
        id: 1,
        image: "https://picsum.photos/seed/post1/600/600",
        images: [
          "https://picsum.photos/seed/post1a/600/600",
          "https://picsum.photos/seed/post1b/600/600",
          "https://picsum.photos/seed/post1c/600/600",
        ],
        likes: 342,
        comments: [
          { id: 1, username: "jane.smith", text: "Stunning shot! 😍" },
          { id: 2, username: "mike.ross", text: "Where is this?" },
          { id: 3, username: "sarah.j", text: "Goals 🙌" },
        ]
      },
      {
        id: 2,
        image: "https://picsum.photos/seed/post2/600/600",
        images: [
          "https://picsum.photos/seed/post2a/600/600",
          "https://picsum.photos/seed/post2b/600/600",
        ],
        likes: 198,
        comments: [
          { id: 1, username: "alex.k", text: "Love this vibe 🔥" },
          { id: 2, username: "nina.w", text: "So cozy!" },
        ]
      },
      {
        id: 3,
        image: "https://picsum.photos/seed/post3/600/600",
        images: [
          "https://picsum.photos/seed/post3a/600/600",
        ],
        likes: 521,
        comments: [
          { id: 1, username: "tom.h", text: "Incredible 🌅" },
        ]
      },
      {
        id: 4,
        image: "https://picsum.photos/seed/post4/600/600",
        images: [
          "https://picsum.photos/seed/post4a/600/600",
          "https://picsum.photos/seed/post4b/600/600",
          "https://picsum.photos/seed/post4c/600/600",
          "https://picsum.photos/seed/post4d/600/600",
        ],
        likes: 87,
        comments: [
          { id: 1, username: "jane.smith", text: "Best series yet!" },
          { id: 2, username: "mike.ross", text: "Slide 3 is my fav 😂" },
        ]
      },
      {
        id: 5,
        image: "https://picsum.photos/seed/post5/600/600",
        images: [
          "https://picsum.photos/seed/post5a/600/600",
          "https://picsum.photos/seed/post5b/600/600",
        ],
        likes: 413,
        comments: [
          { id: 1, username: "sarah.j", text: "This is everything 💯" },
        ]
      },
      {
        id: 6,
        image: "https://picsum.photos/seed/post6/600/600",
        images: [
          "https://picsum.photos/seed/post6a/600/600",
        ],
        likes: 256,
        comments: []
      },
      {
        id: 7,
        image: "https://picsum.photos/seed/post7/600/600",
        images: [
          "https://picsum.photos/seed/post7a/600/600",
          "https://picsum.photos/seed/post7b/600/600",
          "https://picsum.photos/seed/post7c/600/600",
        ],
        likes: 174,
        comments: [
          { id: 1, username: "alex.k", text: "Road trip soon? 🚗" },
          { id: 2, username: "nina.w", text: "Take me with you!" },
          { id: 3, username: "tom.h", text: "These colors 🎨" },
        ]
      },
      {
        id: 8,
        image: "https://picsum.photos/seed/post8/600/600",
        images: [
          "https://picsum.photos/seed/post8a/600/600",
          "https://picsum.photos/seed/post8b/600/600",
        ],
        likes: 632,
        comments: [
          { id: 1, username: "jane.smith", text: "My favorite of yours!" },
        ]
      },
      {
        id: 9,
        image: "https://picsum.photos/seed/post9/600/600",
        images: [
          "https://picsum.photos/seed/post9a/600/600",
          "https://picsum.photos/seed/post9b/600/600",
          "https://picsum.photos/seed/post9c/600/600",
          "https://picsum.photos/seed/post9d/600/600",
          "https://picsum.photos/seed/post9e/600/600",
        ],
        likes: 299,
        comments: [
          { id: 1, username: "mike.ross", text: "5 slides?! Legend 👏" },
          { id: 2, username: "sarah.j", text: "Can't pick a favorite" },
        ]
      },
    ]
  };

 