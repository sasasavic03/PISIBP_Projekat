export const profilesMock = [
  {
    user: {
      id: 1,
      username: "john.doe",
      isPrivate: false,
      fullName: "John Doe",
      avatar: "https://i.pravatar.cc/150?img=1",
      bio: "📸 Photography enthusiast | 🌍 Traveler | ☕ Coffee addict",
      followedBy: [
        { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
      ],
      followers: [
        { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
        { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
      ],
      following: [
        { id: 5, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=5" },
        { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
      ],
    },
    stats: { posts: 9, followers: 1284, following: 347 },
    posts: [
      {
        id: 1,
        image: "https://picsum.photos/seed/john1/600/600",
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
          { id: 1, username: "jane.smith", text: "Stunning shot! 😍" },
          { id: 2, username: "mike.ross",  text: "Where is this?" },
        ],
        content: "caption caption "
      },
      {
        id: 2,
        image: "https://picsum.photos/seed/john2/600/600",
        images: [
          "https://picsum.photos/seed/john2a/600/600",
          "https://picsum.photos/seed/john2b/600/600",
        ],
        likes: 198,
        likedBy: [
          { id: 4, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=4" },
        ],
        comments: [
          { id: 1, username: "sarah.j", text: "Love this vibe 🔥" },
        ],
        content: "caption caption "
      },
      {
        id: 3,
        image: "https://picsum.photos/seed/john3/600/600",
        images: ["https://picsum.photos/seed/john3a/600/600"],
        likes: 521,
        likedBy: [
          { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
          { id: 5, username: "alex.k",     avatar: "https://i.pravatar.cc/150?img=5" },
          { id: 6, username: "nina.w",     avatar: "https://i.pravatar.cc/150?img=6" },
        ],
        comments: [
          { id: 1, username: "alex.k", text: "Incredible 🌅" },
        ],
        content: "caption caption "
      },
      {
        id: 4,
        image: "https://picsum.photos/seed/john4/600/600",
        images: [
          "https://picsum.photos/seed/john4a/600/600",
          "https://picsum.photos/seed/john4b/600/600",
          "https://picsum.photos/seed/john4c/600/600",
          "https://picsum.photos/seed/john4d/600/600",
        ],
        likes: 87,
        likedBy: [
          { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
        ],
        comments: [
          { id: 1, username: "mike.ross", text: "Slide 3 is my fav 😂" },
        ],
        content: "caption caption "
      },
      {
        id: 5,
        image: "https://picsum.photos/seed/john5/600/600",
        images: [
          "https://picsum.photos/seed/john5a/600/600",
          "https://picsum.photos/seed/john5b/600/600",
        ],
        likes: 413,
        likedBy: [
          { id: 4, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=4" },
          { id: 6, username: "nina.w",  avatar: "https://i.pravatar.cc/150?img=6" },
        ],
        comments: [
          { id: 1, username: "sarah.j", text: "This is everything 💯" },
        ],
        content: "caption caption "
      },
      {
        id: 6,
        image: "https://picsum.photos/seed/john6/600/600",
        images: ["https://picsum.photos/seed/john6a/600/600"],
        likes: 256,
        likedBy: [
          { id: 5, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=5" },
        ],
        comments: [],
        content: "caption caption "
      },
      {
        id: 7,
        image: "https://picsum.photos/seed/john7/600/600",
        images: [
          "https://picsum.photos/seed/john7a/600/600",
          "https://picsum.photos/seed/john7b/600/600",
          "https://picsum.photos/seed/john7c/600/600",
        ],
        likes: 174,
        likedBy: [
          { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
          { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
        ],
        comments: [
          { id: 1, username: "nina.w", text: "Road trip soon? 🚗" },
        ],
        content: "caption caption "
      },
      {
        id: 8,
        image: "https://picsum.photos/seed/john8/600/600",
        images: [
          "https://picsum.photos/seed/john8a/600/600",
          "https://picsum.photos/seed/john8b/600/600",
        ],
        likes: 632,
        likedBy: [
          { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
          { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
          { id: 5, username: "alex.k",     avatar: "https://i.pravatar.cc/150?img=5" },
        ],
        comments: [
          { id: 1, username: "jane.smith", text: "My favorite of yours!" },
        ],
        content: "caption caption "
      },
      {
        id: 9,
        image: "https://picsum.photos/seed/john9/600/600",
        images: [
          "https://picsum.photos/seed/john9a/600/600",
          "https://picsum.photos/seed/john9b/600/600",
          "https://picsum.photos/seed/john9c/600/600",
          "https://picsum.photos/seed/john9d/600/600",
          "https://picsum.photos/seed/john9e/600/600",
        ],
        likes: 299,
        likedBy: [
          { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
          { id: 6, username: "nina.w",    avatar: "https://i.pravatar.cc/150?img=6" },
        ],
        comments: [
          { id: 1, username: "mike.ross", text: "5 slides?! Legend 👏" },
          { id: 2, username: "sarah.j",   text: "Can't pick a favorite" },
        ],
        content: "caption caption "
      },
    ]
  },

  {
    user: {
      id: 2,
      username: "jane.smith",
      isPrivate: true,
      fullName: "Jane Smith",
      avatar: "https://i.pravatar.cc/150?img=2",
      bio: "🎨 Digital artist | 🌸 Nature lover | 🐱 Cat mom x2",
      followedBy: [
        { id: 1, username: "john.doe",  avatar: "https://i.pravatar.cc/150?img=1" },
        { id: 5, username: "alex.k",    avatar: "https://i.pravatar.cc/150?img=5" },
      ],
      followers: [
        { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
        { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
      ],
      following: [
        { id: 5, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=5" },
        { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
      ],
    },
    stats: { posts: 6, followers: 3821, following: 210 },
    posts: [
      {
        id: 10,
        image: "https://picsum.photos/seed/jane1/600/600",
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
          { id: 1, username: "john.doe",  text: "This is fire 🔥" },
          { id: 2, username: "mike.ross", text: "Incredible work!" },
        ]
      },
      {
        id: 11,
        image: "https://picsum.photos/seed/jane2/600/600",
        images: ["https://picsum.photos/seed/jane2a/600/600"],
        likes: 541,
        likedBy: [
          { id: 4, username: "sarah.j", avatar: "https://i.pravatar.cc/150?img=4" },
          { id: 6, username: "nina.w",  avatar: "https://i.pravatar.cc/150?img=6" },
        ],
        comments: [
          { id: 1, username: "sarah.j", text: "Love the colors 🎨" },
        ]
      },
      {
        id: 12,
        image: "https://picsum.photos/seed/jane3/600/600",
        images: [
          "https://picsum.photos/seed/jane3a/600/600",
          "https://picsum.photos/seed/jane3b/600/600",
          "https://picsum.photos/seed/jane3c/600/600",
        ],
        likes: 317,
        likedBy: [
          { id: 1, username: "john.doe", avatar: "https://i.pravatar.cc/150?img=1" },
        ],
        comments: [
          { id: 1, username: "alex.k", text: "Swipe to see the magic 🪄" },
        ]
      },
      {
        id: 13,
        image: "https://picsum.photos/seed/jane4/600/600",
        images: [
          "https://picsum.photos/seed/jane4a/600/600",
          "https://picsum.photos/seed/jane4b/600/600",
        ],
        likes: 728,
        likedBy: [
          { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
          { id: 5, username: "alex.k",    avatar: "https://i.pravatar.cc/150?img=5" },
        ],
        comments: [
          { id: 1, username: "nina.w", text: "Obsessed with this 😍" },
        ]
      },
      {
        id: 14,
        image: "https://picsum.photos/seed/jane5/600/600",
        images: ["https://picsum.photos/seed/jane5a/600/600"],
        likes: 204,
        likedBy: [
          { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
        ],
        comments: []
      },
      {
        id: 15,
        image: "https://picsum.photos/seed/jane6/600/600",
        images: [
          "https://picsum.photos/seed/jane6a/600/600",
          "https://picsum.photos/seed/jane6b/600/600",
          "https://picsum.photos/seed/jane6c/600/600",
          "https://picsum.photos/seed/jane6d/600/600",
        ],
        likes: 1103,
        likedBy: [
          { id: 1, username: "john.doe",  avatar: "https://i.pravatar.cc/150?img=1" },
          { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
          { id: 4, username: "sarah.j",   avatar: "https://i.pravatar.cc/150?img=4" },
        ],
        comments: [
          { id: 1, username: "john.doe",  text: "Best one yet 🏆" },
          { id: 2, username: "sarah.j",   text: "Can't stop looking at this" },
        ]
      },
    ]
  },

  {
    user: {
      id: 3,
      username: "mike.ross",
      isPrivate: false,
      fullName: "Mike Ross",
      avatar: "https://i.pravatar.cc/150?img=3",
      bio: "🏋️ Fitness & lifestyle | 🥗 Clean eating | 💪 Personal trainer",
      followedBy: [
        { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
        { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
      ],
      followers: [
        { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
        { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
      ],
      following: [
        { id: 5, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=5" },
        { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
      ],
    },
    stats: { posts: 5, followers: 6540, following: 98 },
    posts: [
      {
        id: 16,
        image: "https://picsum.photos/seed/mike1/600/600",
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
          { id: 1, username: "john.doe",   text: "Beast mode 💪" },
          { id: 2, username: "jane.smith", text: "Goals!" },
        ]
      },
      {
        id: 17,
        image: "https://picsum.photos/seed/mike2/600/600",
        images: ["https://picsum.photos/seed/mike2a/600/600"],
        likes: 876,
        likedBy: [
          { id: 5, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=5" },
          { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
        ],
        comments: [
          { id: 1, username: "alex.k", text: "What's the routine? 🏋️" },
        ]
      },
      {
        id: 18,
        image: "https://picsum.photos/seed/mike3/600/600",
        images: [
          "https://picsum.photos/seed/mike3a/600/600",
          "https://picsum.photos/seed/mike3b/600/600",
          "https://picsum.photos/seed/mike3c/600/600",
        ],
        likes: 543,
        likedBy: [
          { id: 1, username: "john.doe", avatar: "https://i.pravatar.cc/150?img=1" },
          { id: 4, username: "sarah.j",  avatar: "https://i.pravatar.cc/150?img=4" },
        ],
        comments: [
          { id: 1, username: "sarah.j", text: "Meal prep inspo 🥗" },
        ]
      },
      {
        id: 19,
        image: "https://picsum.photos/seed/mike4/600/600",
        images: [
          "https://picsum.photos/seed/mike4a/600/600",
          "https://picsum.photos/seed/mike4b/600/600",
        ],
        likes: 291,
        likedBy: [
          { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        ],
        comments: []
      },
      {
        id: 20,
        image: "https://picsum.photos/seed/mike5/600/600",
        images: [
          "https://picsum.photos/seed/mike5a/600/600",
          "https://picsum.photos/seed/mike5b/600/600",
          "https://picsum.photos/seed/mike5c/600/600",
          "https://picsum.photos/seed/mike5d/600/600",
        ],
        likes: 2104,
        likedBy: [
          { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
          { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
          { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
        ],
        comments: [
          { id: 1, username: "john.doe",   text: "Transformation is unreal 🔥" },
          { id: 2, username: "jane.smith", text: "So inspiring!" },
          { id: 3, username: "nina.w",     text: "You're the GOAT 🐐" },
        ]
      },
    ]
  },

  {
    user: {
      id: 4,
      username: "sarah.j",
      isPrivate: true,
      fullName: "Sarah Johnson",
      avatar: "https://i.pravatar.cc/150?img=4",
      bio: "✈️ Full-time traveler | 🍜 Foodie | 📍 Currently: Bali",
      followedBy: [
        { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
        { id: 6, username: "nina.w",     avatar: "https://i.pravatar.cc/150?img=6" },
      ],
      followers: [
        { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
        { id: 4, username: "sarah.j",    avatar: "https://i.pravatar.cc/150?img=4" },
      ],
      following: [
        { id: 5, username: "alex.k", avatar: "https://i.pravatar.cc/150?img=5" },
        { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
      ],
    },
    stats: { posts: 7, followers: 12400, following: 512 },
    posts: [
      {
        id: 21,
        image: "https://picsum.photos/seed/sarah1/600/600",
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
          { id: 1, username: "john.doe",  text: "Take me there! ✈️" },
          { id: 2, username: "nina.w",    text: "Which part of Bali? 🌴" },
        ]
      },
      {
        id: 22,
        image: "https://picsum.photos/seed/sarah2/600/600",
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
          { id: 1, username: "alex.k", text: "The food looks amazing 🍜" },
        ]
      },
      {
        id: 23,
        image: "https://picsum.photos/seed/sarah3/600/600",
        images: ["https://picsum.photos/seed/sarah3a/600/600"],
        likes: 924,
        likedBy: [
          { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
        ],
        comments: []
      },
      {
        id: 24,
        image: "https://picsum.photos/seed/sarah4/600/600",
        images: [
          "https://picsum.photos/seed/sarah4a/600/600",
          "https://picsum.photos/seed/sarah4b/600/600",
          "https://picsum.photos/seed/sarah4c/600/600",
          "https://picsum.photos/seed/sarah4d/600/600",
        ],
        likes: 2103,
        likedBy: [
          { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
          { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
        ],
        comments: [
          { id: 1, username: "jane.smith", text: "Every slide is a postcard 🌅" },
        ]
      },
      {
        id: 25,
        image: "https://picsum.photos/seed/sarah5/600/600",
        images: [
          "https://picsum.photos/seed/sarah5a/600/600",
          "https://picsum.photos/seed/sarah5b/600/600",
        ],
        likes: 678,
        likedBy: [
          { id: 6, username: "nina.w", avatar: "https://i.pravatar.cc/150?img=6" },
        ],
        comments: [
          { id: 1, username: "nina.w", text: "Wanderlust 100% 🗺️" },
        ]
      },
      {
        id: 26,
        image: "https://picsum.photos/seed/sarah6/600/600",
        images: ["https://picsum.photos/seed/sarah6a/600/600"],
        likes: 445,
        likedBy: [
          { id: 3, username: "mike.ross", avatar: "https://i.pravatar.cc/150?img=3" },
          { id: 5, username: "alex.k",    avatar: "https://i.pravatar.cc/150?img=5" },
        ],
        comments: []
      },
      {
        id: 27,
        image: "https://picsum.photos/seed/sarah7/600/600",
        images: [
          "https://picsum.photos/seed/sarah7a/600/600",
          "https://picsum.photos/seed/sarah7b/600/600",
          "https://picsum.photos/seed/sarah7c/600/600",
        ],
        likes: 1547,
        likedBy: [
          { id: 1, username: "john.doe",   avatar: "https://i.pravatar.cc/150?img=1" },
          { id: 2, username: "jane.smith", avatar: "https://i.pravatar.cc/150?img=2" },
          { id: 3, username: "mike.ross",  avatar: "https://i.pravatar.cc/150?img=3" },
        ],
        comments: [
          { id: 1, username: "john.doe", text: "Sunset goals 🌇" },
          { id: 2, username: "mike.ross", text: "This is unreal!" },
        ]
      },
    ]
  },
];

export function getProfileByUsername(username) {
  return profilesMock.find(p => p.user.username === username) ?? null;
}