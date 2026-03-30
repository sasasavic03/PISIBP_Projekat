import { describe, it, expect } from "vitest";
import { validateLoginForm, validateRegisterForm, isLoggedIn } from "../features/auth/auth.utils";

describe("validateLoginForm", () => {
  it("vraca gresku ako je username prazan", () => {
    const result = validateLoginForm("", "password123");
    expect(result.valid).toBe(false);
    expect(result.error).toBe("Username is required");
  });

  it("vraca gresku ako je password prazan", () => {
    const result = validateLoginForm("john.doe", "");
    expect(result.valid).toBe(false);
    expect(result.error).toBe("Password is required");
  });

  it("vraca valid ako su username i password ispravni", () => {
    const result = validateLoginForm("john.doe", "password123");
    expect(result.valid).toBe(true);
    expect(result.error).toBeNull();
  });
});

describe("validateRegisterForm", () => {
  it("vraca gresku ako je username prazan", () => {
    const result = validateRegisterForm("", "password123", "john@mail.com");
    expect(result.valid).toBe(false);
    expect(result.error).toBe("Username is required");
  });

  it("vraca gresku ako je password kratak", () => {
    const result = validateRegisterForm("john.doe", "123", "john@mail.com");
    expect(result.valid).toBe(false);
    expect(result.error).toBe("Password must be at least 6 characters");
  });

  it("vraca gresku ako email nije ispravan", () => {
    const result = validateRegisterForm("john.doe", "password123", "invalidemail");
    expect(result.valid).toBe(false);
    expect(result.error).toBe("Invalid email");
  });

  it("vraca valid ako su svi podaci ispravni", () => {
    const result = validateRegisterForm("john.doe", "password123", "john@mail.com");
    expect(result.valid).toBe(true);
    expect(result.error).toBeNull();
  });
});

describe("isLoggedIn", () => {
  it("vraca false ako nema tokena", () => {
    localStorage.removeItem("token");
    expect(isLoggedIn()).toBe(false);
  });

  it("vraca true ako postoji token", () => {
    localStorage.setItem("token", "test_token_123");
    expect(isLoggedIn()).toBe(true);
    localStorage.removeItem("token");
  });
});
