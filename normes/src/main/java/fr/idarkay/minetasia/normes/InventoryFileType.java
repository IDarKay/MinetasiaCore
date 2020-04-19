package fr.idarkay.minetasia.normes;

/**
 * @author AloIs B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
public enum InventoryFileType {

    /**
     * no file
     */
    EMPTY(),
    /**
     * all borders recover by material
     */
    SQUARE(),
    /**
     * the top line recover by material
     */
    LINE_TOP(),
    /**
     * the bottom line recover by material
     */
    LINE_BOTTOM(),
    /**
     * {@link InventoryFileType#LINE_TOP } and {@link InventoryFileType#LINE_BOTTOM}
     */
    LINES(),

    /**
     * all case have recovered by material
     */
    FULL(),
    ;


}
