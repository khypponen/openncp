package epsos.ccd.carecom.tsam.synchronizer.database.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains definitions of constants used by the persistents part of the application.
 */
public final class Constants {
	private final static String TEXT_CURRENT = "Current";
	private final static String TEXT_RETIRED = "Retired";
	private final static String TEXT_DUPLICATED = "Duplicated";
	private final static String TEXT_OUTDATED = "Outdated";
	private final static String TEXT_ERRONEOUS = "Errouneous";
	private final static String TEXT_LIMITED = "Limited";
	private final static String TEXT_INAPPROPRIATE = "Inappropriate";
	private final static String TEXT_CONCEPT_INACTIVE = "Concept Inactive";
	private final static String TEXT_IMPLIED = "Implied";
	private final static String TEXT_MOVED_ELSEWHERE = "Moved elsewhere";
	private final static String TEXT_PENDING_MOVE = "Pending move";
	private final static String TEXT_UNPUBLISHED = "Unpublished";
	private final static String TEXT_NOT_IN_USE = "Not in use";
	private final static String TEXT_CONCEPT_NON_CURRENT = "Concept non-current";
	private final static String TEXT_NON_CURRENT = "Non-Current";
	private final static String TEXT_VALID = "valid";
	private final static String TEXT_INVALID = "invalid";
	private final static String TEXT_UNSPECIFIED = "unspecified";
	private final static String TEXT_PREFERRED_TERM = "Preferred term";
	private final static String TEXT_SYNONYM = "Synonym";
	private final static String TEXT_FULLY_SPECIFIED_NAME = "Fully specified name";
	private final static String TEXT_CODE = "Code";
	
	private final static Integer VALUE_CURRENT = Integer.valueOf(0);
	private final static Integer VALUE_RETIRED = Integer.valueOf(1);
	private final static Integer VALUE_DUPLICATE = Integer.valueOf(2);
	private final static Integer VALUE_OUTDATED = Integer.valueOf(3);
	private final static Integer VALUE_ERRONEUS = Integer.valueOf(5);
	private final static Integer VALUE_LIMITED = Integer.valueOf(6);
	private final static Integer VALUE_INAPROPRIATE = Integer.valueOf(7);
	private final static Integer VALUE_CONCEPT_INACTIVE = Integer.valueOf(8);
	private final static Integer VALUE_IMPLIED = Integer.valueOf(9);
	private final static Integer VALUE_MOVED_ELSEWHERE = Integer.valueOf(10);
	private final static Integer VALUE_PENDING_MOVE = Integer.valueOf(11);
	private final static Integer VALUE_UNPUBLISHED = Integer.valueOf(98);
	private final static Integer VALUE_USPECIFIED = Integer.valueOf(0);
	private final static Integer VALUE_SYNONYM = Integer.valueOf(2);
	private final static Integer VALUE_FULLY_SPECIFIED_NAME = Integer.valueOf(3);
	private final static Integer VALUE_CODE = Integer.valueOf(9);
		
	/**
	 * Holds the value of preferred term value.
	 */
	public final static Integer VALUE_PREFERRED_TERM = Integer.valueOf(1);
	
	/**
	 * Contains a mapping between HT status values and the values expected in LTR by
	 * the code system version entity.
	 */
	public static final Map<Integer,String> MAP_CODE_SYSTEM_VERSION;
	
	/**
	 * Contains a mapping between HT status values and the values expected in LTR by
	 * the value set version entity.
	 */
	public static final Map<Integer,String> MAP_VALUE_SET_VERSION;
	
	/**
	 * Contains a mapping between HT status values and the values expected in LTR by
	 * the code system concept entity.
	 */
	public static final Map<Integer,String> MAP_CONCEPT;
	
	/**
	 * Contains a mapping between HT status values and the values expected in LTR by
	 * the designation entity.
	 */
	public static final Map<Integer,String> MAP_DESIGNATION;
	
	/**
	 * Contains a mapping between HT status values and the values expected in LTR by
	 * the transcoding entity.
	 */
	public static final Map<Integer,String> MAP_TRANSCODING;
	
	/**
	 * Contains a mapping between HT status values and the values expected in LTR by
	 * the designation entity.
	 */
	public static final Map<Integer,String> MAP_DESIGNATION_TYPE;
	
	static {
		Map<Integer,String> aMap = new HashMap<Integer, String>(12);
		aMap.put(VALUE_CURRENT, TEXT_CURRENT);
		aMap.put(VALUE_RETIRED, TEXT_RETIRED);
		aMap.put(VALUE_DUPLICATE, TEXT_RETIRED);
		aMap.put(VALUE_OUTDATED, TEXT_RETIRED);
		aMap.put(VALUE_ERRONEUS, TEXT_RETIRED);
		aMap.put(VALUE_LIMITED, TEXT_CURRENT);
		aMap.put(VALUE_INAPROPRIATE, TEXT_RETIRED);
		aMap.put(VALUE_CONCEPT_INACTIVE, TEXT_NOT_IN_USE);
		aMap.put(VALUE_IMPLIED, TEXT_RETIRED);
		aMap.put(VALUE_MOVED_ELSEWHERE, TEXT_RETIRED);
		aMap.put(VALUE_PENDING_MOVE, TEXT_CURRENT);
		aMap.put(VALUE_UNPUBLISHED, TEXT_NOT_IN_USE);
		MAP_CODE_SYSTEM_VERSION = Collections.unmodifiableMap(aMap);
		
		aMap = new HashMap<Integer, String>(12);
		aMap.put(VALUE_CURRENT, TEXT_CURRENT);
		aMap.put(VALUE_RETIRED, TEXT_RETIRED);
		aMap.put(VALUE_DUPLICATE, TEXT_RETIRED);
		aMap.put(VALUE_OUTDATED, TEXT_RETIRED);
		aMap.put(VALUE_ERRONEUS, TEXT_RETIRED);
		aMap.put(VALUE_LIMITED, TEXT_RETIRED);
		aMap.put(VALUE_INAPROPRIATE, TEXT_RETIRED);
		aMap.put(VALUE_CONCEPT_INACTIVE, TEXT_RETIRED);
		aMap.put(VALUE_IMPLIED, TEXT_RETIRED);
		aMap.put(VALUE_MOVED_ELSEWHERE, TEXT_RETIRED);
		aMap.put(VALUE_PENDING_MOVE, TEXT_RETIRED);
		aMap.put(VALUE_UNPUBLISHED, TEXT_RETIRED);
		MAP_VALUE_SET_VERSION = Collections.unmodifiableMap(aMap);
		
		aMap = new HashMap<Integer, String>(12);
		aMap.put(VALUE_CURRENT, TEXT_CURRENT);
		aMap.put(VALUE_RETIRED, TEXT_NON_CURRENT);
		aMap.put(VALUE_DUPLICATE, TEXT_DUPLICATED);
		aMap.put(VALUE_OUTDATED, TEXT_OUTDATED);
		aMap.put(VALUE_ERRONEUS, TEXT_ERRONEOUS);
		aMap.put(VALUE_LIMITED, TEXT_LIMITED);
		aMap.put(VALUE_INAPROPRIATE, TEXT_INAPPROPRIATE);
		aMap.put(VALUE_CONCEPT_INACTIVE, TEXT_CONCEPT_NON_CURRENT);
		aMap.put(VALUE_IMPLIED, TEXT_NON_CURRENT);
		aMap.put(VALUE_MOVED_ELSEWHERE, TEXT_MOVED_ELSEWHERE);
		aMap.put(VALUE_PENDING_MOVE, TEXT_PENDING_MOVE);
		aMap.put(VALUE_UNPUBLISHED, TEXT_CONCEPT_NON_CURRENT);
		MAP_CONCEPT = Collections.unmodifiableMap(aMap);
		
		aMap = new HashMap<Integer, String>(12);
		aMap.put(VALUE_CURRENT, TEXT_CURRENT);
		aMap.put(VALUE_RETIRED, TEXT_RETIRED);
		aMap.put(VALUE_DUPLICATE, TEXT_RETIRED);
		aMap.put(VALUE_OUTDATED, TEXT_RETIRED);
		aMap.put(VALUE_ERRONEUS, TEXT_RETIRED);
		aMap.put(VALUE_LIMITED, TEXT_CURRENT);
		aMap.put(VALUE_INAPROPRIATE, TEXT_RETIRED);
		aMap.put(VALUE_CONCEPT_INACTIVE, TEXT_RETIRED);
		aMap.put(VALUE_IMPLIED, TEXT_RETIRED);
		aMap.put(VALUE_MOVED_ELSEWHERE, TEXT_RETIRED);
		aMap.put(VALUE_PENDING_MOVE, TEXT_CURRENT);
		aMap.put(VALUE_UNPUBLISHED, TEXT_RETIRED);
		MAP_DESIGNATION = Collections.unmodifiableMap(aMap);
		
		aMap = new HashMap<Integer, String>(12);
		aMap.put(VALUE_CURRENT, TEXT_VALID);
		aMap.put(VALUE_RETIRED, TEXT_INVALID);
		aMap.put(VALUE_DUPLICATE, TEXT_INVALID);
		aMap.put(VALUE_OUTDATED, TEXT_INVALID);
		aMap.put(VALUE_ERRONEUS, TEXT_INVALID);
		aMap.put(VALUE_LIMITED, TEXT_VALID);
		aMap.put(VALUE_INAPROPRIATE, TEXT_INVALID);
		aMap.put(VALUE_CONCEPT_INACTIVE, TEXT_INVALID);
		aMap.put(VALUE_IMPLIED, TEXT_INVALID);
		aMap.put(VALUE_MOVED_ELSEWHERE, TEXT_INVALID);
		aMap.put(VALUE_PENDING_MOVE, TEXT_VALID);
		aMap.put(VALUE_UNPUBLISHED, TEXT_INVALID);
		MAP_TRANSCODING = Collections.unmodifiableMap(aMap);
		
		aMap = new HashMap<Integer, String>(5);
		aMap.put(VALUE_USPECIFIED, TEXT_UNSPECIFIED);
		aMap.put(VALUE_PREFERRED_TERM, TEXT_PREFERRED_TERM);
		aMap.put(VALUE_SYNONYM, TEXT_SYNONYM);
		aMap.put(VALUE_FULLY_SPECIFIED_NAME,TEXT_FULLY_SPECIFIED_NAME);
		aMap.put(VALUE_CODE, TEXT_CODE);
		MAP_DESIGNATION_TYPE = Collections.unmodifiableMap(aMap);
	}
	
	/**
	 * Contains the default length of a string that is allowed in the LTR.
	 */
	public final static int DEFAULT_CHARACTER_LENGTH = 255;
}
