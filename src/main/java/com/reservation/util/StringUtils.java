package com.reservation.util;

import java.util.Objects;
import java.util.function.Supplier;

public class StringUtils {
	
	public static boolean isEmpty(String str) {
		return (str == null) ||
				str.isBlank();
	}
	
	/**
     * Checks that the specified string "str" is not {@code null}. This
     * method is designed primarily for doing parameter validation in methods
     * and constructors, as demonstrated below:
     * <blockquote><pre>
     * public Foo(Bar bar) {
     *     this.bar = StringUtils.requireNonEmpty(bar, "Argument 'bar' must contain text");
     * }
     * </pre></blockquote>
     * 
     * This class is based on {@link Objects#requireNonNull(Object, String) Objects.requireNonNull}
     *
     * @param str the String reference to check for nullity or emptiness
     * @return {@code str} if not {@code null} or empty
     * @param errorMessage detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @throws NullPointerException if {@code str} is {@code null} or empty <br>
     * 
     * @see Objects#requireNonNull(Object, String)
     */
	public static String requireNonEmpty(String str, String errorMessage) {
		if (isEmpty(str) ) {
			throw new NullPointerException(errorMessage);
		}
	    return str;
	}
	
	/**
     * Checks that the specified string "str" is not {@code null}. This
     * method is designed primarily for doing parameter validation in methods
     * and constructors, as demonstrated below:
     * <blockquote><pre>
     * public Foo(Bar bar) {
     *     this.bar = StringUtils.requireNonEmpty(bar, () -> new IllegalStateException("Argument 'bar' must contain text"));
     * }
     * </pre></blockquote>
     * 
     * This class is based on {@link Objects#requireNonNull(Object, String) Objects.requireNonNull}
     * @param <E> Type of the exception to be thrown
     * @param exception The supplier which will return the exception to
     * be thrown
     * 
     * @apiNote A method reference to the exception constructor with an empty
     * argument list can be used as the supplier. For example,
     * {@code IllegalStateException::new}
     *
     * @param str the String reference to check for nullity or emptiness
     * @return {@code str} if not {@code null} or empty
     * @param errorMessage detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @throws E if {@code str} is {@code null} or empty <br>
     * 
     * @see Objects#requireNonNull(Object, String)
     */
	public static <E extends Throwable> String requireNonEmpty(String str, Supplier<? extends E> exception) throws E {
		if (isEmpty(str) ) {
			throw exception.get();
		}
	    return str;
	}
	
}
