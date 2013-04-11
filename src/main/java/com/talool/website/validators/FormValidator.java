package com.talool.website.validators;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

/**
 * Form validator singleton class. Currently wraps
 * org.apache.commons.validator.GenericValidator and
 * org.apache.commons.validator.CreditCardValidator. It does not inclde all of
 * Generics validations, so please feel free to add more validations to this
 * class.
 * 
 * Provides some basic date constants for use of date pattern matching.
 * 
 * @author chris.lintz
 * @see http://jakarta.apache.org/commons/validator/api-release/
 * 
 */

public class FormValidator
{
	private static final Logger logger = Logger.getLogger(FormValidator.class.getName());
	private static FormValidator instance = new FormValidator();
	public static final String TWO_DIGIT_MONTH = "MM";
	public static final String FOUR_DIGIT_YEAR = "yyyy";
	public static final String MM_DD_YYYYY = "MM/dd/yyyy";

	public static FormValidator getInstance()
	{
		return instance;
	}

	private FormValidator()
	{}

	/**
	 * Takes a two digit month and a four digit year and returns true if the date
	 * has passed or false otherwise.
	 * 
	 * @param twoDigMon
	 * @param twoDigYear
	 * @return
	 */
	public boolean hasDatePassed(String mm, String yyyy)
	{
		Calendar curCal = Calendar.getInstance();
		int curMonth = curCal.get(Calendar.MONTH);
		// following line added for potentially future support if calendar changes
		if (curCal.getMinimum(Calendar.MONTH) == 0)
			curMonth += 1;

		int curYear = curCal.get(Calendar.YEAR);
		// properly converts 2 digit to a single digit
		try
		{
			int MM = Integer.valueOf(mm).intValue();
			int YYYY = Integer.valueOf(yyyy).intValue();
			if (YYYY < curYear)
				return true;
			if (YYYY == curYear && (MM < curMonth))
				return true;
		}
		catch (NumberFormatException ne)
		{
			logger.error("error converting date string:" + ne.getMessage());
		}

		return false;
	}

	/**
	 * Takes a two digit month, a two digit day, and a four digit year and returns
	 * true if the date has passed or false otherwise.
	 * 
	 * @param twoDigMon
	 * @param twoDigDay
	 * @param twoDigYear
	 * @return
	 */
	public boolean hasDatePassed(String mm, String dd, String yyyy)
	{
		Calendar curCal = Calendar.getInstance();
		int curMonth = curCal.get(Calendar.MONTH);
		// following line added for potentially future support if calendar changes
		if (curCal.getMinimum(Calendar.MONTH) == 0)
			curMonth += 1;

		int curDay = curCal.get(Calendar.DAY_OF_MONTH);

		int curYear = curCal.get(Calendar.YEAR);
		// properly converts 2 digit to a single digit
		try
		{
			int MM = Integer.parseInt(mm);
			int DD = Integer.parseInt(dd);
			int YYYY = Integer.parseInt(yyyy);
			if (YYYY < curYear)
				return true;
			if (YYYY == curYear && (MM < curMonth) && (DD < curDay))
				return true;
		}
		catch (NumberFormatException ne)
		{
			logger.error("error converting date string:" + ne.getMessage());
		}

		return false;
	}

	/**
	 * Checks if the field isn't null and length of the field is greater than zero
	 * not including whitespace.
	 * 
	 * @param value
	 * @return true if value is blank or null false otherwise
	 */
	public boolean isBlankOrNull(String value)
	{
		return GenericValidator.isBlankOrNull(value);
	}

	/**
	 * Checks whether the number is all digits and between 12 and 20
	 * 
	 * @param value
	 * @return true if the value is valid Credit Card Number false otherwise
	 */
	public boolean isCreditCard(String cardNum)
	{

		return ((!isBlankOrNull(cardNum)) && (isDigits(cardNum)) && (cardNum.length() >= 12) && (cardNum
				.length() <= 20));
	}

	/**
	 * 
	 * @param value
	 * @param datePattern
	 * @param strict
	 * @return false if not a date, true if it is a date
	 */
	public boolean isDate(String value, String datePattern, boolean strict)
	{
		return GenericValidator.isDate(value, datePattern, strict);
	}

	/**
	 * 
	 * @param value
	 * @return if value is all numbers (0-9)
	 */
	public boolean isDigits(String value)
	{
		return isLong(value);
	}

	public boolean isDouble(String value)
	{
		return GenericValidator.isDouble(value);
	}

	/*
	 * determins whether the given email address is valid
	 */
	public boolean isEmail(String email)
	{
		return GenericValidator.isEmail(email);
	}

	public boolean isFloat(String value)
	{
		return GenericValidator.isFloat(value);
	}

	/**
	 * Checks if a value is within a range (min & max specified in the vars
	 * attribute).
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return true if it is in range or false otherwise
	 */
	public boolean isInRange(int value, int min, int max)
	{
		return GenericValidator.isInRange(value, min, max);
	}

	/**
	 * 
	 * @param value
	 * @return true value can safely be converted to a int primitive.
	 */
	public boolean isInt(String value)
	{
		return GenericValidator.isInt(value);
	}

	/**
	 * 
	 * @param value
	 * @return true value can safely be converted to a long primitive.
	 */

	public boolean isLong(String value)
	{
		return GenericValidator.isLong(value);
	}

	/**
	 * Validate the specified credit card CVN number.
	 * 
	 * @param cvn
	 * @return true/false
	 */
	public boolean isValidCVN(String cvn)
	{
		if (StringUtils.isNotBlank(cvn) && (cvn.length() >= 3 && cvn.length() <= 4) && isDigits(cvn))
			return true;
		return false;
	}

	/**
	 * This validator takes a String list of comma-delimitted emails and validates
	 * each one
	 * 
	 * @param emails
	 * @param splitEmails
	 *          the passed in value of split email addresses upon success. Upon an
	 *          error, this set will contain the email addresses that have
	 *          problems
	 * @returns null if any email addresses are invalid, otherwise it returns a
	 *          Set of the email addresses as strings
	 */
	public boolean isValidEmails(String emails, Set<String> splitEmails)
	{
		if (emails == null)
			return false;
		if (splitEmails == null)
			splitEmails = new HashSet<String>();
		boolean isGoodEmails = true;
		Set<String> badEmails = new HashSet<String>();

		String[] emailsSplit = emails.split(",");

		for (int i = 0; i < emailsSplit.length; i++)
		{

			if (isEmail(emailsSplit[i]))
			{
				splitEmails.add(emailsSplit[i]);
			}
			else
			{
				badEmails.add(emailsSplit[i]);
				isGoodEmails = false;
			}

		}
		if (!isGoodEmails)
		{
			splitEmails.clear();
			splitEmails.addAll(badEmails);
		}

		return isGoodEmails;

	}

	/**
	 * 
	 * @param phone
	 * @return true value if phone has between 7 and 20 digits or is empty
	 */
	public boolean isValidNonUSPhone(String phone)
	{
		if (isBlankOrNull(phone))
			return true;
		return (phone.length() <= 20 && (phone.length() >= 7));
	}

	/**
	 * 
	 * @param phone
	 * @return true value if phone has between 7 and 20 digits
	 */
	public boolean isValidNonUSRequiredPhone(String phone)
	{
		if (isBlankOrNull(phone))
			return false;
		return (phone.length() <= 20 && (phone.length() >= 7));
	}

	/**
	 * 
	 * @param phone
	 * @return true value is 10 digits or is empty
	 */
	public boolean isValidPhone(String phone)
	{
		if (isBlankOrNull(phone))
			return true;
		return (isDigits(phone) && (phone.length() == 10 || phone.length() == 7));
	}

	/**
	 * 
	 * @param phone
	 * @return true value is 10 digits
	 */
	public boolean isValidRequiredPhone(String phone)
	{
		return (!isBlankOrNull(phone) && isDigits(phone) && (phone.length() == 10));
	}

	/**
	 * <p>
	 * Checks if the value matches the regular expression.
	 * </p>
	 * 
	 * @param value
	 *          The value validation is being performed on.
	 * @param regexp
	 *          The regular expression.
	 */

	public boolean matchRegexp(String value, String regexp)
	{
		return GenericValidator.matchRegexp(value, regexp);
	}

	/**
	 * 
	 * @param value
	 * @return the value without any characters that were not digits (0-9)
	 */
	public String stripNonDigits(String value)
	{
		return value.replaceAll("[^0-9]", "");
	}

}