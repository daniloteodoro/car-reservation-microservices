package com.carrental;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

import com.carrental.util.StringUtils;

public class StringUtilsTest {
	
	@Test
	public void testNullString() {
		String nullStr = null;
		Assert.assertTrue(StringUtils.isEmpty(nullStr));
	}
	
	@Test
	public void testEmptyString() {
		String emptyStr = "";
		Assert.assertTrue(StringUtils.isEmpty(emptyStr));
	}
	
	@Test
	public void testStringContainingText() {
		String str = "any text";
		Assert.assertFalse(StringUtils.isEmpty(str));
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullStringWithMessage() {
		String nullStr = null;
		StringUtils.requireNonEmpty(nullStr, "Any Message");
	}
	
	@Test(expected=NullPointerException.class)
	public void testEmptyStringWithMessage() {
		String emptyStr = "";
		StringUtils.requireNonEmpty(emptyStr, "Any Message");
	}
	
	@Test
	public void testStringContainingTextWithoutMessage() {
		String str = "any text";
		String result = StringUtils.requireNonEmpty(str, "Any Message");
		assertThat(result, is(equalTo("any text")));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testNullStringWithException() {
		String nullStr = null;
		StringUtils.requireNonEmpty(nullStr, () -> new IndexOutOfBoundsException());
	}
	
	@Test(expected=OutOfMemoryError.class)
	public void testEmptyStringWithException() {
		String emptyStr = "";
		StringUtils.requireNonEmpty(emptyStr, OutOfMemoryError::new);
	}
	
	@Test
	public void testStringContainingTextWithoutException() {
		String str = "any text";
		String result = StringUtils.requireNonEmpty(str, () -> new NullPointerException());
		assertThat(result, is(equalTo("any text")));
	}

}




















