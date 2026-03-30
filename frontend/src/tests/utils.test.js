import { describe, it, expect } from "vitest";
import { mapMediaToImages, isPostLikedByUser, isValidFileSize } from "../features/posts/post.utils";

describe("mapMediaToImages", () => {
  it("mapira media_list u pune URL-ove", () => {
    const mediaList = [
      { media_url: "slika.jpg", media_type: "IMAGE" },
      { media_url: "video.mp4", media_type: "VIDEO" },
    ];
    const result = mapMediaToImages(mediaList);
    expect(result[0]).toBe("http://localhost:8080/api/posts/media/slika.jpg");
    expect(result[1]).toBe("http://localhost:8080/api/posts/media/video.mp4");
  });

  it("vraca prazan niz ako je mediaList prazan", () => {
    expect(mapMediaToImages([])).toEqual([]);
  });
});

describe("isPostLikedByUser", () => {
  it("vraca true ako je korisnik lajkovao post", () => {
    const likedBy = [
      { id: 1, username: "john.doe" },
      { id: 2, username: "jane.smith" },
    ];
    expect(isPostLikedByUser(likedBy, "john.doe")).toBe(true);
  });

  it("vraca false ako korisnik nije lajkovao post", () => {
    const likedBy = [
      { id: 1, username: "john.doe" },
    ];
    expect(isPostLikedByUser(likedBy, "mike.ross")).toBe(false);
  });

  it("vraca false ako je likedBy prazan niz", () => {
    expect(isPostLikedByUser([], "john.doe")).toBe(false);
  });
});

describe("isValidFileSize", () => {
  it("vraca true ako je fajl manji od 50MB", () => {
    const fileSize = 10 * 1024 * 1024; // 10MB
    expect(isValidFileSize(fileSize)).toBe(true);
  });

  it("vraca false ako je fajl veci od 50MB", () => {
    const fileSize = 60 * 1024 * 1024; // 60MB
    expect(isValidFileSize(fileSize)).toBe(false);
  });

  it("vraca true ako je fajl tacno 50MB", () => {
    const fileSize = 50 * 1024 * 1024; // tacno 50MB
    expect(isValidFileSize(fileSize)).toBe(true);
  });
});
