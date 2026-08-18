package net.ron.zam.api.rarity;

/**
 * Represents different levels of rarity with associated colors.
 */
public enum Rarity {
    COMMON(0xFF3498DB, "Common"),
    UNCOMMON(0xFF8A2BE2, "Uncommon"),
    RARE(0xFFFF69B4, "Rare"),
    VERY_RARE(0xFFE74C3C, "Very Rare"),
    ULTRA_RARE(0xFFFFD700, "Ultra Rare");

    private final int color;
    private final String displayName;

    /**
     * Constructs a Rarity enum with a specified color and display name.
     *
     * @param color       The color associated with the rarity.
     * @param displayName The human-readable name of the rarity.
     */
    Rarity(int color, String displayName) {
        this.color = color;
        this.displayName = displayName;
    }

    /**
     * Gets the color associated with the rarity.
     *
     * @return The color as an integer (ARGB format).
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets the display name of the rarity.
     *
     * @return The human-readable name of the rarity.
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}