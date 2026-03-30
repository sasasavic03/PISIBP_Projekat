import { describe, it, expect } from "vitest";
import { canViewContent, isUserBlocked, formatFollowerCount } from "../features/profile/profileutils";

describe("canViewContent", () => {
  it("vlasnik uvek moze da vidi sadrzaj", () => {
    expect(canViewContent(true, false, true)).toBe(true);
  });

  it("pratilac moze da vidi sadrzaj privatnog profila", () => {
    expect(canViewContent(false, true, true)).toBe(true);
  });

  it("svako moze da vidi javni profil", () => {
    expect(canViewContent(false, false, false)).toBe(true);
  });

  it("ne moze da vidi privatni profil ako ne prati", () => {
    expect(canViewContent(false, false, true)).toBe(false);
  });
});

describe("isUserBlocked", () => {
  it("vraca true ako je korisnik blokiran", () => {
    const blocked = ["jane.smith", "mike.ross"];
    expect(isUserBlocked(blocked, "jane.smith")).toBe(true);
  });

  it("vraca false ako korisnik nije blokiran", () => {
    const blocked = ["jane.smith"];
    expect(isUserBlocked(blocked, "john.doe")).toBe(false);
  });

  it("vraca false ako je lista blokiranih prazna", () => {
    expect(isUserBlocked([], "john.doe")).toBe(false);
  });
});

describe("formatFollowerCount", () => {
  it("formatira broj manji od 1000", () => {
    expect(formatFollowerCount(500)).toBe("500");
  });

  it("formatira broj u hiljade", () => {
    expect(formatFollowerCount(1500)).toBe("1.5K");
  });

  it("formatira broj u milione", () => {
    expect(formatFollowerCount(1500000)).toBe("1.5M");
  });
});
