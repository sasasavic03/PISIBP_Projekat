export function canViewContent(isOwnProfile, isFollowing, isPrivate) {
  return isOwnProfile || isFollowing || !isPrivate;
}

export function isUserBlocked(blockedList, username) {
  return blockedList.includes(username);
}

export function formatFollowerCount(count) {
  if (count >= 1000000) return `${(count / 1000000).toFixed(1)}M`;
  if (count >= 1000) return `${(count / 1000).toFixed(1)}K`;
  return count.toString();
}
