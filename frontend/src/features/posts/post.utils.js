export function mapMediaToImages(mediaList) {
  return mediaList.map((m) => {
    const mediaUrl = m.media_url || m.mediaUrl;
    return `http://localhost:8080/api/posts/media/${mediaUrl}`;
  });
}

export function isPostLikedByUser(likedBy, username) {
  return likedBy.some((u) => u.username === username);
}

export function isValidFileSize(fileSize, maxSizeMB = 50) {
  return fileSize <= maxSizeMB * 1024 * 1024;
}
