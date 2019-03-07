/**
 * 
 */
package org.waal70.canbus;

import java.util.Objects;

import org.waal70.canbus.CanSocket.CanId;

/**
 * @author awaal
 *
 */

/**
 * This class represents a native CAN filter.
 */
public class CanFilter {

    /**
     * This bit inverts the filter.
     */
	public static final int ERR_FLAG = 0b00100000000000000000000000000000;
    public static final int INVERTED_BIT = ERR_FLAG;

    /**
     * This predefined filter accepts any CAN ID.
     */
    public static final CanFilter ANY = new CanFilter(new CanId(0), 0);

    /**
     * This predefined filter accepts no CAN ID at all.
     */
    public static final CanFilter NONE = new CanFilter(new CanId(0));

    /**
     * The size of the native representation of a {@link tel.schich.javacan.CanFilter}.
     */
    public static final int BYTES = Integer.BYTES * 2;

    /**
     * This filter mask can be used to match a CAN ID exactly.
     */
    public static final int EXACT = -1;

    private CanId id;
    private final int mask;

    /**
     * Creates a filter to exactly matches the given ID.
     *
     * @param id the CAN ID to match
     */
    public CanFilter(CanId id) {
        this(id, EXACT);
    }

    /**
     * Creates a filter with the given CAN ID and mask.
     *
     * @param id The CAN ID to match
     * @param mask the mask to match
     */
    public CanFilter(CanId id, int mask) {
        this.id = id;
        this.mask = mask;// & ~ERR_FLAG;
    }

    /**
     * Gets the CAN ID to be matched by this filter.
     *
     * @return the CAN ID
     */
    public int getId() {
        return id.getCanId();
    }

    /**
     * Gets the mask to used to match the CAN ID.
     *
     * @return the mask
     */
    public int getMask() {
        return mask;
    }

    /**
     * Checks if this filter is inverted.
     *
     * @return true if this filter is inverted
     */
    public boolean isInverted() {
        return (id.getCanId() & INVERTED_BIT) > 0;
    }

    /**
     * Checks if this filter matches its CAN ID exactly.
     *
     * @return true if this filter is exact
     */
    public boolean isExact() {
        return mask == EXACT;
    }

    /**
     * Matches this filter against the given CAN ID.
     * This method is implemented exactly like the kernel implements the filtering.
     *
     * @param id the CAN ID to match
     * @return true if the given CAN ID would be accepted by this filter
     */
    public boolean matchId(int id) {
        return (this.id.getCanId() & mask) == (id & mask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CanFilter canFilter = (CanFilter) o;
        return id == canFilter.id && mask == canFilter.mask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mask);
    }

    @Override
    public String toString() {
        return (isInverted() ? "~" : "") +  String.format("CanFilter(id=%X, mask=%X)", id, mask);
    }

}
