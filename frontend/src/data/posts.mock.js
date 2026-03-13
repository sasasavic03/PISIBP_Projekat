const posts = [
  {
    id: 1,
    author: "john.doe",
    avatar: "https://i.pravatar.cc/150?img=1",
    images: [
      "https://picsum.photos/seed/john1a/600/600",
      "https://picsum.photos/seed/john1b/600/600",
      "https://picsum.photos/seed/john1c/600/600",
    ],
    likes: 342,
    likedBy: [
      { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
      { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
    ],
    comments: [
      { id: 1, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2", text: "Stunning shot! 😍" },
      { id: 2, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3",  text: "Where is this?" },
    ],
    content: "Golden hour never hits the same 🌅 #travel #photography"
  },
  {
    id: 2,
    author: "jane.smith",
    avatar: "https://i.pravatar.cc/150?img=2",
    images: [
      "https://picsum.photos/seed/jane1a/600/600",
      "https://picsum.photos/seed/jane1b/600/600",
    ],
    likes: 892,
    likedBy: [
      { id: 1, username: "john.doe",  avatar: "https://i.pravatar.cc/150?img=1" },
      { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
      { id: 5, username: "alex.k",    avatar: "https://i.pravatar.cc/150?img=5" },
    ],
    comments: [
      { id: 1, username: "john.doe", avatar: "https://i.pravatar.cc/150?img=4", text: "This is fire 🔥" },
      { id: 2, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=5", text: "Incredible work!" },
    ],
    content: "New art series dropping 🎨 Let me know what you think!"
  },
  {
    id: 3,
    author: "mike.ross",
    avatar: "https://i.pravatar.cc/150?img=3",
    images: [
      "https://picsum.photos/seed/mike1a/600/600",
      "https://picsum.photos/seed/mike1b/600/600",
    ],
    likes: 1432,
    likedBy: [
      { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
      { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
      { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
    ],
    comments: [
      { id: 1, username: "john.doe", avatar: "https://i.pravatar.cc/150?img=2",   text: "Beast mode 💪" },
      { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=3", text: "Goals!" },
    ],
    content: "Morning grind never stops 💪 #fitness #gym #motivation"
  },
  {
    id: 4,
    author: "sarah.j",
    avatar: "https://i.pravatar.cc/150?img=4",
    images: [
      "https://picsum.photos/seed/sarah1a/600/600",
      "https://picsum.photos/seed/sarah1b/600/600",
      "https://picsum.photos/seed/sarah1c/600/600",
    ],
    likes: 3421,
    likedBy: [
      { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
      { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
      { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
    ],
    comments: [
      { id: 1, username: "john.doe",avatar: "https://i.pravatar.cc/150?img=2", text: "Take me there! ✈️" },
      { id: 2, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=2",  text: "Which part of Bali? 🌴" },
    ],
    content: "Bali mornings are something else 🌴 #bali #travel #wanderlust"
  },
  {
    id: 5,
    author: "alex.k",
    avatar: "https://i.pravatar.cc/150?img=5",
    images: [
      "https://picsum.photos/seed/alex1a/600/600",
    ],
    likes: 654,
    likedBy: [
      { id: 4, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=4" },
      { id: 6, username: "nina.w",  avatar: "https://i.pravatar.cc/150?img=6" },
    ],
    comments: [
      { id: 1, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=2", text: "Where is this cafe? ☕" },
      { id: 2, username: "nina.w",avatar: "https://i.pravatar.cc/150?img=2",  text: "Cozy vibes 🤎" },
    ],
    content: "Found the perfect spot ☕ #coffee #cafe #aesthetic"
  },
  {
    id: 6,
    author: "nina.w",
    avatar: "https://i.pravatar.cc/150?img=6",
    images: [
      "https://picsum.photos/seed/nina1a/600/600",
      "https://picsum.photos/seed/nina1b/600/600",
      "https://picsum.photos/seed/nina1c/600/600",
      "https://picsum.photos/seed/nina1d/600/600",
    ],
    likes: 287,
    likedBy: [
      { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
      { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
    ],
    comments: [
      { id: 1, username: "jane.smith",avatar: "https://i.pravatar.cc/150?img=2", text: "Love this series! 🌸" },
      { id: 2, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=2",    text: "Slide 4 is everything 😍" },
    ],
    content: "Spring in the city 🌸 swipe to see my favorite spots"
  },
  {
    id: 7,
    author: "john.doe",
    avatar: "https://i.pravatar.cc/150?img=1",
    images: [
      "https://picsum.photos/seed/john2a/600/600",
      "https://picsum.photos/seed/john2b/600/600",
    ],
    likes: 198,
    likedBy: [
      { id: 4, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=4" },
      { id: 5, username: "alex.k",  avatar: "https://i.pravatar.cc/150?img=5" },
    ],
    comments: [
      { id: 1, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=2", text: "Love this vibe 🔥" },
    ],
    content: "Weekend mode activated 😎 #weekend #chill"
  },
  {
    id: 8,
    author: "mike.ross",
    avatar: "https://i.pravatar.cc/150?img=3",
    images: [
      "https://picsum.photos/seed/mike2a/600/600",
      "https://picsum.photos/seed/mike2b/600/600",
      "https://picsum.photos/seed/mike2c/600/600",
      "https://picsum.photos/seed/mike2d/600/600",
    ],
    likes: 2104,
    likedBy: [
      { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
      { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
      { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
    ],
    comments: [
      { id: 1, username: "john.doe", avatar: "https://i.pravatar.cc/150?img=2",  text: "Transformation is unreal 🔥" },
      { id: 2, username: "jane.smith",avatar: "https://i.pravatar.cc/150?img=2", text: "So inspiring!" },
      { id: 3, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=2",   text: "You're the GOAT 🐐" },
    ],
    content: "6 month transformation 📈 Swipe to see the journey. Consistency is everything 💯"
  },
  {
    id: 9,
    author: "sarah.j",
    avatar: "https://i.pravatar.cc/150?img=4",
    images: [
      "https://picsum.photos/seed/sarah2a/600/600",
      "https://picsum.photos/seed/sarah2b/600/600",
    ],
    likes: 1876,
    likedBy: [
      { id: 5, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=5" },
      { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
    ],
    comments: [
      { id: 1, username: "alex.k",avatar: "https://i.pravatar.cc/150?img=2", text: "The food looks amazing 🍜" },
    ],
    content: "Street food tour in Bangkok 🍜 my stomach thanks me, my wallet doesn't 😂"
  },
  {
    id: 10,
    author: "jane.smith",
    avatar: "https://i.pravatar.cc/150?img=2",
    images: [
      "https://picsum.photos/seed/jane2a/600/600",
      "https://picsum.photos/seed/jane2b/600/600",
      "https://picsum.photos/seed/jane2c/600/600",
      "https://picsum.photos/seed/jane2d/600/600",
    ],
    likes: 1103,
    likedBy: [
      { id: 1, username: "john.doe",  avatar: "https://i.pravatar.cc/150?img=1" },
      { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
      { id: 4, username: "sarah.j",   avatar: "https://i.pravatar.cc/150?img=4" },
    ],
    comments: [
      { id: 1, username: "john.doe",avatar: "https://i.pravatar.cc/150?img=2", text: "Best one yet 🏆" },
      { id: 2, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=2", text: "Can't stop looking at this" },
    ],
    content: "New collection finished 🎨 4 pieces, swipe to see them all"
  },
];

export default posts;